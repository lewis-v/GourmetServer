package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class Crash extends BaseApi{

	public Crash(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("time") && parmMap.containsKey("content")){
			String id = "time,content";
			String value = parmMap.get("time")+",\""+parmMap.get("content")+"\"";
			if (parmMap.containsKey("id")){
				id = id + ",user_id";
				value = value + "," + parmMap.get("id");
			}
			if (SqlConnection.getInstance().insertData(id, value, "crash")){
				setStatus(SUCCESS);
				setMessage("������˵�С����,�Ѹ�֪����Ա��Ӵ~");
			}else{
				setStatus(FAIL);
				setMessage("����ʲô�����");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
