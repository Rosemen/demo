package cn.edu.scau.netty.client;

import cn.edu.scau.netty.client.handler.EchoChildChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noob
 * @date 2020/11/29 15:23
 * @description
 */
public class EchoClient {

    private static final String SERVER_HOST = "127.0.0.1";

    private static final int PORT = 8080;

    private static final Logger logger = LoggerFactory.getLogger(EchoClient.class);

    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new EchoChildChannelHandler());

            ChannelFuture future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        }catch (Throwable throwable) {
            logger.error("client start failed", throwable);
            throw new RuntimeException(throwable);
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoClient().connect(SERVER_HOST, PORT);
    }
}
