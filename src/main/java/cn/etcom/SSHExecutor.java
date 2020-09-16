package cn.etcom;

import com.jcraft.jsch.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * SSH 客户端
 */
public class SSHExecutor {
    private final static long INTERVAL = 100L;
    private final static int SESSION_TIMEOUT = 300000;
    private final static int CHANNEL_TIMEOUT = 30000;
    private Session session;

    public SSHExecutor(SSHInfo sshInfo) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(sshInfo.getUser(), sshInfo.getHost(), sshInfo.getPort());
        session.setPassword(sshInfo.getPassword());
        session.setUserInfo(new SSHUserInfo());
        session.connect(SESSION_TIMEOUT);
        session.setConfig("StrictHostKeyChecking", "no");
    }

    /*
     * 注意编码转换
     * */
    public long shell(String cmd) throws JSchException, IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        channel.setPtyType("vt200");
        OutputStream outputStream = channel.getOutputStream();
        PrintStream commander = new PrintStream(outputStream, true, "UTF-8");
        channel.connect(CHANNEL_TIMEOUT);
        commander.println(cmd);
        //Thread.sleep( INTERVAL );
        commander.println("exit");
//        Thread.sleep( INTERVAL );
        commander.close();
        InputStream inputstream  = channel.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result.append(line);
            System.out.println("line:" + line);
        }
        System.out.println("result:" + result);
        inputstream.close();
        outputStream.close();
        br.close();
        channel.disconnect();
        return System.currentTimeMillis() - start;
    }

    public String exec(String cmd) throws IOException, JSchException, InterruptedException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(cmd);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        //channelExec.setPtyType("vt200");
        InputStream in = channelExec.getInputStream();
        channelExec.connect();
        Thread.sleep(500);
        int res = -1;
        StringBuffer buf = new StringBuffer(1024);
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                buf.append(new String(tmp, 0, i));
            }
            if (channelExec.isClosed()) {
                res = channelExec.getExitStatus();
                System.out.println(format("Exit-status: %d", res));
                break;
            }
            TimeUnit.MILLISECONDS.sleep(500);
        }
        System.out.println("buf.toString()=" + buf.toString());
        channelExec.disconnect();
        return buf.toString();
    }

    public Session getSession() {
        return session;
    }

    public void close() {
        getSession().disconnect();
    }

    /*
     * SSH连接信息
     * */
    public static class SSHInfo {
        private String user;
        private String password;
        private String host;
        private int port;

        public SSHInfo(String user, String password, String host, int port) {
            this.user = user;
            this.password = password;
            this.host = host;
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }


}
