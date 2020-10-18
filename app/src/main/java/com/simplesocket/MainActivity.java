package com.simplesocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.simplesocket.socket.BaseSocket;
import com.simplesocket.socket.RecvSocketHandler;
import com.simplesocket.socket.SocketDefine;
import com.simplesocket.socket.TcpSocket;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //Object
    public String               m_strIp             = "127.0.0.1";
    public int                  m_nPort             = 5000;
    public RecvSocketHandler    m_RecvHandler;
    public BaseSocket           m_SimpleSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Socket Init(Tcp Init)
        m_RecvHandler  = new RecvSocketHandler(m_SimpleSocket, getApplicationContext());
        m_SimpleSocket = new BaseSocket(m_RecvHandler, getApplicationContext(), SocketDefine.SOCKET_TCP);
        m_SimpleSocket.connect(m_strIp, m_nPort);

        //Socket Send
        SendMsg(SocketDefine.SOCKET_FUNCITON_MSG, "Socket Test Send");
    }

    public void SendMsg(int nSockcode, String SockValue)
    {
        try {
            if (m_SimpleSocket.GetConnect() == true) {
                TcpSocket pSocket = (TcpSocket) m_SimpleSocket.GetSocket();
                if(nSockcode != SocketDefine.SOCKET_NOT_PROTOCAL )
                    pSocket.GetDataOutputStream().writeInt(nSockcode);
                pSocket.GetDataOutputStream().writeInt(SockValue.length());
                pSocket.send(SockValue.getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
