package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class UserInfoGet extends BaseApi{

	public UserInfoGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("id")){
			List<JSONObject> list = SqlConnection.getInstance().search("*", "user_id = "+parmMap.get("id"), "user_info");
			if (list != null && list.size()>0){
				setStatus(SUCCESS);
				setMessage("获取成功");
				setData(list.get(0).toString());
			}else{
				setStatus(FAIL);
				setMessage("获取失败");
			}
		}else{
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}	
		addLog(SqlConnection.getInstance().getLog());
		addLog(js.toString());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
