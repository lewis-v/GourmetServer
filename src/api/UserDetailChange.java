package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class UserDetailChange extends BaseApi{

	public UserDetailChange(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id")){
			String id = parmMap.get("id");
			if (setUserData(id, "img_header")&&//头像
					setUserData(id, "personal_back")&&//个人中心背景
					setUserData(id, "sex")&&//性别
					setUserData(id, "address")&&//地区
					setUserData(id, "nickname")&&//昵称
					setUserData(id, "introduction")//简介
					)
			{ 
				List<JSONObject> list = 
						SqlConnection.getInstance().search("*", "user_id = "+id, "user_info_all");
				addLog(SqlConnection.getInstance().getLog());
				if (list.size() > 0){
					setStatus(SUCCESS);
					setMessage("修改成功");
					setData(list.get(0).toString());
				}else {
					setStatus(DATA_FAIL);
					setMessage("操作失败");
				}
			}
		}else{
			setStatus(DATA_FAIL);
			setMessage("修改失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

	/**
	 * 修改个人数据
	 * @param id 用户id
	 * @param key 数据名
	 */
	public boolean setUserData(String id,String key){
		if (parmMap.containsKey(key)){//头像
			if (!SqlConnection.getInstance().setData(key
					, "\'"+parmMap.get(key)+"\'", "user_id = "+id,"user_info")){
				return false;
			}
		}
		return true;
	}
}
