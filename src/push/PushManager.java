package push;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.mysql.jdbc.log.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import utils.Api;
import utils.MD5;

public class PushManager {
	public static final String PUSH_PATH = "https://api.jpush.cn/v3/push";
	public static final String Authorization = " Basic ODdkYTlmNGI4NjU3ZGY2YjdkOWVlNzZjOjY0NTkxNDFmNDg1NWVkNTgxZDgwYWJhZQ==";

	private static class Instance{
		private static final PushManager Instance = new PushManager();
	}

	public static PushManager getInstance(){
		return Instance.Instance;
	}

	/**
	 * ���͸�������
	 * @param content
	 * @param title
	 */
	public String pushAll(String content,String title){
		JSONObject js = new JSONObject();
		js.put("plats", "[\"android\"]");//ƽ̨
		js.put("audience", "\"all\"");//���ͷ�Χ
		js.put("notification", "\"android\" : {\"alert\":\""+content+"\",\"title\":\""+title+"\"}");
		String result = Api.getInstance().post(Authorization,PUSH_PATH,js.toString());
		System.out.println("push:"+result);
		return result;
	}

	/**
	 * ������Ϣ��ָ���û�
	 * @param list message_detail��������������
	 * @return
	 */
	public String sendMessage(List<JSONObject> list){
		if (list != null && list.size()>0){
			JSONObject js = new JSONObject();
			js.put("plats", "[[\"android\"]]");//ƽ̨
			js.put("audience", "{\"alias\" : [ \""+list.get(0).get("get_id")+"\"]}");//���ͷ�Χ
			js.put("messaeg", "{\"msg_content\":\"message\",\"extras\":\""+list.get(0).toString()+"\"}");
			String result = Api.getInstance().post(Authorization,PUSH_PATH,js.toString());
			System.out.println("push:"+result);
			return result;
		}
		return "����������";
	}
}
