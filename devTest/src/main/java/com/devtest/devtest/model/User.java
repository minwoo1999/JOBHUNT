package com.devtest.devtest.model;

public class User {
    int uno;

    private String email;
    private String pass;
    private String pass2;
    private String nickname;
    private String birth;
    private String phone;
    private String sex;
    private String pro_img;

    private int expireCheck;

    public int getExpireCheck() {
        return expireCheck;
    }

    public void setExpireCheck(int expireCheck) {
        this.expireCheck = expireCheck;
    }

    private String updatepass;
    private String updatepass2;

    public int getUno() {
        return uno;
    }

    public void setUno(int uno) {
        this.uno = uno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPro_img() {
        return pro_img;
    }

    public void setPro_img(String pro_img) {
        this.pro_img = pro_img;
    }

    public String getUpdatepass() {
        return updatepass;
    }

    public void setUpdatepass(String updatepass) {
        this.updatepass = updatepass;
    }

    public String getUpdatepass2() {
        return updatepass2;
    }

    public void setUpdatepass2(String updatepass2) {
        this.updatepass2 = updatepass2;
    }
}
