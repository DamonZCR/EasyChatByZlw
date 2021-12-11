package utils;

import com.alibaba.fastjson.JSON;
import message.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Utils {
    private  DataInputStream dis = null;
    private DataOutputStream dos = null;
    private FilterChinese fc = null;
    public String s = "ceshi";

    public Utils(Socket c_socket) {
        try {
            dis = new DataInputStream(c_socket.getInputStream());
            dos = new DataOutputStream(c_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        fc = new FilterChinese();
    }

    // 发送
    public void WritePkg(String mess) throws InterruptedException {
        try {// 1、首先发送消息的长度
            // 信息过滤（筛选中文汉字和中文字符个数）：fc.chineCharNum(mess).
            dos.write(mess.length() + 2 * fc.chineCharNum(mess));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //需要给服务器端200毫秒的延迟，要不然会报错。
        Thread.sleep(200);

        try {// 2、再发送消息本身
            dos.write(mess.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // 接收
    /**
     * go客户端发送的是序列化后的消息，接收时需要使用byte数组去接收。
     * 因为采用的“安全接收”，即客户端与服务器端通信，首先发送的是消息的长度，然后再发送消息本身。
     * 这不仅为了判断通信过程中是否有丢包，也是为了客户端再接收的时候可以使用String类的构造方法，
     * 判断byte数组数据的长度，然后对byte数组进行转换。如果不判断实际接收的长度与想要发送的长度
     * 是否一直，而直接去转换就会转换错误，可能就导致信息类型不匹配。
     * @throws IOException
     */
    public String ReadPkg() throws IOException {
        String mess = null;
        byte[] du = new byte[8096];//接收消息本身
        byte[] lenB = new byte[4];//用来接收服务器发送的消息长度。
        dis.read(lenB);
        int len = bytesToIntLowAhead(lenB, 0);
        int reaLen = 0;
        try {
            reaLen = dis.read(du);
        }catch (IOException e){
            System.out.println("ReadPkg()接收信息时错误：" + e);
        }

        if (reaLen != len){
            System.out.println("ReadPkg()数据接收长度错误！");
            mess = "false";
        }else {
            // 接收到的数据与要发送的大致匹配，可以转换。
            // String 的构造方法的一种，指定字符集。
            mess = new String(du, 0, reaLen, "UTF-8");
            //Message mes = JSON.parseObject(mess, Message.class);
        }
        return mess;
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
