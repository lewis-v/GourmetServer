package api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.MD5;
import utils.ServiceResult;

public class Login extends BaseApi{

	public Login(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("token") && parmMap.get("token").trim().length() != 0){//token��½
			List<JSONObject> list = SqlConnection.getInstance()
					.search("id", "token = \'"+parmMap.get("token")+"\' ", "user");
			if (list.size() > 0){
				setStatus(SUCCESS).setMessage("��½�ɹ�");
				list = SqlConnection.getInstance()
						.search("*", "user_id = "+list.get(0).getString("id"), "user_info_all");
				if (list.size() > 0){
					String token = MD5.md5(System.nanoTime()+"").toUpperCase();
					if (list.get(0).containsKey("token")){
						list.get(0).replace("token", token);
					}else{
						list.get(0).put("token", token);
					}
					SqlConnection.getInstance().setData("token", "\'"+token+"\'"
							, "id = "+list.get(0).getString("user_id"), "user");
					setData(list.get(0).toString());
				}else{
					setMessage("��¼ʧ��");
					setStatus(FAIL);
				}
			}else {//token��½���ɹ�
				setStatus(DATA_FAIL).setMessage("token������");
			}
			addLog(SqlConnection.getInstance().getLog());
		}else{//��token��½
			if(!parmMap.containsKey("id") || parmMap.get("id").trim().length() == 0){//���˺�
				setStatus(DATA_FAIL).setMessage("�������˺�");
			}else if(!parmMap.containsKey("password") || parmMap.get("password").length() == 0){//������
				setStatus(DATA_FAIL).setMessage("����������");
			}else{//�˺������¼����
				List<JSONObject> list = SqlConnection.getInstance()
						.search("id", "accout_number = \'"+parmMap.get("id")+"\' "
								+ "AND password = \'"+parmMap.get("password")+"\'", "user");
				if (list.size()>0){
					setStatus(SUCCESS).setMessage("��¼�ɹ�");
					list = SqlConnection.getInstance()
							.search("*", "user_id = "+list.get(0).getString("id"), "user_info_all");
					if (list.size() > 0){
						String token = MD5.md5(System.nanoTime()+"").toUpperCase();
						if (list.get(0).containsKey("token")){
							list.get(0).replace("token", token);
						}else{
							list.get(0).put("token", token);
						}
						
						SqlConnection.getInstance().setData("token", "\'"+token+"\'"
								, "id = "+list.get(0).getString("user_id"), "user");
						setData(list.get(0).toString());
					}else{
						setMessage("��¼ʧ��");
						setStatus(FAIL);
					}
				}else{
					setStatus(DATA_FAIL).setMessage("�˺Ż��������");
				}
				addLog(SqlConnection.getInstance().getLog());
			}
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}


}
