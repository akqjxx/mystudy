package cn.liujc;

import cn.etcom.SSHExecutor;
import org.junit.Test;

public class TestOver {


    @Test
    public void t1() throws Exception {
        SSHExecutor ssh = null;
        try {
            ssh = new SSHExecutor(
                    new SSHExecutor.SSHInfo(
                            "root",
                            "Akqjxx110@",
                            "121.36.128.152",
                            65522)
            );
            System.out.println("-----------1-----------");
            ssh.shell("ls\n");
            System.out.println("-----------2-----------");
            ssh.shell("ls\n");
            System.out.println("-----------3-----------");
            ssh.shell("ls\n");
            System.out.println("-----------4-----------");
            ssh.shell("ifconfig\n");
            System.out.println("-----------5-----------");
            ssh.shell("pwd\n");



           // ssh.shell("exit\n");



        } finally {
            ssh.close();
        }

    }

}
