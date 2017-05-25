package me.j360.rpc.codec.protostuff;

import java.util.Map;

/**
 * RPC Response
 * @author huangyong
 * @author min_xu
 */
public class RpcResponse {

    private Long requestId;
    private String error;
    private Object result;

    //为后期添加额外信息准备,链路、tcc等
    private Map<String,String> headers;

    public boolean isError() {
        return error != null;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
