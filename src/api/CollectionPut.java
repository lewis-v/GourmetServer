package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class CollectionPut extends BaseApi{

	public CollectionPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if(parmMap.containsKey("id") &&parmMap.containsKey("act_id") && parmMap.containsKey("type")
				&& parmMap.containsKey("act")){//act:1收藏,-1取消收藏
			String where = "act_id = "+parmMap.get("act_id")
			+" AND type = "+parmMap.get("type") +" AND user_id = "+parmMap.get("id");
			List<JSONObject> list = SqlConnection.getInstance().search("*",where , "collection");
			if (parmMap.get("act").equals("1")){
				if (list != null && list.size()>0){
					setStatus(FAIL);
					setMessage("你已经收藏过啦");
				}else{
					if (SqlConnection.getInstance().insertData("type,act_id,user_id", parmMap.get("type")
							+","+parmMap.get("act_id")+","+parmMap.get("id"), "collection")){
						setStatus(SUCCESS);
						setMessage("收藏成功");
					}else{
						setStatus(FAIL);
						setMessage("收藏失败");
					}
				}
			}else {
				if (list == null || list.size()==0){
					setStatus(FAIL);
					setMessage("取消收藏失败");
				}else{
					if (SqlConnection.getInstance().removeData(where, "collection")){
						setStatus(SUCCESS);
						setMessage("取消收藏成功");
					}else{
						setStatus(FAIL);
						setMessage("取消收藏失败");
					}
				}
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
