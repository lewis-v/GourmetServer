package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class ReMarkPutData extends BaseApi{

	public ReMarkPutData(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("act_id") && parmMap.containsKey("type") 
				&& parmMap.containsKey("act")){
			if (SqlConnection.getInstance().search("id"
					, "user_id = "+parmMap.get("id") +" AND act_id = "+parmMap.get("act_id")
					+ " AND type = "+parmMap.get("type")
					, "remark_info").size() == 0){
				if (SqlConnection.getInstance().insertData("user_id,act_id,act,create_time,type"
						, parmMap.get("id")
						+","+parmMap.get("act_id")
						+","+parmMap.get("act")
						+","+System.currentTimeMillis()/1000
						+","+parmMap.get("type")
						, "remark_info")){
					addLog(SqlConnection.getInstance().getLog());
					List<JSONObject> list = SqlConnection.getInstance().search("*"
							, "id = "+parmMap.get("act_id") +" AND type = " +parmMap.get("type")
							, "share_list_all");
					if ( list.size() > 0 ){
						setStatus(SUCCESS);
						setMessage("�����ɹ�");
						setData(list.get(0).toString());
					}else {
						setStatus(DATA_FAIL);
						setMessage("����ʧ��");
					}
				}else {
					setStatus(DATA_FAIL);
					setMessage("����ʧ��");
				}
				addLog(SqlConnection.getInstance().getLog());
			}else {
				setStatus(DATA_FAIL);
				setMessage("�������۹���~~");
			}
		}else {//ȱ�ٲ���
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
				addLog(js.toString());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
