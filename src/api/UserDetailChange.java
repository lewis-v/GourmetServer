package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class UserDetailChange extends BaseApi{

	public UserDetailChange(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id")){
			String id = parmMap.get("id");
			if (setUserData(id, "img_header")&&//ͷ��
					setUserData(id, "personal_back")&&//�������ı���
					setUserData(id, "sex")&&//�Ա�
					setUserData(id, "address")&&//����
					setUserData(id, "nickname")&&//�ǳ�
					setUserData(id, "introduction")//���
					)
			{ 
				List<JSONObject> list = 
						SqlConnection.getInstance().search("*", "user_id = "+id, "user_info_all");
				addLog(SqlConnection.getInstance().getLog());
				if (list.size() > 0){
					setStatus(SUCCESS);
					setMessage("�޸ĳɹ�");
					setData(list.get(0).toString());
				}else {
					setStatus(DATA_FAIL);
					setMessage("����ʧ��");
				}
			}
		}else{
			setStatus(DATA_FAIL);
			setMessage("�޸�ʧ��");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

	/**
	 * �޸ĸ�������
	 * @param id �û�id
	 * @param key ������
	 */
	public boolean setUserData(String id,String key){
		if (parmMap.containsKey(key)){//ͷ��
			if (!SqlConnection.getInstance().setData(key
					, "\'"+parmMap.get(key)+"\'", "user_id = "+id,"user_info")){
				return false;
			}
		}
		return true;
	}
}
