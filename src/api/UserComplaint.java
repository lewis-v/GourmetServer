package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class UserComplaint extends BaseApi{

	public UserComplaint(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
	if(parmMap.containsKey("user_id") && parmMap.containsKey("act_id") && parmMap.containsKey("type") 
			&& parmMap.containsKey("content")){
		if(SqlConnection.getInstance().insertData("user_id,act_id,type,content,time"
				, parmMap.get("user_id") + ","+parmMap.get("act_id")+","+parmMap.get("type")+",'"+parmMap.get("content")+"',"+System.currentTimeMillis(), "complaint")){
			setStatus(SUCCESS);
			setMessage("投诉成功,谢谢您对我们工作的支持");
		}else{
			setStatus(FAIL);
			setMessage("投诉失败");
		}
	}else {
		setStatus(DATA_FAIL);
		setMessage("访问失败");
	}
	response = ServiceResult.getJSONResult(js.toString());
	return response;
	}

}
