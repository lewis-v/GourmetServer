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
	private boolean isSuccess = true;//�Ƿ�ɹ�

	public TopPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("act_id")
				&& parmMap.containsKey("type")){

			List<JSONObject> list = SqlConnection.getInstance().search("*", "user_id = "+parmMap.get("id")+"  Order by top_num ", "top");
			if (list != null && list.size()>0){
				if (list.size()> 0 && parmMap.containsKey("top_num") && parmMap.containsKey("top_num2")){
					if (parmMap.get("top_num").equals(parmMap.get("top_num2"))){//ɾ��
						if (!SqlConnection.getInstance().removeData("user_id = "+parmMap.get("id")+" AND top_num = "+parmMap.get("top_num"), "top")){
							isSuccess = false;
						}
						if (!parmMap.get("top_num").equals(String.valueOf(list.size()))){//�������һ��,Ҫ���������ǰ
							for (int top_num = Integer.parseInt(parmMap.get("top_num"))+1;top_num <= list.size();top_num ++){
								if (!SqlConnection.getInstance().setData("top_num", String.valueOf(top_num - 1), "user_id = "+parmMap.get("id")+" AND top_num = "+top_num, "top")){
									isSuccess = false;
									break;
								}
							}
						}
					}else{//��λ��
						if (!SqlConnection.getInstance().setData("top_num", parmMap.get("top_num"), "user_id = "+parmMap.get("id")+" AND top_num = "+parmMap.get("top_num2"), "top")){
							isSuccess = false;
						}
						if (isSuccess){
							if (!SqlConnection.getInstance().setData("top_num",  parmMap.get("top_num2"), "user_id = "+parmMap.get("id")
							+" AND type = "+parmMap.get("type") + " AND act_id = "+parmMap.get("act_id"), "top")){
								isSuccess = false;
							}
						}
						
					}
				}else{
					if (list.size() == 3){//ɾ�����һ��
						if (!SqlConnection.getInstance().removeData("user_id = "+parmMap.get("id") +" AND top_num = 3", "top")){
							isSuccess = false;
						}
						if (isSuccess){
							list.remove(2);
						}
					}
					for (int len = list.size()-1;0 <= len;len --){
						if (!SqlConnection.getInstance().setData("top_num", String.valueOf(len+2), "user_id = "+parmMap.get("id")+" AND top_num = "+(len+1), "top")){
							isSuccess = false;
							break;
						}
					}
					if (isSuccess){
						if (!SqlConnection.getInstance().insertData("user_id,act_id,type,top_num", parmMap.get("id")+","+
								parmMap.get("act_id")+","+parmMap.get("type")+",1", "top")){
							isSuccess = false;
						}
					}
				}
				if (isSuccess){
					setStatus(SUCCESS);
					setMessage("�����ɹ�");
				}else{
					setStatus(FAIL);
					setMessage("����ʧ��");
				}

			}else{
				if (SqlConnection.getInstance().insertData("user_id,act_id,type,top_num", parmMap.get("id")+","+
						parmMap.get("act_id")+","+parmMap.get("type")+",1", "top")){
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
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
