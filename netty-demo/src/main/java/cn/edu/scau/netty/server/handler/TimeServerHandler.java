package cn.edu.scau.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * @author noob
 * @date 2020/11/15 16:55
 * @description 处理数据
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ChannelHandlerAdapter.class);

    private static final String QUERY_ORDER_TIME = "QUERY TIME ORDER";

    private int count = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("receive order error", cause);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1. 解析客户端信息
        String body = (String)msg;
        String currentTime = body.equalsIgnoreCase(QUERY_ORDER_TIME)?new Date(System.currentTimeMillis()).toString():"ERROR ORDER";
        logger.info("receive order message: {}, response: {}, count: {}", body, currentTime, ++count);
        //2. 返回响应到客户端
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
