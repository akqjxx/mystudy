package cn.liujc;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class SSHExecutor {
    private static long INTERVAL = 100L;
    private static int SESSION_TIMEOUT = 30000;
    private static int CHANNEL_TIMEOUT = 3000;
    private JSch jsch = null;
    private Session session = null;

    private SSHExecutor(SSHInfo sshInfo) throws JSchException {
        jsch =new JSch();
        session = jsch.getSession(sshInfo.getUser(),sshInfo.getHost(),sshInfo.getPort());
        session.setPassword(sshInfo.getPassword());
        session.setUserInfo(new MyUserInfo());
        session.connect(SESSION_TIMEOUT);
    }

    /*
    * 在这里修改访问入口,当然可以把这个方法弄到SSHExecutor外面，这里是方便操作才这么做的
    * */
    public static SSHExecutor newInstance() throws JSchException {
        SSHInfo sshInfo = new SSHInfo("root","******","locahost",22);
        return new SSHExecutor(sshInfo);
    }

    /*
    * 注意编码转换
    * */
    public long shell(String cmd, String outputFileName) throws JSchException, IOException, InterruptedException {
        long start = System.currentTimeMillis();
        Channel channel = session.openChannel("shell");
        PipedInputStream pipeIn = new PipedInputStream();
        PipedOutputStream pipeOut = new PipedOutputStream( pipeIn );
        FileOutputStream fileOut = new FileOutputStream( outputFileName, true);
        channel.setInputStream(pipeIn);
        channel.setOutputStream(fileOut);
        channel.connect(CHANNEL_TIMEOUT);

        pipeOut.write(cmd.getBytes());
        Thread.sleep( INTERVAL );
        pipeOut.close();
        pipeIn.close();
        fileOut.close();
        channel.disconnect();
        return System.currentTimeMillis() - start;
    }

    public int exec(String cmd) throws IOException, JSchException, InterruptedException {
        ChannelExec channelExec = (ChannelExec)session.openChannel( "exec" );
        channelExec.setCommand( cmd );
        channelExec.setInputStream( null );
        channelExec.setErrStream( System.err );
        InputStream in = channelExec.getInputStream();
        channelExec.connect();

        int res = -1;
        StringBuffer buf = new StringBuffer( 1024 );
        byte[] tmp = new byte[ 1024 ];
        while ( true ) {
            while ( in.available() > 0 ) {
                int i = in.read( tmp, 0, 1024 );
                if ( i < 0 ) break;
                buf.append( new String( tmp, 0, i ) );
            }
            if ( channelExec.isClosed() ) {
                res = channelExec.getExitStatus();
                System.out.println( format( "Exit-status: %d", res ) );
                break;
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
        System.out.println( buf.toString() );
        channelExec.disconnect();
        return res;
    }

    public Session getSession(){
        return session;
    }

    public void close(){
        getSession().disconnect();
    }

    /*
    * SSH连接信息
    * */
    public static class SSHInfo{
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

    /*
    * 自定义UserInfo
    * */
    private static class MyUserInfo implements UserInfo{

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
}