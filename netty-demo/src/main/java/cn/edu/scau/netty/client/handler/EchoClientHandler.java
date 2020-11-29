package cn.edu.scau.netty.client.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noob
 * @date 2020/11/29 15:33
 * @description
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    private int counter = 0;

    private int num = 10;

    private String message = "Hello, welcome to Netty.$_";

    private static final Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client read failed", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0 ; i < num; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        logger.info("client receive, message: {}, count: {}", message, ++counter);
    }
}
