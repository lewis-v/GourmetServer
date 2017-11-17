package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class ShareListLoad extends BaseApi{
	public ShareListLoad(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		String where = "";
		if (parmMap.containsKey("type")){
			where = " type = '"+parmMap.get("type")+"'";
		}
		List<JSONObject> list = SqlConnection.getInstance().search("*", where, "share_list");
		addLog(list.toString());
		setStatus(SUCCESS);
		setMessage("º”‘ÿ≥…π¶");
		setData(list.toString());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
