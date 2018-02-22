package api;

import base.BaseApi;
import dao.SqlConnection;
import io.netty.handler.codec.http.FullHttpResponse;
import utils.ServiceResult;

import java.io.IOException;
import java.util.Map;

public class ManagerReject extends BaseApi {
    public ManagerReject(Map<String, String> parmMap) {
        super(parmMap);
    }

    @Override
    public FullHttpResponse getResponse() throws IOException {
        if (parmMap.containsKey("type") && parmMap.containsKey("act_id") && parmMap.containsKey("id") && parmMap.containsKey("content")){
            if (SqlConnection.getInstance().insertData("time,user_id,type,act_id,content"
                    ,System.currentTimeMillis()+","+parmMap.get("id")+","+parmMap.get("type")+","+parmMap.get("act_id")+",'"+parmMap.get("content")+"'","reject")){
                setStatus(SUCCESS);
                setMessage("�ܾ��ɹ�");
            }else{
                setStatus(FAIL);
                setMessage("�ܾ�ʧ��");
            }
        }else {
            setStatus(DATA_FAIL);
            setMessage("����ʧ��");
        }
        addLog(SqlConnection.getInstance().getLog());
        response = ServiceResult.getJSONResult(js.toString());
        addLog(js.toString());
        return response;
    }
}
