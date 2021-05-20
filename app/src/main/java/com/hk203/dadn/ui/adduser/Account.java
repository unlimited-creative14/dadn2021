package com.hk203.dadn.ui.adduser;

import java.io.Serializable;

public class Account implements Serializable {
    public enum UserType
    {
        Admin, User
    }
    public class AccountInfo implements Serializable{
        String fname, lname;
        String cmndNum;
        String email;

        public AccountInfo()
        {

        }

        public AccountInfo(String fname, String lname, String cmndNum, String email, String room) {
            this.fname = fname;
            this.lname = lname;
            this.cmndNum = cmndNum;
            this.email = email;
            this.room = room;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getCmndNum() {
            return cmndNum;
        }

        public void setCmndNum(String cmndNum) {
            this.cmndNum = cmndNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        String room;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    UserType userType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String username;
    String password;

    public AccountInfo getInfo() {
        return info;
    }

    AccountInfo info;

    public Account() {
        info = new AccountInfo();
    }
}
