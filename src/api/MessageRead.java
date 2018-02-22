package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class MessageRead extends BaseApi{

	public MessageRead(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("id")){//�Ѷ�����
			if (SqlConnection.getInstance().setData("is_read", "0", "id = "+parmMap.get("id"), "message")){
				setStatus(SUCCESS);
				setMessage("�Ѷ��ɹ�");
			}else{
				setStatus(FAIL);
				setMessage("�Ѷ�ʧ��");
			}
		}else if(parmMap.containsKey("get_id") && parmMap.containsKey("put_id")){//�Ѷ�һ���Ự
			if (SqlConnection.getInstance().setData("is_read", "0", "get_id = "+parmMap.get("get_id")+" AND put_id = "
					+parmMap.get("put_id") + " AND is_read = 1", "message")){
				setStatus(SUCCESS);
				setMessage("�Ѷ��ɹ�");
			}else{
				setStatus(FAIL);
				setMessage("�Ѷ�ʧ��");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
