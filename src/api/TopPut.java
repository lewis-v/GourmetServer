package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class TopPut extends BaseApi{
	private boolean isSuccess = false;//是否成功

	public TopPut(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("act_id")
				&& parmMap.containsKey("type")){

			List<JSONObject> list = SqlConnection.getInstance().search("*", "user_id = "+parmMap.get("id")+"  Order by top_num ", "top");
			if (list != null && list.size()>0){
				if (list.size()>=2 && parmMap.containsKey("top_num") && parmMap.containsKey("top_num2")){
					SqlConnection.getInstance().setData("top_num", parmMap.get("top_num"), "user_id = "+parmMap.get("id")+" AND top_num = "+parmMap.get("top_num2"), "top");
					SqlConnection.getInstance().setData("top_num",  parmMap.get("top_num"), "user_id = "+parmMap.get("id")
					+" AND type = "+parmMap.get("type") + " AND act_id = "+parmMap.get("act_id"), "top");
					setStatus(SUCCESS);
					setMessage("操作成功");

				}else{
					if (list.size() == 3){//删除最后一个
						if (!SqlConnection.getInstance().removeData("user_id = "+parmMap.get("id") +" AND top_num = 3", "top")){

						}
						list.remove(2);
					}
					for (int num = 0,len = list.size();num < len;num ++){
						SqlConnection.getInstance().setData("top_num", String.valueOf(num+2), "user_id = "+parmMap.get("id")+" AND top_num = "+num+1, "top");
					}
					SqlConnection.getInstance().insertData("user_id,act_id,type,top_num", parmMap.get("id")+","+
							parmMap.get("act_id")+","+parmMap.get("type")+",1", "top");
				}
			}else{
				if (SqlConnection.getInstance().insertData("user_id,act_id,type,top_num", parmMap.get("id")+","+
						parmMap.get("act_id")+","+parmMap.get("type")+",1", "top")){
					setStatus(SUCCESS);
					setMessage("操作成功");
				}else{
					setStatus(FAIL);
					setMessage("操作失败");
				}
			}
		}else{
			setStatus(DATA_FAIL);
			setMessage("访问失败");
		}	
		addLog(js.toString());
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
