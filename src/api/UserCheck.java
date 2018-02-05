package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class UserCheck extends BaseApi{

	public UserCheck(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		List<JSONObject> list;
		if (parmMap.containsKey("accout_number")){
			list = SqlConnection.getInstance().search("*", "accout_number = '"+parmMap.get("accout_number")+"'", "user");
			if (list != null && list.size()>0){
				setMessage("�˺��Ѵ���");
				setStatus(FAIL);
			}else{
				setMessage("�˺�δע��");
				setStatus(SUCCESS);
			}
		}else if(parmMap.containsKey("nickname")){
			list = SqlConnection.getInstance().search("*", "nickname = '"+parmMap.get("nickname")+"'", "user_info");
			if (list != null && list.size()>0){
				setMessage("�ǳ��Ѵ���");
				setStatus(FAIL);
			}else{
				setMessage("�ǳ�δ��ʹ��");
				setStatus(SUCCESS);
			}
		}else{
			setMessage("����ʧ��");
			setStatus(FAIL);
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
