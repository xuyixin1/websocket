package com.example.websocket;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author
 *
 * netty服务初始化器
 **/
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private NioWebSocketHandler nioWebSocketHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(3,3,3,TimeUnit.MINUTES));
        //设置解码器
        pipeline.addLast("http-codec",new HttpServerCodec());
        //聚合器，使用websocket会用到
        pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
        //用于大数据的分区传输
        pipeline.addLast("http-chunked",new ChunkedWriteHandler());
        //自定义的业务handler
        pipeline.addLast("handler",nioWebSocketHandler);
    }
}

