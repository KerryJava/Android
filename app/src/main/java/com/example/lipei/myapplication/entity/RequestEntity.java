package com.example.lipei.myapplication.entity;

import java.util.Map;

/**
 * Created by eric on 15-10-12.
 */
public class RequestEntity {
    private int id;
    private String jsonrpc="2.0";
    private String method;
    private Map<String ,Object> params;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "id=" + id +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
