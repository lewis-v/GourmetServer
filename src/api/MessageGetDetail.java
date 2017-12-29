package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class MessageGetDetail extends BaseApi{

	public MessageGetDetail(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("put_id") && parmMap.containsKey("get_id")){
			String put_id = parmMap.get("put_id");
			String get_id = parmMap.get("get_id");
			String where = "(put_id = " + put_id + " AND get_id = " + get_id 
					+") OR (get_id = " + put_id + " AND put_id = "+get_id+")";
			List<JSONObject> list = SqlConnection.getInstance().search("*", where, "message_detail");
			addLog(SqlConnection.getInstance().getLog());
			if (list != null && list.size()>0){
				setStatus(SUCCESS);
				setMessage("success");
				setData(list.toString());
				addLog(list.toString());
			}else{
				setStatus(FAIL);
				setMessage("ªÒ»° ß∞‹");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("∑√Œ  ß∞‹");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
