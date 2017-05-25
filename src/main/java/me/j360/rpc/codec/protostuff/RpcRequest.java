package me.j360.rpc.codec.protostuff;

import java.util.Map;

/**
 * RPC Request
 * @author huangyong
 * @author min_xu
 */
public class RpcRequest {

    private Long requestId;
    private String className;   //完成的类名
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    //为后期添加额外信息准备,链路、tcc等
    private Map<String,String> headers;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}