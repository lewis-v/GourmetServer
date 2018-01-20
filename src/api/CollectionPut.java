package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class CollectionPut extends BaseApi{

	public CollectionPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("id") &&parmMap.containsKey("act_id") && parmMap.containsKey("type")
				&& parmMap.containsKey("act")){//act:1�ղ�,-1ȡ���ղ�
			String where = "act_id = "+parmMap.get("act_id")
			+" AND type = "+parmMap.get("type") +" AND user_id = "+parmMap.get("id");
			List<JSONObject> list = SqlConnection.getInstance().search("*",where , "collection");
			if (parmMap.get("act").equals("1")){
				if (list != null && list.size()>0){
					setStatus(FAIL);
					setMessage("���Ѿ��ղع���");
				}else{
					if (SqlConnection.getInstance().insertData("type,act_id,user_id", parmMap.get("type")
							+","+parmMap.get("act_id")+","+parmMap.get("id"), "collection")){
						setStatus(SUCCESS);
						setMessage("�ղسɹ�");
					}else{
						setStatus(FAIL);
						setMessage("�ղ�ʧ��");
					}
				}
			}else {
				if (list == null || list.size()==0){
					setStatus(FAIL);
					setMessage("ȡ���ղ�ʧ��");
				}else{
					if (SqlConnection.getInstance().removeData(where, "collection")){
						setStatus(SUCCESS);
						setMessage("ȡ���ղسɹ�");
					}else{
						setStatus(FAIL);
						setMessage("ȡ���ղ�ʧ��");
					}
				}
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
