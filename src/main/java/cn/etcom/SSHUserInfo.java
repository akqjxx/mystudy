package cn.etcom;

import com.jcraft.jsch.UserInfo;

public class SSHUserInfo implements UserInfo {

    @Override public String getPassphrase() { return null; }

    @Override public String getPassword() { return null; }

    @Override public boolean promptPassword(String s) { return false; }

    @Override public boolean promptPassphrase(String s) { return false; }

    @Override
    public boolean promptYesNo(String s) {
        System.out.println(s);
        System.out.println("true");
        return true;
    }

    @Override public void showMessage(String s) { }
}