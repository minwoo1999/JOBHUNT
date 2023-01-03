package com.devtest.devtest.service;

import com.devtest.devtest.mapper.LoginMapper;
import com.devtest.devtest.model.HUNTUSER_BOOKMARK;
import com.devtest.devtest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public User getUser(String email) {
        return loginMapper.getUser(email);
    }

    @Override
    public int insertUser(User user) {
        return loginMapper.insertUser(user);
    }

    @Override
    public User getNickname(String nickname) {
        return loginMapper.getNickname(nickname);
    }

    @Override
    public int Insert_User_BookMark(HUNTUSER_BOOKMARK bookmark) {
        System.out.println(bookmark);
        return loginMapper.Insert_User_BookMark(bookmark);
    }

    @Override
    public List<HUNTUSER_BOOKMARK> get_HUNTUSER_BOOKMARK(int uno) {
        return loginMapper.get_HUNTUSER_BOOKMARK(uno);
    }

    @Override
    public int delete_User_BookMark(String companyname, int uno) {
        return loginMapper.delete_User_BookMark(companyname,uno);
    }

    @Override
    public Integer get_HUNTUSER_BOOKMARK_Check(String companyname, int uno) {
        return this.loginMapper.get_HUNTUSER_BOOKMARK_Check(companyname, uno);
    }

    @Override
    public List<User> getUserList(String email) {
        return loginMapper.getUserList(email);
    }

    @Override
    public List<HUNTUSER_BOOKMARK> BookMark_Check(String email) {
        return loginMapper.BookMark_Check(email);
    }
}