package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class CollectionGet extends BaseApi{

	public CollectionGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("id")){
			String where = " collection_id = "+parmMap.get("id");
			String id = "";
			String from = "collect_all LEFT JOIN comment_status_all ON collect_all.type = comment_status_all.m_type AND collect_all.id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("id");
			if (parmMap.containsKey("time_flag") && parmMap.containsKey("act")){
				if (parmMap.get("act").equals("-1")){
					where = where + " AND put_time < "+parmMap.get("time_flag") ;
				}
			}
			if (parmMap.containsKey("status")){//请求查看指定权限内容的
				if (where.length()>0){
					where = where + " AND status = "+parmMap.get("status");
				}else{
					where = "status = "+parmMap.get("status");
				}
			}else{//正常无法看到被禁止的
				if (where.length()>0){
					where = where + " AND status = 1";
				}else{
					where = "status = 1";
				}
			}
			where = where +" GROUP BY id,type ORDER BY put_time DESC LIMIT 0,10";
			List<JSONObject> list = SqlConnection.getInstance().search("*", where, from);
			setStatus(SUCCESS);
			setMessage("获取成功");
			setData(list.toString());
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;

	}

}
