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
import utils.ServiceResult;

public class Login extends BaseApi{
	Map<String, String> parmMap = new HashMap<>();

	public Login(Map<String, String> parmMap){
		this.parmMap = parmMap;
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
			if(!parmMap.containsKey("id")){//���˺�
				setStatus(DATA_FAIL).setMessage("�������˺�");
				response = ServiceResult.getJSONResult(js.toString());
			}else if(!parmMap.containsKey("password")){//������
				setStatus(DATA_FAIL).setMessage("����������");
				response = ServiceResult.getJSONResult(js.toString());
			}else{//��¼����
				List<JSONObject> list = SqlConnection.getInstance()
						.search("id", "password = \'"+parmMap.get("password")+"\' && "
				+"accout_number = \'"+parmMap.get("id")+"\'", "user");
				System.out.println(list.toString());
				setStatus(SUCCESS).setMessage("��¼�ɹ�");
				response = ServiceResult.getJSONResult(js.toString());
			}
		return response;

	}


}
