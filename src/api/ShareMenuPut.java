package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class ShareMenuPut extends BaseApi{

	public ShareMenuPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("status")
				&& parmMap.containsKey("title") && parmMap.containsKey("cover")
				&& parmMap.containsKey("difficult_level") && parmMap.containsKey("play_time")
				&& parmMap.containsKey("introduction") && parmMap.containsKey("practice")
				&& parmMap.containsKey("create_time")){
			StringBuilder name = new StringBuilder();
			StringBuilder data = new StringBuilder();
			name.append("user_id,put_time,status,title,cover,difficult_level,play_time,introduction,practice,create_time");
			data.append(parmMap.get("id"));
			data.append(",");
			data.append(System.currentTimeMillis()/1000);
			data.append(",");
			data.append(parmMap.get("status"));
			data.append(",'");
			data.append(parmMap.get("title"));
			data.append("','");
			data.append(parmMap.get("cover"));
			data.append("',");
			data.append(parmMap.get("difficult_level"));
			data.append(",'");
			data.append(parmMap.get("play_time"));
			data.append("','");
			data.append(parmMap.get("introduction"));
			data.append("','");
			data.append(parmMap.get("practice"));
			data.append("',");
			data.append(parmMap.get("create_time"));
			if (parmMap.containsKey("ingredient")){
				name.append(",ingredient");
				data.append(",'");
				data.append(parmMap.get("ingredient"));
				data.append("'");
			}
			if (parmMap.containsKey("tip")){
				name.append(",tip");
				data.append(",'");
				data.append(parmMap.get("tip"));
				data.append("'");
			}
			if (parmMap.containsKey("address")){
				name.append(",address");
				data.append(",'");
				data.append(parmMap.get("address"));
				data.append("'");
			}
			if (SqlConnection.getInstance().insertData(name.toString(), data.toString(), "menu")){
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
