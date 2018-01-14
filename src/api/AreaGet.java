package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class AreaGet extends BaseApi{

	public AreaGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		List<JSONObject> list = SqlConnection.getInstance().search("short_name", "", "area");
		if(list == null || list.size() == 0){
			setStatus(FAIL);
			setMessage("获取数据失败");
		}else{
			setStatus(SUCCESS);
			setMessage("获取数据成功");
			List<String> areaData = new ArrayList<>();
			for (JSONObject mJs : list){
				areaData.add("\""+mJs.get("short_name").toString()+"\"");
			}
			setData(areaData.toString());
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
