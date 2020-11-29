package cn.edu.scau.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noob
 * @date 2020/11/29 14:53
 * @description
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);

    private int counter = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("echoServer read failed", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       String body = (String) msg;
       logger.info("receive message: {}, count: {}", body, ++counter);
       body = body + "$_";
       ByteBuf response = Unpooled.copiedBuffer(body.getBytes());
       ctx.writeAndFlush(response);
    }
}
