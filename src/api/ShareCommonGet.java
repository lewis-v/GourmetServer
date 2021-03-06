package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class ShareCommonGet extends BaseApi{

	public ShareCommonGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id")){
			List<JSONObject> list ;
			if (parmMap.containsKey("user_id")){
				list = SqlConnection.getInstance().search("*", "id = "+parmMap.get("id"), "common_share_all LEFT JOIN comment_status_all ON common_share_all.type = comment_status_all.m_type AND common_share_all.id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("user_id"));
			}else{
				list = SqlConnection.getInstance().search("*", "id = "+parmMap.get("id"), "common_share_all");
			}
			if (list != null && list.size()>0){
				setStatus(SUCCESS);
				setMessage("获取成功");
				setData(list.get(0).toString());
			}else {
				setStatus(FAIL);
				setMessage("获取失败");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
