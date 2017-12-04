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
			sb.group(group) // ���̳߳�
			.channel(NioServerSocketChannel.class) // ָ��ʹ�õ�channel
			.localAddress(this.port)// �󶨼����˿�
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() { // �󶨿ͻ�������ʱ�򴥷�����

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					System.out.println("connected...; Client:" + ch.remoteAddress());
					ch.pipeline()
					// server�˽��յ�����httpRequest������Ҫʹ��HttpRequestDecoder���н���  
//					.addLast("decoder",new HttpServerCodec())
					 .addLast(new HttpRequestDecoder())
			        .addLast(new HttpResponseEncoder())
					//����ѹ��
					.addLast(new HttpContentCompressor())
//					.addLast("aggregator",new HttpObjectAggregator(66356))
//					.addLast("chunked",new ChunkedWriteHandler())
//					.addLast(new FileServerHandler())
					.addLast("serverHandler", new HttpUploadServerHandler());  
				}
			})
//			.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
			;
			ChannelFuture cf = sb.bind().sync(); // �������첽������
			System.out.println(EchoServer.class + " started and listen on " + cf.channel().localAddress());
			cf.channel().closeFuture().sync(); // �رշ�����ͨ��
		} finally {
			group.shutdownGracefully().sync(); // �ͷ��̳߳���Դ
		}
	}

	public static void main(String[] args) throws Exception {
		new EchoServer(47423).start(); // ����
	}
}