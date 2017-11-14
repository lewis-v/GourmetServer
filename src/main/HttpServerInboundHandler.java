package main;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;  
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;  
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;  
import static io.netty.handler.codec.http.HttpResponseStatus.OK;  
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;  
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Login;
import io.netty.buffer.ByteBuf;  
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;  
import io.netty.handler.codec.http.HttpContent;  
import io.netty.handler.codec.http.HttpHeaders;  
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import net.sf.json.JSONObject;
import utils.FileUtils;
import utils.ServiceResult;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {  
	private String Log = "----------start----------\n";

	@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { 
		HttpRequest request;
		String uri = "";//调用的接口
		FullHttpResponse response = null;
		request = (HttpRequest)msg;
		uri = request.getUri();
		Log = Log +uri +"\n";
		//信息处理
		FullHttpRequest fullReq = (FullHttpRequest)msg;
		Map<String, String> parmMap = new HashMap<>();
		Map<String, File> parmMapFile = new HashMap<>();
		HttpMethod method = fullReq.method();
		if (HttpMethod.GET == method) {
			// 是GET请求
			Log = Log + "GET:\n";
			QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
			decoder.parameters().entrySet().forEach( entry -> {
				// entry.getValue()是一个List, 只取第一个元素
				parmMap.put(entry.getKey(), entry.getValue().get(0));
				Log = Log + entry.getKey()+":"+entry.getValue().get(0)+"\n";
			});
		} else if (HttpMethod.POST == method) {
			// 是POST请求
			Log = Log + "POST:\n";
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
			decoder.offer(fullReq);
			List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
			for (InterfaceHttpData parm : parmList) {
				if (parm instanceof Attribute) {
					Attribute data = (Attribute) parm;
					parmMap.put(data.getName(), data.getValue());
					Log = Log + data.getName()+":"+data.getValue()+"\n";
				}else if(parm instanceof MixedFileUpload) { 
					MixedFileUpload upload = (MixedFileUpload)parm;
					parmMapFile.put(upload.getName(), upload.getFile());
					File file = new File("file/UP"+System.currentTimeMillis()+upload.getFilename().substring(upload.getFilename().lastIndexOf(".")));
					FileUtils.copyFile(upload.getFile(), file);
					Log = Log + file.getAbsolutePath()+"\n";
				} else {
					//未知类型,尝试转换来查看其类型提示
					MixedFileUpload upload = (MixedFileUpload)parm;
					parmMapFile.put(upload.getName(), upload.getFile());
					Log = Log + upload.getName()+":"+upload.getFile()+"\n";
				}
			}
			if (uri.startsWith("/Login")){
				Login login = new Login(parmMap);
				response = login.getResponse();
				Log = Log + login.getLog();
			}

		}

		//		File file = new File("IMG_0784.JPG");
		//		FullHttpResponse response = ServiceResult.getFileResult(file.getName());
		if (response ==null){//对未处理的接口进行"无接口"处理
			response = ServiceResult.getUnHandleResult();
		}
		if (HttpHeaders.isKeepAlive(request)) {  
			response.headers().set(CONNECTION, Values.KEEP_ALIVE);  
		}
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes()); 

		Log = Log +response.toString()+"\n";
		ctx.write(response);  
		ctx.flush();  
	}  

	@Override  
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
//		ctx.flush();  
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

		System.out.println(Log+"----------end----------\n");
		Log = "----------start----------\n";
	}  

	/**
	 * 服务器出错处理
	 */
	@Override  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
		System.out.println("ERR:"+cause.getMessage());  
		JSONObject js = new JSONObject();
		js.put("status", "-1");
		js.put("message", cause.getLocalizedMessage());
		js.put("data", "");
		FullHttpResponse response;
		try {
			response = ServiceResult.getERRResult(js.toString());
			Log = Log +response.toString()+"\n";
			ctx.write(response);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctx.flush();   
		System.out.println(Log+"----------end----------\n");
		Log = "----------start----------\n";
	}  
}  
