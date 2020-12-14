package com.bitlab.connect;

import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import com.bitlab.connect.data.NodeHashMap;
import com.bitlab.connect.data.StateBundle;
import com.bitlab.constant.Constants;
import com.bitlab.message.data.IPv6;
import com.bitlab.message.data.NetAddr;
import com.bitlab.util.Lookup;

import org.slf4j.Logger;
/**
 * Obsługuje połączenia
 */
public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class); // do pisania logów
    private EventLoopGroup worker;//do obsługi połączeń
    public static final NodeHashMap peers = new NodeHashMap(); //znalezione peery

    public static final LinkedBlockingQueue<NetAddr> queue = new LinkedBlockingQueue<>();//kolejka do przejrzenia

    public ConnectionManager(){
        worker = new NioEventLoopGroup(Constants.THREADS);
    }

    public void run()throws Exception{
        try {
            Bootstrap bootstrap = getNewBootstrap(worker);//do tworzenia nowych połączeń ("kanałów")

            ArrayList<InetAddress> byDNS = new ArrayList<>();// adresy początkowe w sieci do przejrzenia w poszukiwaniu kolejnych peerów
            byDNS.addAll(Arrays.asList(Lookup.lookup(Constants.SEED_DNS[0]))); //TODO: czy nie powinniśmy od razu całej tablicy SEED'ów przejrzeć?
            for (InetAddress addr : byDNS) {
                queue.put(new NetAddr(0, 0, addr.getHostAddress(),8333));
            }
            logger.info("znaleziono przez DNS: " + byDNS.size());
            ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            while(!queue.isEmpty()){
                while (channels.size()<=Constants.THREADS) {
                    NetAddr target = queue.take();
                    logger.debug("Connecting to " + target.getIp().toString() + ":" + target.getPort());
                    StateBundle bundle = new StateBundle(target.getIp(), target.getPort(), new Date().getTime());
                    ConnectionHandler.map.put(target.getIp().toString(), bundle);
                    ChannelFuture future = bootstrap.connect(target.getIp().toString(), target.getPort());
                    channels.add(future.channel());
                }
            }
            
            
            channels.close().sync();
        } finally{
            worker.shutdownGracefully();    
        }

    }
    /**
     * Zwraca odpowiednia sparametryzowany obiekt do tworzenia nowych połączeń ("Kanałów")
     * @param group
     * @return
     */
    private Bootstrap getNewBootstrap(EventLoopGroup group) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new MessageDecoder(), new ConnectionHandler());
            }
        });

        return bootstrap;
    }

}
