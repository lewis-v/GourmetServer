package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;
import utils.TimeUtils;

public class CommentGet extends BaseApi{

	public CommentGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("type") && parmMap.containsKey("id")){
			List<JSONObject> list = SqlConnection.getInstance()
					.search("*", "type = "+parmMap.get("type") +" AND act_id = "+parmMap.get("id"), "comment_all");
			for (JSONObject json : list){//��create_timeת����������ʾ��ʱ��
				if (json.containsKey("create_time")) {
					String create_time = TimeUtils.getTime(json.get("create_time").toString());
					json.remove("create_time");
					json.put("create_time", create_time);
				}
			}
			if (list != null && list.size()> 0){
				setStatus(SUCCESS);
				setMessage("��ȡ�ɹ�");
				setData(list.toString());
			}else {
				setStatus(FAIL);
				setMessage("��ȡʧ��");
			}
			addLog(SqlConnection.getInstance().getLog());
		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
