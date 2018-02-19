package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class UserFeedback extends BaseApi{

	public UserFeedback(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("id") &&parmMap.containsKey("content")){
			if(SqlConnection.getInstance().insertData("user_id,content,create_time", parmMap.get("id")+",'"+parmMap.get("contet")
			+"',"+System.currentTimeMillis(), "feedback")){
				setMessage("反馈成功,感谢您的支持");
				setStatus(SUCCESS);
			}else{
				setMessage("反馈失败");
				setStatus(FAIL);
			}
		}
		else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
