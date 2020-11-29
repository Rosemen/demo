package cn.edu.scau.netty.client;

import cn.edu.scau.netty.client.handler.ChildChannelHandler;
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
 * @date 2020/11/15 16:38
 * @description 时间客户端
 */
public class TimeClient {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 8080;

    private static final Logger logger = LoggerFactory.getLogger(TimeClient.class);

    public void connect() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChildChannelHandler());
            ChannelFuture channelFuture = bootstrap.connect(HOST, PORT).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Throwable throwable) {
            logger.info("connect error", throwable);
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new TimeClient().connect();
    }
}
