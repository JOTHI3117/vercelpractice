package com.person.rvz.service;

public interface LoginService {

    String login(String username, String rawPassword);

    void updatePassword(String requesterUsername, String targetUsername, String newPassword);

}
