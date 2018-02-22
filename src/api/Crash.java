package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class Crash extends BaseApi{

	public Crash(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("time") && parmMap.containsKey("content")){
			String id = "time,content";
			String value = parmMap.get("time")+",\""+parmMap.get("content")+"\"";
			if (parmMap.containsKey("id")){
				id = id + ",user_id";
				value = value + "," + parmMap.get("id");
			}
			if (SqlConnection.getInstance().insertData(id, value, "crash")){
				setStatus(SUCCESS);
				setMessage("程序出了点小问题,已告知程序员了哟~");
			}else{
				setStatus(FAIL);
				setMessage("这是什么神操作");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
