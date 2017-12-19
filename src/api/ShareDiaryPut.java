package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class ShareDiaryPut extends BaseApi{

	public ShareDiaryPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("time") && parmMap.containsKey("create_time")
				&& parmMap.containsKey("title") && parmMap.containsKey("content") && parmMap.containsKey("status")){
			StringBuffer setId = new StringBuffer();
			StringBuffer setData = new StringBuffer();
			setId.append("user_id,time,create_time,title,content,put_time,status");
			setData.append(parmMap.get("id"));
			setData.append(",");
			setData.append(parmMap.get("time"));
			setData.append(",");
			setData.append(parmMap.get("create_time"));
			setData.append(",'");
			setData.append(parmMap.get("title"));
			setData.append("','");
			setData.append(parmMap.get("content"));
			setData.append("',");
			setData.append(System.currentTimeMillis()/1000);
			setData.append(",");
			setData.append(parmMap.get("status"));
			if (parmMap.containsKey("cover")){//封面
				setId.append(",cover");
				setData.append(",'");
				setData.append(parmMap.get("cover"));
				setData.append("'");
			}
			if (parmMap.containsKey("content_back")){//内容背景
				setId.append(",content_back");
				setData.append(",'");
				setData.append(parmMap.get("content_back"));
				setData.append("'");
			}
			if (parmMap.containsKey("address")){
				setId.append(",address");
				setData.append(",'");
				setData.append(parmMap.get("address"));
				setData.append("'");
			}
			if (parmMap.containsKey("lat") && parmMap.containsKey("lng")){
				setId.append(",lat,lng");
				setData.append(",");
				setData.append(parmMap.get("lat"));
				setData.append(",");
				setData.append(parmMap.get("lng"));
			}
			if (SqlConnection.getInstance().insertData(setId.toString(), setData.toString(), "diary")){
				setStatus(SUCCESS);
				setMessage("发布成功");
			}else{
				setStatus(FAIL);
				setMessage("发布失败");
			}
			addLog(SqlConnection.getInstance().getLog());
			
		}else{
			setStatus(DATA_FAIL);
			setMessage("缺少参数");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
