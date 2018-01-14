package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.ls.LSInput;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class Init extends BaseApi{

	public Init(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		List<JSONObject> list = SqlConnection.getInstance().search("*", "", "init_data");
		if(list == null || list.size() == 0){
			setStatus(FAIL);
			setMessage("获取数据失败");
		}else{
			setStatus(SUCCESS);
			setMessage("获取数据成功");
			list.get(0).put("time", System.currentTimeMillis()/1000);
			setData(list.get(0).toString());
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
