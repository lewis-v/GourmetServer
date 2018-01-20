package api;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

public class ImgUp extends BaseApi{

	public ImgUp(Map<String, String> parmMap) {
		super(parmMap);
	}

	@Override
	public FullHttpResponse getResponse() throws IOException {
		if (parmMap.containsKey("id") && parmMap.containsKey("path")){
			File file = new File(parmMap.get("path"));
			if (file.exists()){
				if (SqlConnection.getInstance().insertData("user_id,path,up_time"
						, parmMap.get("id")+","+"\'http://39.108.236.30:47423/img/"+file.getName()+"\',"+System.currentTimeMillis()
						, "upimg")){
					setStatus(SUCCESS);
					setMessage("上传成功");
					setData("http://39.108.236.30:47423/img/"+file.getName());
				}else {
					setStatus(DATA_FAIL);
					setMessage("服务器出错");
				}
			}else {
				setStatus(DATA_FAIL);
				setMessage("上传失败");
			}
		}else {
			setStatus(DATA_FAIL);
			setMessage("操作失败");
		}
		response = ServiceResult.getJSONResult(js.toString());
		return response;
	}

}
