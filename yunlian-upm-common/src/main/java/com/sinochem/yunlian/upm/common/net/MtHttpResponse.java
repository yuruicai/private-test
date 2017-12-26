package com.sinochem.yunlian.upm.common.net;

import com.sinochem.yunlian.upm.common.rpc.ErrorCode;
import com.sinochem.yunlian.upm.common.rpc.Paging;
import com.sinochem.yunlian.upm.common.rpc.Response;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author zhangxi
 * @created 13-1-16
 */
public class MtHttpResponse<T> implements Response<T> {
    private T data;
    private Paging page;
    private ErrorCode error;

    public MtHttpResponse() {
    }

    public MtHttpResponse(T data) {
        this.data = data;
    }

    public MtHttpResponse(T data, Paging paging) {
        this.data = data;
        this.page = paging;
        setPaging(paging);
    }

    public MtHttpResponse(ErrorCode error) {
        this.error = error;
    }

    public static <T> MtHttpResponse<T> create(T data) {
        return new MtHttpResponse<T>(data);
    }

    public static <T> MtHttpResponse<T> create(T data, Paging paging) {
        return new MtHttpResponse<T>(data, paging);
    }

    public static <T> MtHttpResponse<T> error(ErrorCode error) {
        return new MtHttpResponse(error);
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public Paging getPaging() {
        return page;
    }

    public void setPaging(Paging page) {
        this.page = page;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MtHttpResponse");
        sb.append("{data=").append(data);
        sb.append(", page=").append(page);
        sb.append(", error=").append(error);
        sb.append('}');
        return sb.toString();
    }
}
