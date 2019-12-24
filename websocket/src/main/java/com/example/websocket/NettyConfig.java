package com.example.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Slf4j
@Configuration
public class NettyConfig {

    @Value("${netty.bossGroup}")
    private Integer bossGroupNum;

    @Value("${netty.workGroup}")
    private Integer workGroupNum;

    @Value("${netty.ChannelOption.SO_BACKLOG}")
    private Integer backlogNum;

    @Value("${netty.port}")
    private Integer port;

    @Autowired
    private  ServerChannelInitializer serverChannelInitializer;

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossGroupNum);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workGroup() {
        return new NioEventLoopGroup(workGroupNum);
    }

    @Bean(name = "socketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(port);
    }


    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap(){
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup(), workGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(serverChannelInitializer)
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, backlogNum)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return bootstrap;
    }

}


