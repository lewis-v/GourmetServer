package base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;

public abstract class BaseApi {
	public static final int SUCCESS = 0;//�ɹ�
	public static final int FAIL = -1;//��������
	public static final int RELOGIN = 2;//���µ�¼
	public static final int DATA_FAIL = 1;//�������ݴ���

	protected JSONObject js = new JSONObject();
	protected FullHttpResponse response = null;;

	public abstract FullHttpResponse getResponse() throws IOException;//�Խӿ����ݵĴ������ش���ķ��������ؽ��

	/**
	 * ���÷��ص�״ֵ̬
	 * @param statusType
	 * @return
	 */
	public BaseApi setStatus(int statusType){
		js.put("status", statusType);
		return this;
	}
	
	/**
	 * ���÷��ص�message��Ϣ
	 * @param message
	 * @return
	 */
	public BaseApi setMessage(String message){
		js.put("message", message);
		return this;
	}
	/**
	 * ���÷��ص�data
	 * @param data
	 * @return
	 */
	public BaseApi setData(String data){
		js.put("data", data);
		return this;
	}
}
