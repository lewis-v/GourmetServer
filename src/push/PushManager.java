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
	public static final String APP_KEY = "87da9f4b8657df6b7d9ee76c";
	public static final String APP_SECRET = "6459141f4855ed581d80abae";
	public static final String Authorization = " Basic ODdkYTlmNGI4NjU3ZGY2YjdkOWVlNzZjOjY0NTkxNDFmNDg1NWVkNTgxZDgwYWJhZQ==";

	private static class Instance{
		private static final PushManager Instance = new PushManager();
	}

	public static PushManager getInstance(){
		return Instance.Instance;
	}

	/**
	 * 推送给所有人
	 * @param content
	 * @param title
	 */
	public String pushAll(String content,String title){
		JSONObject cache = new JSONObject();
		cache.put("alert", content);
		cache.put("title", title);
		JSONObject ca2 = new JSONObject();
		ca2.put("android", cache);
		ca2.put("alert", content);
		JSONObject js = new JSONObject();
		js.put("platform", "all");//平台
		js.put("audience", "all");//推送范围
		js.put("notification", ca2.toString());
		String result  = "";
		result = Api.getInstance().post(Authorization,PUSH_PATH,js.toString());
		System.out.println("push:"+result);
		return result;
	}

	/**
	 * 发送消息给指定用户
	 * @param list message_detail表中搜索的数据
	 * @return
	 */
	public String sendMessage(List<JSONObject> list){
		if (list != null && list.size()>0){
			JSONObject js = new JSONObject();
			js.put("platform", "[\"android\"]");//平台
			JSONObject cache = new JSONObject();
			cache.put("alias", "[\""+list.get(0).get("get_id")+"\"]");
			js.put("audience", cache.toString());//推送范围
			cache.clear();
			cache.put("msg_content", "message");
			cache.put("extras", list.get(0).toString());
			js.put("message", cache.toString());
			String result = Api.getInstance().post(Authorization,PUSH_PATH,js.toString());
			System.out.println("push:"+result);
			return result;
		}
		return "推送无数据";
	}
}
