package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class MessageGetList extends BaseApi{
	public MessageGetList(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id")){
			String id = parmMap.get("id");
			List<JSONObject> list = SqlConnection.getInstance()
					.search("id,list.put_id,list.get_id,content,put_time,is_read,un_read_num", "list.get_id = " + id 
							+" or list.put_id = " + id +" order by put_time", "message_list AS list LEFT JOIN (SELECT COUNT(message.is_read) AS un_read_num, message.get_id,message.put_id FROM message WHERE message.is_read = 1 AND message.get_id = "+parmMap.get("id")+" GROUP BY message.put_id ) AS msg ON list.get_id = msg.get_id AND list.put_id = msg.put_id OR list.get_id = msg.put_id AND list.put_id = msg.get_id");
			addLog(SqlConnection.getInstance().getLog());
			if (list != null && list.size() > 0){
				for (int i = 0;i < list.size();i++){
					JSONObject jsonObject = list.get(i);
					if (jsonObject.containsKey("list.put_id") && jsonObject.containsKey("list.get_id")){
						jsonObject.put("put_id", jsonObject.get("list.put_id"));
						jsonObject.put("get_id", jsonObject.get("list.get_id"));
						List<JSONObject> jList = SqlConnection.getInstance()
								.search("*", "user_id = " 
										+ (id.equals(jsonObject.getString("put_id"))?
												jsonObject.getString("get_id"):jsonObject.getString("put_id")), "user_info");
						
						if (jList != null && jList.size()>0){
							if (jList.get(0).containsKey("img_header")){
								list.get(i).put("img_header", jList.get(0).getString("img_header"));
							}
							if (jList.get(0).containsKey("nickname")){
								list.get(i).put("nickname", jList.get(0).getString("nickname"));
							}
						}
					}
				}
				setStatus(SUCCESS);
				setMessage("获取成功");
				setData(list.toString());
				addLog(js.toString());
				response = ServiceResult.getJSONResult(js.toString());
				return response;
			}else{
				setStatus(SUCCESS);
				setMessage("获取成功");
				addLog(js.toString());
				setData(list.toString());
				response = ServiceResult.getJSONResult(js.toString());
				return response;
			}

		}
		setStatus(DATA_FAIL);
		setMessage("访问失败");
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
