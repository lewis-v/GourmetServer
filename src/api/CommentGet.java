package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class CommentGet extends BaseApi{

	public CommentGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("type") && parmMap.containsKey("id")){
			List<JSONObject> list = SqlConnection.getInstance()
					.search("*", "type = "+parmMap.get("type") +" AND act_id = "+parmMap.get("id"), "comment_all");
			if (list != null && list.size()> 0){
				setStatus(SUCCESS);
				setMessage("获取成功");
				setData(list.toString());
			}else {
				setStatus(FAIL);
				setMessage("获取失败");
			}
			addLog(SqlConnection.getInstance().getLog());
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
