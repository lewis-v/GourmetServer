package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class UserRegistered extends BaseApi{

	public UserRegistered(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("nickname") && parmMap.containsKey("password") 
				&& parmMap.containsKey("sex") && parmMap.containsKey("accout_number")){
			List<JSONObject> list = SqlConnection.getInstance().search("*", "accout_number = '"+parmMap.get("accout_number")+"'", "user");
			if (list != null && list.size()>0){
				setMessage("ÕËºÅÒÑ´æÔÚ");
				setStatus(DATA_FAIL);
			}else{
				SqlConnection.getInstance().insertData("accout_number,password,create_time", "'"+parmMap.get("accout_number")
						+"','"+parmMap.get("password")+"',"+System.currentTimeMillis(), "user");
				list = SqlConnection.getInstance().search("*", "accout_number = '"+parmMap.get("accout_number")+"'", "user");
				if (list == null ||list.size() ==0){
					setMessage("×¢²áÊ§°Ü");
					setStatus(FAIL);
				}else{
					String id = list.get(0).get("id").toString();
					if (SqlConnection.getInstance().insertData("user_id,sex,nickname", id+",'"+parmMap.get("sex")+"','"
							+parmMap.get("nickname")+"'", "user_info")){
						setMessage("×¢²á³É¹¦");
						setStatus(SUCCESS);
					}else{
						setMessage("×¢²áÊ§°Ü");
						setStatus(FAIL);
					}
					
				}
			}
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
