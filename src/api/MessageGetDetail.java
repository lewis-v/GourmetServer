package api;

import java.io.IOException;
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
			String put_id = parmMap.get("put_id");
			String get_id = parmMap.get("get_id");
			String from = "message_detail";
			String where = "((put_id = " + put_id + " AND get_id = " + get_id 
					+") OR (get_id = " + put_id + " AND put_id = "+get_id+"))";
			if(parmMap.containsKey("type") && parmMap.containsKey("start_id")){
				if (parmMap.get("type").equals("new")){//��������Ϣ
					where = where + " AND id > "+parmMap.get("start_id");
				}else if(parmMap.get("type").equals("history")){//������ʷ��Ϣ
					where = where + " AND id < "+parmMap.get("start_id");
					from = from + "LIMIT 20";
				}
			}else {//Ĭ��ֻ�᷵�������100��,��������Ϣϵͳ�ᶨ������
				from = from + "LIMIT 100";
			}
			List<JSONObject> list = SqlConnection.getInstance().search("*", where, from);
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
