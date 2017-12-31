package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class ShareDiaryGet extends BaseApi{

	public ShareDiaryGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id")){
			List<JSONObject> list = SqlConnection.getInstance().search("*", "id = "+parmMap.get("id"), "diary_all");
			addLog(SqlConnection.getInstance().getLog());
			if (list != null && list.size()>0){
				setStatus(SUCCESS);
				setMessage("��ȡ�ɹ�");
				setData(list.get(0).toString());
			}else {
				setStatus(FAIL);
				setMessage("��ȡʧ��");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}