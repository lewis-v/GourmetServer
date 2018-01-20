package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class ShareListLoad extends BaseApi{
	public ShareListLoad(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		String where = "";
		String id = "*";
		String from = "share_list_all";
		if (parmMap.containsKey("type") && parmMap.containsKey("id")){//��ȡָ��������Ϣ
			where = " type = "+parmMap.get("type")+" AND  user_id = "+parmMap.get("id");
		}else if (parmMap.containsKey("type")){//��ȡָ������
			where = " type = "+parmMap.get("type");
		}else if (parmMap.containsKey("id")){//ָ��id
			where = " user_id = "+parmMap.get("id");
		}
		if(parmMap.containsKey("time_flag") && parmMap.containsKey("act")){//act�О�,1������,-1������
			if (where.length() > 1){
				where = where+" AND ";
			}
			if (parmMap.get("act").equals("1")){//�ϸ���
				where = where + "put_time > " + parmMap.get("time_flag");
			}else{//�¼��d
				where = where + "put_time < " + parmMap.get("time_flag");
				where = where + " LIMIT 0,10";
			}
		}else{
			if (where.length()==0){
				from = from + " LIMIT 0,10";
			}else{
				where = where + " LIMIT 0,10";
			}
		}
		List<JSONObject> list = SqlConnection.getInstance().search(id, where, from);
		addLog(SqlConnection.getInstance().getLog());
		addLog(list.toString());
		setStatus(SUCCESS);
		setMessage("���سɹ�");
		setData(list.toString());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
