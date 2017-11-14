package api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.MD5;
import utils.ServiceResult;

public class Login extends BaseApi{
	Map<String, String> parmMap = new HashMap<>();

	public Login(Map<String, String> parmMap){
		this.parmMap = parmMap;
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
			if(!parmMap.containsKey("id")){//Œﬁ’À∫≈
				setStatus(DATA_FAIL).setMessage("«Î ‰»Î’À∫≈");
				response = ServiceResult.getJSONResult(js.toString());
			}else if(!parmMap.containsKey("password")){//Œﬁ√‹¬Î
				setStatus(DATA_FAIL).setMessage("«Î ‰»Î√‹¬Î");
				response = ServiceResult.getJSONResult(js.toString());
			}else{//µ«¬º≤Ÿ◊˜
				List<JSONObject> list = SqlConnection.getInstance()
						.search("id", "accout_number = \'"+parmMap.get("id")+"\' "
								+ "AND password = \'"+parmMap.get("password")+"\'", "user");
				if (list.size()>0){
					setStatus(SUCCESS).setMessage("µ«¬º≥…π¶");
					list = SqlConnection.getInstance()
							.search("*", "user_id = "+list.get(0).getString("id"), "user_info");
					if (list.size() > 0){
						String token = MD5.md5(System.currentTimeMillis()+"").toUpperCase();
						list.get(0).put("token", token);
						SqlConnection.getInstance().setData("token", "\'"+token+"\'"
								, "id = "+list.get(0).getString("user_id"), "user");
						setData(list.get(0).toString());
					}
					addLog(SqlConnection.getInstance().getLog());
				}else{
					setStatus(SUCCESS).setMessage("’À∫≈ªÚ√‹¬Î¥ÌŒÛ");
				}
				response = ServiceResult.getJSONResult(js.toString());
			}
		return response;

	}


}
