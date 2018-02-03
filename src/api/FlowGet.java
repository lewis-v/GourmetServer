package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class FlowGet extends BaseApi{

	public FlowGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		List<JSONObject> list = SqlConnection.getInstance().search("*", "", "flow_data group by num ");
		if (list != null && list.size()>0){
			setData(list.toString());
			setStatus(SUCCESS);
			setMessage("获取成功");
		}else{
			setStatus(FAIL);
			setMessage("获取失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}
}
