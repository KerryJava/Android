package com.kerryapp.goldenage.model;

/**
 * Created by mojet on 2017/10/2.
 */

public class LoginParam {
    public long phone;
    public String passwd;
    public LoginLog log;

    public LoginParam(long phone,String passwd){
        this.phone = phone;
        this.passwd = passwd;
        this.log = new LoginLog();
    }

    public class LoginLog {
        public String screen;
        public int channel;
        public String device;
    }
}
