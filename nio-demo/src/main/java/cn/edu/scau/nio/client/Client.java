package cn.edu.scau.nio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author noob
 * @date 2020/11/8 16:08
 * @description
 */
public class Client {

    private static final int SERVER_PORT = 12345;

    private static final String SERVER_HOST = "127.0.0.1";

    private static final int BUFFER_SIZE = 1024;

    private static ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            logger.info("start client success");
            byte[] data = null;
            while (true) {
                data = new byte[BUFFER_SIZE];
                System.in.read(data);
                byteBuffer.put(data);
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (Exception e) {
            logger.error("start client error, error message: {}", e);
            throw new RuntimeException(e);
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                logger.error("close client error, error message: {}", e);
                throw new RuntimeException(e);
            }
        }

    }
}
