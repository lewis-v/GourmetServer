package api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONObject;

public class Home extends BaseApi{

	public Home(Map<String, String> parmMap) {
		super(parmMap);
	}

	public void getResult(StringBuilder responseContent){
		responseContent.setLength(0);//Çå¿Õ
		String cache;
		File file = new File("Html/gourmet_home_form.html");
		if (file.exists()){
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				while((cache = bufferedReader.readLine()) != null){
					responseContent.append(cache);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	@Override
	public FullHttpResponse getResponse() throws IOException {
		return null;
	}

}
