package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class MessagePut extends BaseApi{
	//消息类型
	public final static String TEXT = "0";//文本
	public final static String VOICE = "1";//语音
	public final static String IMG = "2";//图片

	public MessagePut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("put_id") && parmMap.containsKey("get_id") && parmMap.containsKey("type")
				&&(parmMap.containsKey("content") || parmMap.containsKey("img"))){
			StringBuilder id = new StringBuilder();
			id.append("put_id,get_id,type,put_time");
			StringBuilder data = new StringBuilder();
			data.append(parmMap.get("put_id"));
			data.append(",");
			data.append(parmMap.get("get_id"));
			data.append(",");
			data.append(parmMap.get("type"));
			data.append(",");
			data.append(String.valueOf(System.currentTimeMillis()/1000));
			switch (parmMap.get("type")) {
			case TEXT:
				if (parmMap.containsKey("content")){
					id.append(",content");
					data.append(",'");
					data.append(parmMap.get("content"));
					data.append("'");
				}else{
					setStatus(DATA_FAIL);
					setMessage("访问失败");
				}
				break;
			case VOICE:

				break;
			case IMG:
				if (parmMap.containsKey("img")){
					id.append(",img");
					data.append(",'");
					data.append(parmMap.get("img"));
					data.append("'");
				}else{
					setStatus(DATA_FAIL);
					setMessage("访问失败");
				}
				break;
			}
			if (SqlConnection.getInstance().insertData(id.toString(), data.toString(), "message")){
				setStatus(SUCCESS);
				setMessage("发送成功");
			}else{
				setStatus(FAIL);
				setMessage("发送失败");
			}
			addLog(SqlConnection.getInstance().getLog());
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
