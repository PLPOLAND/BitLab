package com.bitlab.connect;

import com.bitlab.connect.data.StateBundle;
import com.bitlab.connect.data.TypeOfAction;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.bitlab.message.*;
import com.bitlab.message.data.IPv6;
import com.bitlab.message.data.NetAddr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Obiekt do obsługi połączeń pomiędzy peerami
 */
public class ConnectionHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);
    public static final ConcurrentHashMap<String, StateBundle> map = new ConcurrentHashMap<>();
    private boolean isSendGetaddr;
    private StateBundle bundle;
    TypeOfAction typeOfAction;


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("ChannelRegistered: " + ctx.name());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("ChannelUnRegistered: " + ctx.name());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        isSendGetaddr = false;
        String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        bundle = map.get(IPv6.convert(ip));
        if (bundle == null) {
            logger.warn("Check out this ip: " + ip);
            ctx.close();
        }
        typeOfAction = bundle.getTypeOfAction();
        bundle.setSuccess(true);
        logger.info("ChannelActive with: " + IPv6.convert(bundle.getIp()) + "/" + bundle.getPort());

        switch (typeOfAction) {
            case GETADDR:
            case SCAN:
            case PING:
            //Dla Skanowania sieci / pobierania adresu / testu pingowania zainicjuj połączenie z peerem (wyślij VERSION)
                writeAndFlush(ctx, new Version(bundle.getIp(), bundle.getPort()).serialize());
                break;
            case GETDATA:
                writeAndFlush(ctx, new GetData().serialize());
                break;
            default:
                break;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Closed:" + bundle.getIp() + ":" + bundle.getPort());
    }
    /**
     * Obsługa komend przychodzących
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Message message = (Message) msg;
        switch (typeOfAction) {
            case GETADDR://Pobranie adresów
                switch (message.getCommand()) {
                    case VERSION:
                        logger.info("Got: VERSION; From " + IPv6.convert(bundle.getIp()));
                        writeAndFlush(ctx, new Verack().serialize());
                        bundle.setVersion(new Version(message.getHeader()));
                        break;
                    case VERACK:
                        logger.info("Got: VERACK; From " + IPv6.convert(bundle.getIp()));
                        writeAndFlush(ctx, new GetAddr().serialize());
                        isSendGetaddr = true;
                        break;
                    case ADDR:
                        logger.info("Got: ADDR; From " + IPv6.convert(bundle.getIp()));
                        Addr addr = new Addr(message.getHeader());
                        // logger.info(addr.toString());

                        if (isSendGetaddr && addr.getList().size() > 1) {

                            for (NetAddr netAddr : addr.getList()) {
                                // jeśli peer o takim adresie nie był jeszcze scanowany to dodaj do kolejki
                                if (!ConnectionManager.peers.insert(netAddr, new StateBundle(netAddr.getIp(), netAddr.getPort(), 0))) {
                                    logger.debug("Poraz pierwszy: " + netAddr.getIp().toString());
                                } else {
                                    logger.debug(netAddr.getIp().toString() + " był już na liście w systemie");
                                }
                            }
                            ctx.close();
                        }
                        break;
                    case PING:
                        Ping ping = new Ping(message.getHeader());
                        logger.info("Got: PING("+ping.getNonce()+"); From " + IPv6.convert(bundle.getIp()));
                        writeAndFlush(ctx, new Pong(ping.getNonce()).serialize());
                        logger.info("Sent: PONG; TO:" + IPv6.convert(bundle.getIp()));
                        break;
                    case REJECT: // sprawdzanie możliwości wywalenia błędu w połączeniu między peerami
                        Reject reject = new Reject(message.getHeader());
                        logger.info("GOT: REJECT" + reject.toString());
                        break;
                }
                break;
            case SCAN://Scanowanie sieci
                switch (message.getCommand()) {
                    case VERSION:
                        logger.debug("Got: VERSION; From " + IPv6.convert(bundle.getIp()));
                        writeAndFlush(ctx, new Verack().serialize());
                        bundle.setVersion(new Version(message.getHeader()));
                        break;
                    case VERACK:
                        logger.debug("Got: VERACK; From " + IPv6.convert(bundle.getIp()));
                        writeAndFlush(ctx, new GetAddr().serialize());
                        isSendGetaddr = true;
                        break;
                    case ADDR:
                        logger.debug("Got: ADDR; From " + IPv6.convert(bundle.getIp()));
                        Addr addr = new Addr(message.getHeader());

                        if (isSendGetaddr && addr.getList().size() > 1) {

                            for (NetAddr netAddr : addr.getList()) {
                                // jeśli peer o takim adresie nie był jeszcze scanowany to dodaj do kolejki
                                if (!ConnectionManager.peers.insert(netAddr,
                                        new StateBundle(netAddr.getIp(), netAddr.getPort(), 0))) {
                                    ConnectionManager.queue.put(netAddr);
                                    logger.debug("Dodano do kolejki: " + netAddr.getIp().toString());
                                } else {
                                    logger.debug("Pominięto: " + netAddr.getIp().toString() + " był już w kolejce");
                                }
                            }
                            ctx.close();
                        }
                        break;
                    case PING:
                        Ping ping = new Ping(message.getHeader());
                        logger.debug("Got: PING(" + ping.getNonce() + "); From " + IPv6.convert(bundle.getIp()));
                        writeAndFlush(ctx, new Pong(ping.getNonce()).serialize());
                        logger.debug("Sent: PONG; TO:" + IPv6.convert(bundle.getIp()));
                        break;
                    case REJECT: // sprawdzanie możliwości wywalenia błędu w połączeniu między peerami
                        Reject reject = new Reject(message.getHeader());
                        logger.info("GOT: REJECT" + reject.toString());
                        break;
                }
                break;
            case PING: //sending PING and wait for PONG
                switch (message.getCommand()) {
                    case VERSION:
                    logger.info("Got: VERSION; From " + IPv6.convert(bundle.getIp()));
                    writeAndFlush(ctx, new Verack().serialize());
                    bundle.setVersion(new Version(message.getHeader()));
                    break;
                    case VERACK:
                        logger.info("Got: VERACK; From " + IPv6.convert(bundle.getIp()));
                        Ping tmp = new Ping();
                        writeAndFlush(ctx, tmp.serialize());
                        logger.info("Sent: Ping("+tmp.getNonce()+") to: " + IPv6.convert(bundle.getIp()));
                        break;
                    case ADDR:
                        logger.info("Got: ADDR; From " + IPv6.convert(bundle.getIp()));
                        break;
                    case PONG:
                        Pong tmp2 = new Pong(message.getHeader());
                        logger.info("Got: PONG("+ tmp2.getNonce()+ ") from: " + IPv6.convert(bundle.getIp()));
                        ctx.close();
                        break;
                    case REJECT: // sprawdzanie możliwości wywalenia błędu w połączeniu między peerami
                        Reject reject = new Reject(message.getHeader());
                        logger.info("GOT: REJECT" + reject.toString());
                        break;
                }
                break;
            default:
                break;
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(bundle == null) {
          logger.error("exceptionCaught (bundle is null):" + ctx.channel().remoteAddress());
        } else {
            logger.error("exceptionCaught: " + bundle.getIp() + "/" + bundle.getPort(), cause);
            if(cause instanceof IOException) {
                bundle.setSuccess(false);
                bundle.setException(cause);
            }
        }

        ctx.close();
    }

    private void writeAndFlush (ChannelHandlerContext ctx, ByteBuffer buffer) {
        ctx.writeAndFlush(Unpooled.wrappedBuffer(buffer));
    }
}
