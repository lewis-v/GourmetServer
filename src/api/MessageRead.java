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
		if(parmMap.containsKey("id")){//已读单条
			if (SqlConnection.getInstance().setData("is_read", "0", "id = "+parmMap.get("id"), "message")){
				setStatus(SUCCESS);
				setMessage("已读成功");
			}else{
				setStatus(FAIL);
				setMessage("已读失败");
			}
		}else if(parmMap.containsKey("get_id") && parmMap.containsKey("put_id")){//已读一个会话
			if (SqlConnection.getInstance().setData("is_read", "0", "get_id = "+parmMap.get("get_id")+" AND put_id = "
					+parmMap.get("put_id") + " AND is_read = 1", "message")){
				setStatus(SUCCESS);
				setMessage("已读成功");
			}else{
				setStatus(FAIL);
				setMessage("已读失败");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
