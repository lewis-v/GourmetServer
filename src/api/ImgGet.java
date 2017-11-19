package api;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class ImgGet extends BaseApi{
	private static final String IMGPATH = ".\\img\\";
	private String uri = "";

	public ImgGet(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (uri != null && uri.length()>0){
			uri = uri.substring("/img/".length());
			File file = new File(IMGPATH+uri);
			if (file.exists()){
				response = ServiceResult.getFileResult(file.getAbsolutePath());
				addLog(js.toString());
				return response;
			}else{
				addLog("文件不存在");
				return null;
			}
		}else {
			return null;
		}
	}

	public ImgGet setUri(String uri){
		this.uri = uri;
		return this;
	}
}