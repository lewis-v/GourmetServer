package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;
import utils.TimeUtils;

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
			data.append(String.valueOf(System.currentTimeMillis()));
			if (SqlConnection.getInstance().insertData(id, data.toString(), "comment")){
				List<JSONObject> list = SqlConnection.getInstance()
						.search("*", "type = "+parmMap.get("type") +" AND act_id = "+parmMap.get("act_id"), "comment_all");
				for (JSONObject json : list){//将create_time转换成正常显示的时间
					if (json.containsKey("create_time")) {
						String create_time = TimeUtils.getTime(json.get("create_time").toString());
						json.remove("create_time");
						json.put("create_time", create_time);
					}
				}
				if (list != null && list.size()> 0){
					setStatus(SUCCESS);
					setMessage("评论成功");
					setData(list.toString());
				}else {
					setStatus(FAIL);
					setMessage("获取失败");
				}
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
