package com.devtest.devtest.service;

import com.devtest.devtest.model.HUNTUSER_BOOKMARK;
import com.devtest.devtest.model.User;

import java.util.List;

public interface LoginService {
    User getUser(String email); //select

    List<User> getUserList(String email);

    int insertUser(User user);

    User getNickname(String nickname);

    int Insert_User_BookMark(HUNTUSER_BOOKMARK bookmark);

    List<HUNTUSER_BOOKMARK> get_HUNTUSER_BOOKMARK(int uno);

    int delete_User_BookMark(String companyname,int uno);

    Integer get_HUNTUSER_BOOKMARK_Check(String companyname,int uno);

    List<HUNTUSER_BOOKMARK> BookMark_Check(String email);

}