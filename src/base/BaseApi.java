package base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;

public abstract class BaseApi {
	public static final int SUCCESS = 0;//成功
	public static final int FAIL = -1;//发生错误
	public static final int RELOGIN = 2;//重新登录
	public static final int DATA_FAIL = 1;//传入数据错误

	protected JSONObject js = new JSONObject();
	protected FullHttpResponse response = null;;

	public abstract FullHttpResponse getResponse() throws IOException;//对接口数据的处理并返回处理的服务器返回结果

	/**
	 * 设置返回的状态值
	 * @param statusType
	 * @return
	 */
	public BaseApi setStatus(int statusType){
		js.put("status", statusType);
		return this;
	}
	
	/**
	 * 设置返回的message信息
	 * @param message
	 * @return
	 */
	public BaseApi setMessage(String message){
		js.put("message", message);
		return this;
	}
	/**
	 * 设置返回的data
	 * @param data
	 * @return
	 */
	public BaseApi setData(String data){
		js.put("data", data);
		return this;
	}
}
