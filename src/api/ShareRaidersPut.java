package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class ShareRaidersPut extends BaseApi{

	public ShareRaidersPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("title") && parmMap.containsKey("status")
				&& parmMap.containsKey("raiders_type") && parmMap.containsKey("introduction")
				&& parmMap.containsKey("content") && parmMap.containsKey("cover")){
			String name = "user_id,title,status,raiders_type,introduction,content,cover,create_time,type";
			String data = parmMap.get("id")+",'"+parmMap.get("title")+"',"+parmMap.get("status")+",'"+parmMap.get("raiders_type")
			+"','"+parmMap.get("intruduction")+"','"+parmMap.get("content")+"','"+parmMap.get("cover")+"',"
			+String.valueOf(System.currentTimeMillis()/1000)+",1";
			if (SqlConnection.getInstance().insertData(name, data, "raiders")){
				setStatus(SUCCESS);
				setMessage("�����ɹ�");
			}else{
				setStatus(FAIL);
				setMessage("����ʧ��");
			}
			addLog(SqlConnection.getInstance().getLog());
		}else {
			setStatus(DATA_FAIL);
			setMessage("ȱ�ٲ���");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}