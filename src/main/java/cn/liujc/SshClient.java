package cn.liujc;//

import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

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
public class SshClient {

    public static void main(String[] args)  throws Exception{

        Session session = JschUtil.getSession(
                "121.36.128.152",
                22,
                "root",
                "akqjxx110@");
        session.setUserInfo(new UserInfo() {
            public String getPassphrase() {
                return null;
            }

            public String getPassword() {
                return null;
            }

            public boolean promptPassword(String message) {
                return false;
            }

            public boolean promptPassphrase(String message) {
                return false;
            }

            public boolean promptYesNo(String message) {
                return false;
            }

            public void showMessage(String message) {

            }
        });
        //session.connect(5000);
        Channel channel = session.openChannel("shell");

        channel.connect();




    }

}
