package me.j360.rpc.codec.protostuff;

/**
 * RPC Response
 * @author huangyong
 */
public class RpcResponse {

    private Long requestId;
    private String error;
    private Object result;

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
}
