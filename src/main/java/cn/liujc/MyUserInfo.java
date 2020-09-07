package cn.liujc;

import com.jcraft.jsch.UserInfo;

public class MyUserInfo
        implements UserInfo {
    @Override
    public String getPassphrase() {
        System.out.println("getPassphrase");
        return null;
    }

    @Override
    public String getPassword() {
        System.out.println("getPassword");
        return null;
    }

    @Override
    public boolean promptPassword(String s) {
        System.out.println("promptPassword:" + s);
        return false;
    }

    @Override
    public boolean promptPassphrase(String s) {
        System.out.println("promptPassphrase:" + s);
        return false;
    }

    @Override
    public boolean promptYesNo(String s) {
        System.out.println("promptYesNo:" + s);
        return true;//notice here!
    }

    @Override
    public void showMessage(String s) {
        System.out.println("showMessage:" + s);
    }
}