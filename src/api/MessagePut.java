package api;

import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class MessagePut extends BaseApi{
	//��Ϣ����
	public final static String TEXT = "0";//�ı�
	public final static String VOICE = "1";//����
	public final static String IMG = "2";//ͼƬ

	public MessagePut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("put_id") && parmMap.containsKey("get_id") && parmMap.containsKey("type")
				&&(parmMap.containsKey("content") || parmMap.containsKey("img"))){
			StringBuilder id = new StringBuilder();
			id.append("put_id,get_id,type,put_time");
			StringBuilder data = new StringBuilder();
			data.append(parmMap.get("put_id"));
			data.append(",");
			data.append(parmMap.get("get_id"));
			data.append(",");
			data.append(parmMap.get("type"));
			data.append(",");
			data.append(String.valueOf(System.currentTimeMillis()/1000));
			switch (parmMap.get("type")) {
			case TEXT:
				if (parmMap.containsKey("content")){
					id.append(",content");
					data.append(",'");
					data.append(parmMap.get("content"));
					data.append("'");
				}else{
					setStatus(DATA_FAIL);
					setMessage("����ʧ��");
				}
				break;
			case VOICE:

				break;
			case IMG:
				if (parmMap.containsKey("img")){
					id.append(",img");
					data.append(",'");
					data.append(parmMap.get("img"));
					data.append("'");
				}else{
					setStatus(DATA_FAIL);
					setMessage("����ʧ��");
				}
				break;
			}
			if (SqlConnection.getInstance().insertData(id.toString(), data.toString(), "message")){
				setStatus(SUCCESS);
				setMessage("���ͳɹ�");
			}else{
				setStatus(FAIL);
				setMessage("����ʧ��");
			}
			addLog(SqlConnection.getInstance().getLog());
		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
