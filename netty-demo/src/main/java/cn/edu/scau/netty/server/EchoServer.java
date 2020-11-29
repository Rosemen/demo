package cn.edu.scau.netty.server;

import cn.edu.scau.netty.server.handler.EchoChildChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noob
 * @date 2020/11/29 14:39
 * @description
 */
public class EchoServer {

    private static final int PORT = 8080;

    private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);

    public void bind(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new EchoChildChannelHandler());
            ChannelFuture future = serverBootstrap.bind(port).sync();

            future.channel().closeFuture().sync();
        } catch (Throwable throwable) {
            logger.error("echoServer start failed", throwable);
            throw new RuntimeException(throwable);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoServer().bind(PORT);
    }
}
