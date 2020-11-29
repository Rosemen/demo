package cn.edu.scau.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author noob
 * @date 2020/11/29 15:27
 * @description
 */
public class EchoChildChannelHandler extends ChannelInitializer {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                .addLast(new StringDecoder()).addLast(new EchoClientHandler());
    }
}
