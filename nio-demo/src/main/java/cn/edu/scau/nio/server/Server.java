package cn.edu.scau.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author noob
 * @date 2020/11/8 14:31
 * @description
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int PORT = 12345;

    private static final int BUFFER_SIZE = 1024;

    private Selector selector;

    private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    /**
     * 初始化服务器
     */
    private void init() {
        try {
            //1. 开启Selector
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //2. 设置channel非阻塞 & 绑定端口
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            //3. 将channel注册到selector上, 监听阻塞状态
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("init server success, port: {}", PORT);
        } catch (Exception e) {
            logger.error("init server error, error message: {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据传输处理
     */
    private void handleRead(SelectionKey selectionKey) {
        try{
            buffer.clear();
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            int size = channel.read(buffer);
            // 客户端关闭
            if (size == -1) {
                channel.close();
                selectionKey.cancel();
                return;
            }
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            logger.info("handle read success, data: {}", new String(data));
        }catch (Exception e) {
            logger.error("handle read error, error message: {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接处理
     */
    private void handleAccept(SelectionKey selectionKey) {
        try {
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            // 调用accept, 处理客户端连接
            SocketChannel socketChannel = channel.accept();
            // 设置非阻塞
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            logger.info("handle server accept success");
        } catch (Exception e) {
            logger.error("handle accept error, error message: {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 运行服务器，处理客户端连接与数据传输
     */
    public void run() {
        init();
        try {
            while (true) {
                //1. selector询问channel是否有事件发生，包括连接建立&数据传输
                selector.select();
                //2. 获取注册到selector的channel的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                //3. 遍历事件集合，进行不同处理
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    // 非合法key跳过,不处理
                    if (!selectionKey.isValid()) {
                        continue;
                    }
                    // 处理连接事件
                    if (selectionKey.isAcceptable()) {
                        handleAccept(selectionKey);
                    }
                    // 处理数据传输事件
                    if (selectionKey.isReadable()) {
                        handleRead(selectionKey);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("server run with error, error message: {}", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

}
