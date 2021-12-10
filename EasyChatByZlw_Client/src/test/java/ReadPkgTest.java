

import org.junit.Test;
import java.io.*;
import java.net.Socket;

public class ReadPkgTest {
    @Test
    public void ReceivePkg() throws IOException {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        Socket socket = new Socket("127.0.0.1", 8889);
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] du = new byte[8096];
        byte[] len = new byte[4];
        dis.read(len);
        int a = bytesToIntLowAhead(len, 0);
        System.out.println(a);

        dis.read(du);
        /*//输出读到的每一个字符
        for (int i = 0;i < a;i++)
            System.out.println(du[i])*/;
        // String 的构造方法的一种，指定字符集。
        String res = new String(du, 0, a, "UTF-8");
        System.out.println(res);

    }
    /*×将一个字节数组转换为一个整数，按照低位在后，高位在前的顺序
     * byte数组中取int数值
     */
    public static int bytesToIntLowAhead(byte[] src, int from) {
        return (src[from] & 0xFF)
                | ((src[from + 1] & 0xFF) << 8)
                | ((src[from + 2] & 0xFF) << 16)
                | ((src[from + 3] & 0xFF) << 24);
    }
}