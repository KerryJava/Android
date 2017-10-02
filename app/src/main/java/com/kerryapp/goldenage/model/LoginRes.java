package com.kerryapp.goldenage.model;

/**
 * Created by mojet on 2017/10/2.
 */

public class LoginRes {
    public String jsonrpc;
    public Res result;
    public int id;

    public class Res {
        public UserData userdata;
        public int status;
        public String statusmsg;
    }

    public class UserData {
        public String userid;
        public String phone;
        public String name;
        public String token;
    }
}
