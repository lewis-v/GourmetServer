package api;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

import java.io.IOException;
import java.util.Map;

public class ManagerComplaint extends BaseApi {
    public ManagerComplaint(Map<String, String> parmMap) {
        super(parmMap);
    }

    @Override
    public FullHttpResponse getResponse() throws IOException {
        if (parmMap.containsKey("is_handle")){
            SqlConnection.getInstance().search("*","is_handle = "+parmMap.get("is_handle"),"complaint_all");

        }else {
            setStatus(DATA_FAIL);
            setMessage("∑√Œ  ß∞‹");
        }
        addLog(SqlConnection.getInstance().getLog());
        response = ServiceResult.getJSONResult(js.toString());
        addLog(js.toString());
        return response;
    }
}
