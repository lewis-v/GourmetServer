package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class MessageGetDetail extends BaseApi{

	public MessageGetDetail(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("put_id") && parmMap.containsKey("get_id")){
			boolean isOrder = false;//�Ƿ���Ҫ��������
			String put_id = parmMap.get("put_id");
			String get_id = parmMap.get("get_id");
			String from = "message_detail";
			String where = "((put_id = " + put_id + " AND get_id = " + get_id 
					+") OR (get_id = " + put_id + " AND put_id = "+get_id+"))";
			if(parmMap.containsKey("type") && parmMap.containsKey("start_id")){
				if (parmMap.get("type").equals("new")){//��������Ϣ
					where = where + " AND id > "+parmMap.get("start_id");
				}else if(parmMap.get("type").equals("history")){//������ʷ��Ϣ
					where = where + " AND id < "+parmMap.get("start_id") + " LIMIT 20";
				}
			}else {//Ĭ��ֻ�᷵�������50��,��������Ϣϵͳ�ᶨ������
				List<JSONObject> unRead = SqlConnection.getInstance().search("*", "get_id = " + put_id + " AND put_id = " + get_id +" AND is_read = 1", from);
				if (unRead != null && unRead.size()>0){
					String time = unRead.get(0).get("time_flag").toString();
					where = where+" AND put_time >= " +time;
				}else{
					isOrder = true;
					where = where + " ORDER BY put_time DESC LIMIT 50";
				}
			}
			List<JSONObject> list = SqlConnection.getInstance().search("*", where, from);
			if (isOrder){
				List<JSONObject> cache = new ArrayList<>();
				for(int len = list.size()-1;len>=0;len--){
					cache.add(list.get(len));
				}
				list = cache;
			}
			addLog(SqlConnection.getInstance().getLog());
			setStatus(SUCCESS);
			setMessage("success");
			setData(list.toString());
			addLog(list.toString());
		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
