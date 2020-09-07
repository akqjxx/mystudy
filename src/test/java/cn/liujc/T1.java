package cn.liujc;//

import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.junit.Test;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                           O\  =  /O
//                        ____/`---'\____
//                      .'  \\|     |//  `.
//                     /  \\|||  :  |||//  \
//                    /  _||||| -:- |||||-  \
//                    |   | \\\  -  /// |   |
//                    | \_|  ''\---/''  |   |
//                    \  .-\__  `-`  ___/-. /
//                  ___`. .'  /--.--\  `. . __
//               ."" '<  `.___\_<|>_/___.'  >'"".
//              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//              \  \ `-.   \_ __\ /__ _/   .-` /  /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//                      佛祖保佑       永无BUG
public class T1 {

    @Test
    public void t1() throws Exception {

        Session session = JschUtil.getSession(
                "121.36.128.152",
                22,
                "root",
                "akqjxx110@");
        session.setUserInfo(new MyUserInfo());
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand("echo zhangwu |tee b.txt");
        ((ChannelExec) channel).setCommand("hahhahha");
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        //((ChannelShell)channel).setPtyType("vt102");
//        session.setConfig("StrictHostKeyChecking", "yes");
        channel.connect();
        try (InputStream inputStream = channel.getInputStream();
             OutputStream outputStream = channel.getOutputStream();
             PrintStream printStream = new PrintStream(outputStream);
        ) {

            //printStream.println("echo 'hello' >> a.txt");
            // printStream.println("uname");
            int res = -1;
            StringBuffer buf = new StringBuffer(1024);
            byte[] tmp = new byte[1024];
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    buf.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    res = channel.getExitStatus();
                    System.out.println(format("Exit-status: %d", res));
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
            System.out.println("buf=" + buf);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
            session.disconnect();
        }

    }
}
