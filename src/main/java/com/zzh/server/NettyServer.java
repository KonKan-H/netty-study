package com.zzh.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import sun.rmi.runtime.Log;

/**
 * @author zzh
 * @version 1.0
 * @date 2019/12/18 17:00
 */
public class NettyServer {

    private static final int BEGIN_PORT = 1000;

    public static void main(String[] args) {
        //引导类 引导服务端的启动工作
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                .group(boss, worker)
                //引导服务端的IO模型为NIO
                .channel(NioServerSocketChannel.class)
                //定义后续每条连接的数据读写
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                });
//        serverBootstrap.bind(8000).addListener(future -> {
//            if(future.isSuccess()) {
//                System.out.println("端口绑定成功");
//            } else{
//                System.out.println("端口绑定失败");
//
//            }
//        });
        bind(serverBootstrap, BEGIN_PORT);
    }


    private static void bind(final ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isSuccess()) {
                    System.out.println("端口" + port + "端口绑定成功");
                } else {
                    System.out.println("端口" + port + "端口绑定失败");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }
}
