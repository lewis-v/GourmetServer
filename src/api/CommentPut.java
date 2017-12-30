package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class CommentPut extends BaseApi{


	public CommentPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("type") && parmMap.containsKey("act_id")
				&& parmMap.containsKey("user_id") && parmMap.containsKey("content")){
			String id = "act_id,user_id,type,content,create_time";
			StringBuilder data = new StringBuilder();
			data.append(parmMap.get("act_id"));
			data.append(",");
			data.append(parmMap.get("user_id"));
			data.append(",");
			data.append(parmMap.get("type"));
			data.append(",'");
			data.append(parmMap.get("content"));
			data.append("',");
			data.append(String.valueOf(System.currentTimeMillis()/1000));
			if (SqlConnection.getInstance().insertData(id, data.toString(), "comment")){
				setStatus(SUCCESS);
				setMessage("评论成功");
			}else {
				setStatus(FAIL);
				setMessage("评论失败");
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
