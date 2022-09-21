package com.abhinay.walletsystem.jwtimpl;


public class AuthenticationResponse {
    private final String userEmail;
    private final Boolean loginStatus;
    private final String jwt;

    public AuthenticationResponse(String userEmail, Boolean loginStatus, String jwt) {
        this.userEmail = userEmail;
        this.loginStatus = loginStatus;
        this.jwt = jwt;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public String getJwt() {
        return jwt;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "userEmail='" + userEmail + '\'' +
                ", loginStatus=" + loginStatus +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
