package api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import utils.TimeUtils;

public class ShareOther extends BaseApi{
	public ShareOther(Map<String, String> parmMap){
		super(parmMap);
	}

	public void getResult(StringBuilder responseContent){
		responseContent.setLength(0);//清空
		if (parmMap.containsKey("id") && parmMap.containsKey("type")){
			String type = parmMap.get("type");
			String id = parmMap.get("id");
			File file;
			String cache;
			List<JSONObject> js;
			switch (type) {
			case "0"://日记
				js = SqlConnection.getInstance().search("*", "id = "+id, "diary");
				addLog(SqlConnection.getInstance().getLog());
				if(js == null || js.size() == 0){//无法搜索到对应数据
					break;
				}
				file = new File("Html/gourmet_diary_form.html");
				if (file.exists()){
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						while((cache = bufferedReader.readLine()) != null){
							responseContent.append(cache);
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
					cache = responseContent.toString();
					responseContent.setLength(0);
					Map<String, String> parm = js.get(0);
					if (parm.containsKey("title")){//标题
						cache = cache.replaceAll(">gourmet_title<", ">"+parm.get("title")+"<");
					}else {
						cache = cache.replaceAll(">gourmet_title<", ">美食日记<");
					}
					if (parm.containsKey("address")){//地址
						cache = cache.replaceAll("<h5>gourmet_address ", "<h5>"+parm.get("address"));
					}else {
						cache = cache.replaceAll("<h5>gourmet_address", "<h5>");
					}
					if (parm.containsKey("time")){//时间
						cache = cache.replaceAll(" gourmet_time ",parm.get("time"));
					}else {
						cache = cache.replaceAll("gourmet_time ", " ");
					}
					js = SqlConnection.getInstance().search("*", "user_id = "+id, "user_info");
					if (js != null && js.size()>0 && js.get(0).containsKey("auth")){
						cache = cache.replaceAll("gourmet_auth</h5>", js.get(0).getString("auth")+"</h5>");//作者
					}else {
						cache = cache.replaceAll("gourmet_auth</h5>", "</h5>");
					}
					if (parm.containsKey("content")){//内容
						cache = cache.replaceAll("gourmet_content</body>", parm.get("content")+"</body>");
					}else {
						cache = cache.replaceAll("gourmet_content</body>", "请下载APP后查看</body>");
					}
					responseContent.append(cache);
					addLog(cache);
				}
				break;
			case "1"://攻略

				break;
			case "2"://食谱

				break;
			case "3"://普通分享
				js = SqlConnection.getInstance().search("*", "id = "+id+" AND type = "+type, "share_list_all");
				addLog(SqlConnection.getInstance().getLog());
				if(js == null || js.size() == 0){//无法搜索到对应数据
					break;
				}
				file = new File("Html/gourmet_common_form.html");
				if (file.exists()){
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						while((cache = bufferedReader.readLine()) != null){
							responseContent.append(cache);
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
					cache = responseContent.toString();
					responseContent.setLength(0);
					Map<String, String> parm = js.get(0);
					if (parm.containsKey("title")){//标题
						cache = cache.replaceAll(">gourmet_title<", ">"+parm.get("title")+"<");
					}else {
						cache = cache.replaceAll(">gourmet_title<", ">我的分享<");
					}
					if (parm.containsKey("img_header")){//头像
						cache = cache.replaceAll("src=\"gourmet_header\"", "src=\""+parm.get("img_header")+"\"");
					}else {
						cache = cache.replaceAll("src=\"gourmet_header\"", "src=\"http://39.108.236.30:47423/img/def_header.png\"");
					}
					if (parm.containsKey("nickname")){//分享者
						cache = cache.replaceAll(">gourmet_auth ",">"+parm.get("nickname"));
					}else {
						cache = cache.replaceAll(">gourmet_auth ", "> ");
					}
					if (parm.containsKey("put_time")){//时间
						cache = cache.replaceAll("gourmet_time<", parm.get("put_time")+"<");
					}else {
						cache = cache.replaceAll("gourmet_time<", " ");
					}
					if (parm.containsKey("content")){//文字内容
						cache = cache.replaceAll("gourmet_content</br></br>", parm.get("content")+"</br></br>");
					}else {
						cache = cache.replaceAll("gourmet_content</br></br>", " ");
					}
					if (parm.containsKey("img")){//图片内容
						String imgs = ((Object)parm.get("img")).toString();
						if (imgs!=null && imgs.length()>3){
							imgs = imgs.substring("[".length(),imgs.length()-1);
							addLog(imgs);
							String[] arr = imgs.split(",");
							StringBuilder stringBuilder = new StringBuilder();
							for (String str : arr){
								stringBuilder.append("<li><img src ="+str+" style=\"max-width: 30%; height: auto;\"></li>");
							}
							cache = cache.replaceAll("gourmet_img</body>", stringBuilder.toString()+"</body>");
						}
					}else {
						cache = cache.replaceAll("gourmet_img</body>", "</body>");
					}
					responseContent.append(cache);
					addLog(cache);
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		return null;
	}
}
