package utils;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import net.sf.json.JSONObject;

public class ServiceResult {
	/**
	 * 获取json的服务器返回
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static FullHttpResponse getJSONResult(String content) throws UnsupportedEncodingException{
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,		
				Unpooled.wrappedBuffer(content.getBytes("UTF-8")));
		response.headers().set(CONTENT_TYPE,Values.APPLICATION_JSON);
		return response;
	}
	
	/**
	 * 获取文件(图片)的服务器返回
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static FullHttpResponse getFileResult(String fileName) throws IOException{
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,		
				Unpooled.wrappedBuffer(FileUtils.toByteArray(fileName)));
		return response;
	}
	
	/**
	 * 获取服务器错误返回
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static FullHttpResponse getERRResult(String content) throws UnsupportedEncodingException{
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND,  	
				Unpooled.wrappedBuffer(content.getBytes("UTF-8")));
		response.headers().set(CONTENT_TYPE,Values.APPLICATION_JSON); 
		return response;
	}
	
	public static FullHttpResponse getUnHandleResult() throws IOException{
		JSONObject js = new JSONObject();
		js.put("status", "1");
		js.put("message", "无此接口调用");
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,  	
				Unpooled.wrappedBuffer(js.toString().getBytes("UTF-8")));
		response.headers().set(CONTENT_TYPE,Values.APPLICATION_JSON); 
		return response;
	}
}
