package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class CommentGetMy extends BaseApi{

	public CommentGetMy(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		String from = "";
		String where = "";
		if (parmMap.containsKey("type") && parmMap.containsKey("id")){//获取的act行为,0,全部,1评论,2赞,3踩
			String act = parmMap.get("type");
			where = "";
			if(parmMap.containsKey("time_flag") && parmMap.containsKey("act")){//act行為,1為向上,-1為向下
				if (where.length() > 1){
					where = where+" AND ";
				}
				if (parmMap.get("act").equals("1")){//上更新
					where = where + "put_time > " + parmMap.get("time_flag");
				}else{//下加載
					where = where + "put_time < " + parmMap.get("time_flag");
				}
			}
			if (where.length() > 1){
				where = where+" AND ";
			}
			if (act.equals("0")){
				from = " comment_my_all LEFT JOIN comment_status_all ON comment_my_all.type = comment_status_all.m_type AND comment_my_all.id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("id");
				where = where +  " user_id = "+parmMap.get("id")+"  GROUP BY comment_my_all.id,comment_my_all.type ORDER BY comment_my_all.put_time DESC ";
			}else if(act.equals("1")){
				from = " comment_my  LEFT JOIN comment_status_all ON comment_my.type = comment_status_all.m_type AND comment_my.id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("id");
				where = where + " comment_user_id = "+parmMap.get("id")+"  GROUP BY comment_my.id,comment_my.type ORDER BY comment_my.put_time DESC ";
			}else if(act.equals("2")){
				from = " remark_my  LEFT JOIN comment_status_all ON remark_my.type = comment_status_all.m_type AND remark_my.id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("id");
				where = where +  " user_id = "+parmMap.get("id")+" AND act = 1  GROUP BY remark_my.id,remark_my.type ORDER BY remark_my.put_time DESC ";
			}else if(act.equals("3")){
				from = " remark_my  LEFT JOIN comment_status_all ON remark_my.type = comment_status_all.m_type AND remark_my.id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("id");
				where = where +  " user_id = "+parmMap.get("id")+" AND act = 0  GROUP BY remark_my.id,remark_my.type ORDER BY remark_my.put_time DESC ";
			}else{
				setStatus(DATA_FAIL);
				setMessage("访问失败");
				response = ServiceResult.getJSONResult(js.toString());
				addLog(js.toString());
				return response;
			}


			if (where.length()==0){
				from = from + " LIMIT 0,10";
			}else{
				where = where + " LIMIT 0,10";
			}


			List<JSONObject> list = SqlConnection.getInstance().search("*", where, from);
			addLog(SqlConnection.getInstance().getLog());
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

