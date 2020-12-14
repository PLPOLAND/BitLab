package com.bitlab.connect;

import com.bitlab.connect.data.StateBundle;
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
        String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        bundle = map.get(IPv6.convert(ip));
        if(bundle == null) {
            logger.warn("Check out this ip: " + ip);
            ctx.close();
        }
        bundle.setSuccess(true);
        logger.info("ChannelActive with: " + IPv6.convert(bundle.getIp()) + "/" + bundle.getPort());
        writeAndFlush(ctx, new Version(bundle.getIp(), bundle.getPort()).serialize());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Closed:" + bundle.getIp() + " / " + bundle.getPort());
    }
    /**
     * Obsługa komend przychodzących
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // msg is message
        Message message = (Message) msg;
        switch (message.getCommand()) {
            case VERSION:
                logger.debug("Got: VERSION; From "+IPv6.convert(bundle.getIp()));
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
                logger.info(addr.toString());

                if(isSendGetaddr && addr.getList().size() > 1) {

                    for(NetAddr netAddr : addr.getList()) {
                            ConnectionManager.queue.put(netAddr);
                    }

                    // for(NetAddr netAddr : addr.getList())
                    //     ConnectionManagerOld.map.insert(netAddr, bundle);
                    ctx.close();
                }
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
