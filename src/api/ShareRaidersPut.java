package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class ShareRaidersPut extends BaseApi{

	public ShareRaidersPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("title") && parmMap.containsKey("status")
				&& parmMap.containsKey("raiders_type") && parmMap.containsKey("introduction")
				&& parmMap.containsKey("raiders_content") && parmMap.containsKey("cover")){
			String name = "user_id,title,status,raiders_type,introduction,raiders_content,cover,create_time,type,put_time";
			String data = parmMap.get("id")+",'"+parmMap.get("title")+"',"+parmMap.get("status")+",'"+parmMap.get("raiders_type")
			+"','"+parmMap.get("introduction")+"','"+parmMap.get("raiders_content")+"','"+parmMap.get("cover")+"',"
			+String.valueOf(System.currentTimeMillis()/1000)+",1,"+String.valueOf(System.currentTimeMillis()/1000);
			if (SqlConnection.getInstance().insertData(name, data, "raiders")){
				setStatus(SUCCESS);
				setMessage("发布成功");
			}else{
				setStatus(FAIL);
				setMessage("发布失败");
			}
			addLog(SqlConnection.getInstance().getLog());
		}else {
			setStatus(DATA_FAIL);
			setMessage("缺少参数");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
