package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class FindShare extends BaseApi{

	public FindShare(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("key") && parmMap.containsKey("type")){
			String key = parmMap.get("key");
			String from ="";
			String where = "";
			switch (parmMap.get("type")) {
			case "0"://日记
				from = "diary_all";
				where = "title LIKE '%"+key+"%' OR address LIKE '%"+key+"%' OR content LIKE '%"+key+"%'";
				break;
			case "1"://攻略
				from = "raiders_all";
				where = "title LIKE '%"+key+"%' OR address LIKE '%"+key+"%' OR content LIKE '%"+key+"%' OR introduction LIKE '%"
						+key+"%' OR raiders_type LIKE '%"+key+"%' OR raiders_content LIKE '%"+key+"%'";
				break;
			case "2"://食谱
				from = "menu_all";
				where = "title LIKE '%"+key+"%' OR address LIKE '%"+key+"%' OR content LIKE '%"+key+"%' OR introduction LIKE '%"
						+key+"%' OR practice LIKE '%"+key+"%' OR ingredient LIKE '%"+key+"%' OR tip LIKE '%"+key+"%'";
				break;
			case "3"://普通分享
				from = "common_share_all";
				where = "address LIKE '%"+key+"%' OR content LIKE '%"+key+"%'";
				break;
			case "-1"://找朋友
				from = "user_info";
				where = "nickname LIKE '%"+key+"%'";
				break;
			}
			if (parmMap.containsKey("user_id") && from.length()>0 && !parmMap.get("type").equals("-1")){
				from = from+" LEFT JOIN comment_status_all ON "+from+".type = comment_status_all.m_type AND "+from+".id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("user_id");
			}	
			List<JSONObject> list = SqlConnection.getInstance().search("*", where, from);
			if (list != null && list.size()>0){
				setData(list.toString());
				setStatus(SUCCESS);
				setMessage("获取成功");
			}else{
				setStatus(FAIL);
				if(parmMap.get("type").equals("-1")){
					setMessage("没有相关的朋友哟");
				}else{
					setMessage("没有相关的东西哟");
				}
			}

		}else{
			setStatus(FAIL);
			setMessage("访问失败");
		}
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
