package api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;
import utils.ServiceResult;

public class FindShare extends BaseApi{

	public FindShare(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("key") && parmMap.containsKey("type")){
			String key = parmMap.get("key");
			String from ="";
			String where = "";
			switch (parmMap.get("type")) {
			case "0"://�ռ�
				from = "diary_all";
				where = "title LIKE '%"+key+"%' OR address LIKE '%"+key+"%' OR content LIKE '%"+key+"%'";
				break;
			case "1"://����
				from = "raiders_all";
				where = "title LIKE '%"+key+"%' OR address LIKE '%"+key+"%' OR content LIKE '%"+key+"%' OR introduction LIKE '%"
						+key+"%' OR raiders_type LIKE '%"+key+"%' OR raiders_content LIKE '%"+key+"%'";
				break;
			case "2"://ʳ��
				from = "menu_all";
				where = "title LIKE '%"+key+"%' OR address LIKE '%"+key+"%' OR content LIKE '%"+key+"%' OR introduction LIKE '%"
						+key+"%' OR practice LIKE '%"+key+"%' OR ingredient LIKE '%"+key+"%' OR tip LIKE '%"+key+"%'";
				break;
			case "3"://��ͨ����
				from = "common_share_all";
				where = "address LIKE '%"+key+"%' OR content LIKE '%"+key+"%'";
				break;
			case "-1"://������
				from = "user_info";
				where = "nickname LIKE '%"+key+"%'";
				break;
			}
			if (parmMap.containsKey("user_id") && from.length()>0 && !parmMap.get("type").equals("-1")){
				from = from+" LEFT JOIN comment_status_all ON "+from+".type = comment_status_all.m_type AND "+from+".id = comment_status_all.m_id AND comment_status_all.m_user_id = "+parmMap.get("user_id");
			}	
			List<JSONObject> list = SqlConnection.getInstance().search("*", where, from);
			if (list != null && list.size()>0){
				setData(list.toString());
				setStatus(SUCCESS);
				setMessage("��ȡ�ɹ�");
			}else{
				setStatus(FAIL);
				if(parmMap.get("type").equals("-1")){
					setMessage("û����ص�����Ӵ");
				}else{
					setMessage("û����صĶ���Ӵ");
				}
			}

		}else{
			setStatus(FAIL);
			setMessage("����ʧ��");
		}
		addLog(SqlConnection.getInstance().getLog());
		response = ServiceResult.getJSONResult(js.toString());
		addLog(js.toString());
		return response;
	}

}
