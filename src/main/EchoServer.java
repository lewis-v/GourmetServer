package main;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

public class EchoServer {
	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(group) // 绑定线程池
			.channel(NioServerSocketChannel.class) // 指定使用的channel
			.localAddress(this.port)// 绑定监听端口
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					System.out.println("connected...; Client:" + ch.remoteAddress());
					ch.pipeline()
					// server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码  
//					.addLast("decoder",new HttpServerCodec())
					 .addLast(new HttpRequestDecoder())
			        .addLast(new HttpResponseEncoder())
					//内容压缩
					.addLast(new HttpContentCompressor())
//					.addLast("aggregator",new HttpObjectAggregator(66356))
//					.addLast("chunked",new ChunkedWriteHandler())
//					.addLast(new FileServerHandler())
					.addLast("serverHandler", new HttpUploadServerHandler());  
				}
			})
//			.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
			;
			ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
			System.out.println(EchoServer.class + " started and listen on " + cf.channel().localAddress());
			cf.channel().closeFuture().sync(); // 关闭服务器通道
		} finally {
			group.shutdownGracefully().sync(); // 释放线程池资源
		}
	}

	public static void main(String[] args) throws Exception {
		new EchoServer(47423).start(); // 启动
	}
}