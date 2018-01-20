package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class ShareCommonPut extends BaseApi{

	public ShareCommonPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("status") 
				&& (parmMap.containsKey("content") || parmMap.containsKey("img"))){
			String name = "user_id,put_time,create_time,status,content,img";
			String data = parmMap.get("id")+","+System.currentTimeMillis()+","
			+System.currentTimeMillis()+","+parmMap.get("status")+",'"
					+(parmMap.containsKey("content")?parmMap.get("content"):"")+"','"
					+(parmMap.containsKey("img")?parmMap.get("img"):"")+"'";
			if (SqlConnection.getInstance().insertData(name, data, "common_share")){
				addLog(SqlConnection.getInstance().getLog());
				setStatus(SUCCESS);
				setMessage("发布成功");
			}else {
				setStatus(DATA_FAIL);
				setMessage("访问失败");
			}
		}else{
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
