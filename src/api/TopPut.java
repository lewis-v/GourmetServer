package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class TopPut extends BaseApi{

	public TopPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("act_id")
				&& parmMap.containsKey("type") && parmMap.containsKey("top_num")){
			List<JSONObject> list = SqlConnection.getInstance().search("*", "user_id = "+parmMap.get("id")+
					" AND top_num = "+parmMap.get("top_num"), "top");
			if (list != null && list.size()>0){
				if(SqlConnection.getInstance().setData("act_id,type", parmMap.get("act_id")+","+parmMap.get("type")
				,"user_id = "+parmMap.get("id")+" AND top_num = "+parmMap.get("top_num") , "top")){
					setStatus(SUCCESS);
					setMessage("�����ɹ�");
				}else{
					setStatus(FAIL);
					setMessage("����ʧ��");
				}
			}else{
				if (SqlConnection.getInstance().insertData("user_id,act_id,type,top_num", parmMap.get("id")+","+
						parmMap.get("act_id")+","+parmMap.get("type")+","+parmMap.get("top_num"), "top")){
					setStatus(SUCCESS);
					setMessage("�����ɹ�");
				}else{
					setStatus(FAIL);
					setMessage("����ʧ��");
				}
			}
		}else{
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}	
		addLog(js.toString());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
