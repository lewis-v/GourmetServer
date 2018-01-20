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
		if (parmMap.containsKey("type") && parmMap.containsKey("id")){//��ȡ��act��Ϊ,0,ȫ��,1����,2��,3��
			String act = parmMap.get("type");
			where = " user_id = "+parmMap.get("id");
			if (act.equals("0")){
				from = " comment_my_all ";
			}else if(act.equals("1")){
				from = " comment_my ";
			}else if(act.equals("2")){
				from = " remark_my ";
				where = where + " AND act = 1";
			}else if(act.equals("3")){
				from = " remark_my ";
				where = where + " AND act = 0";
			}else{
				setStatus(DATA_FAIL);
				setMessage("����ʧ��");
				response = ServiceResult.getJSONResult(js.toString());
				addLog(js.toString());
				return response;
			}

			if(parmMap.containsKey("time_flag") && parmMap.containsKey("act")){//act�О�,1������,-1������
				if (where.length() > 1){
					where = where+" AND ";
				}
				if (parmMap.get("act").equals("1")){//�ϸ���
					where = where + "put_time > " + parmMap.get("time_flag");
				}else{//�¼��d
					where = where + "put_time < " + parmMap.get("time_flag");
					where = where + " LIMIT 0,10";
				}
			}else{
				if (where.length()==0){
					from = from + " LIMIT 0,10";
				}else{
					where = where + " LIMIT 0,10";
				}
			}

			List<JSONObject> list = SqlConnection.getInstance().search("*", where, from);
			addLog(SqlConnection.getInstance().getLog());
			setStatus(SUCCESS);
			setMessage("��ȡ�ɹ�");
			setData(list.toString());

		}else {
			setStatus(DATA_FAIL);
			setMessage("����ʧ��");
		}
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}

