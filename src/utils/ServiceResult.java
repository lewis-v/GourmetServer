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
	 * ��ȡjson�ķ���������
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
	 * ��ȡ�ļ�(ͼƬ)�ķ���������
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
	 * ��ȡ���������󷵻�
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
		js.put("message", "�޴˽ӿڵ���");
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,  	
				Unpooled.wrappedBuffer(js.toString().getBytes("UTF-8")));
		response.headers().set(CONTENT_TYPE,Values.APPLICATION_JSON); 
		return response;
	}
}
