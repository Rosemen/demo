package cn.edu.scau.netty.server;

import cn.edu.scau.netty.server.handler.ChildChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author noob
 * @date 2020/11/15 16:38
 * @description  时间服务器
 */
public class TimeServer {

    private static final Logger logger = LoggerFactory.getLogger(TimeServer.class);

    /**
     * 绑定服务器端口
     * @param port
     */
    public void bind(int port) {
        EventLoopGroup bossGroup = null;
        EventLoopGroup workerGroup = null;
        try {
            //1. 配置NIO线程组
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            //2. 绑定端口
            ChannelFuture future = serverBootstrap.bind(port).sync();
            //3. 监听端口关闭
            future.channel().closeFuture().sync();
        }catch (Throwable ex) {
            logger.error("init error", ex);
        } finally {
            //4. 关闭线程组
            Optional.ofNullable(bossGroup).ifPresent(boss -> boss.shutdownGracefully());
            Optional.ofNullable(workerGroup).ifPresent(worker -> worker.shutdownGracefully());
        }
    }

    public static void main(String[] args) {
        new TimeServer().bind(8080);
    }
}
