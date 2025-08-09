package com.se.user_service.dto;

public class GetEndPointResponseDTO {
    private String method;
    private String url;
    private String service;
    
    // public GetEndPointResponseDTO() {
    // }

    public GetEndPointResponseDTO(String method, String url, String service) {
        this.method = method;
        this.url = url;
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    
    
}
