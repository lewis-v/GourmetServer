package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class CollectionGet extends BaseApi{

	public CollectionGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("id")){
			List<JSONObject> list = SqlConnection.getInstance().search("*", " collection_id = "+parmMap.get("id"), "collect_all");
			setStatus(SUCCESS);
			setMessage("获取成功");
			setData(list.toString());
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;

	}

}
