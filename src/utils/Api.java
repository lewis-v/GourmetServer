package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
	private static Api api;

	public static Api getInstance(){//��ȡ��������
		if(api == null){
			api = new Api();
		}
		return api;
	}

	public String httpGet(String HOST,String url,String uid) throws IOException {
		OkHttpClient httpClient = new OkHttpClient();
		Request request = new Request.Builder()
				.get()
				.url(HOST+url)
				.build();
		System.out.println("Get:�ȴ��ظ�");
		Response response = httpClient.newCall(request).execute();
		String result = response.body().string();
		System.out.println("GET:����ת��:"+result);
		return result; // ���ص���string ���ͣ�json��mapper����ֱ�Ӵ���
	}

	public String httpPost(String Authorization,String path, RequestBody requestBody) throws IOException {
		Builder okBuilder = new OkHttpClient().newBuilder();
		Request request = new Request.Builder()
				.addHeader("Authorization", Authorization)
				.post(requestBody)
				.url(path)
				.build();
		System.out.println("POST:�ȴ��ظ�:"+Authorization+";"+path);

		Response response = okBuilder.build().newCall(request).execute();
		String result = response.body().string();
		System.out.println("POST:����ת��:"+result);
		return result; 
	}

	/**
	 * ����post push
	 * @param key
	 * @param secret
	 * @param path
	 * @param body
	 * @return
	 */
	public String post(String Authorization,String path,String body){
		StringBuffer sbuffer= new StringBuffer("");
		try {
			URL url = new URL(path);
			HttpURLConnection  conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");   //���ñ�������ķ�ʽ �� Ĭ����GET��ʽ�� ����Ҫ���Ǵ�д��ĸ
			conn.setConnectTimeout(5000);//�������ӳ�ʱ
			conn.setDoInput(true);//�Ƿ�������� �� �˷���Ĭ��Ϊtrue
			conn.setDoOutput(true);//�Ƿ��������� �˷���Ĭ��Ϊfalse
			conn.addRequestProperty("Authorization", Authorization);
			conn.addRequestProperty("Content-type", "application/json; charset=utf-8");
			conn.connect();//��ʾ����
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
			writer.write(body);
			writer.flush();
			os.close();
			writer.close();
			System.out.println("post:"+Authorization+";"+body);
			int code =  conn.getResponseCode();
			sbuffer.append(code);
			InputStreamReader inputStream;
			BufferedReader reader;
			String lines;  
			if (code == 200){
				inputStream =new InputStreamReader(conn.getInputStream());//����HttpURLConnection���Ӷ����getInputStream()����, ���ڴ滺�����з�װ�õ�������HTTP������ķ��͵�����ˡ� 
			}else{
				inputStream =new InputStreamReader(conn.getErrorStream());
				sbuffer.append("ʧ��");
			}
			reader = new BufferedReader(inputStream);  
			while ((lines = reader.readLine()) != null) {                

				lines = new String(lines.getBytes(), "utf-8");                    

				sbuffer.append(lines);                
			}  
			reader.close();    

			conn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sbuffer.toString();
	}
	
}
