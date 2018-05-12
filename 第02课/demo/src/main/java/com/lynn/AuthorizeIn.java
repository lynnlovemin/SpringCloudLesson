package com.lynn;

import javax.validation.constraints.NotBlank;

public class AuthorizeIn {

    @NotBlank(message = "缺少response_type参数")
    private String responseType;
    @NotBlank(message = "缺少client_id参数")
    private String ClientId;

    private String state;

    @NotBlank(message = "缺少redirect_uri参数")
    private String redirectUri;

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
