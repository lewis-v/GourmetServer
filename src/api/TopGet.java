package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class TopGet extends BaseApi{

	public TopGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id")){
			List<JSONObject> list = SqlConnection.getInstance().search("*", "user_id = "+parmMap.get("id") + " GROUP BY type,id,top_num Order by top_num ", "top_all  LEFT JOIN comment_status_all ON top_all.type = comment_status_all.m_type AND top_all.id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("id"));
			if (list != null){
				setStatus(SUCCESS);
				setMessage("获取成功");
				setData(list.toString());
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
