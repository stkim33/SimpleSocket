package com.simplesocket.socket;

import android.content.Context;
import android.os.Handler;


public class BaseSocket{

    static 	    BaseSocket 		                instance = null;
    public static BaseSocket _getinstance()
    {
        return instance;
    }

    private     String                          m_strIp;
    private     int                             m_nPort;
    private     int                             m_SocketType;
    private		Boolean				            m_bConnect = false;

    private     java.io.Closeable               m_Socket;
    private     TcpSocket                       m_pTcpSocket;
    private     UdpSocket                       m_pUdpSocket;
    private     RecvThread                      m_RecvThread;
    private     Handler                         m_Recvhandler = null;

    public BaseSocket(Handler handler, Context context, int nType) {
        this.m_Recvhandler  = handler;
        this.instance        = this;
        this.m_SocketType   = nType;

        switch (m_SocketType) {
            case SocketDefine.SOCKET_TCP:    m_pTcpSocket    = new TcpSocket(this);         break;
            case SocketDefine.SOCKET_UDP:    m_pUdpSocket   =  new UdpSocket(this);         break;
        }
    }

    public int                  GetSocketType()
    {
        return m_SocketType;
    }
    public String               GetIp()
    {
        return m_strIp;
    }
    public int                  GetPort()
    {
        return m_nPort;
    }
    public java.io.Closeable    GetSocket()
    {
        return m_Socket;
    }
    public Boolean              GetConnect() {
        return m_bConnect;
    }
    public Handler              GetRecvHandler()
    {
        return m_Recvhandler;
    }
    public void                 SetSocket(java.io.Closeable socket)
    {
        m_Socket = socket;
    }
    public void                 SetConnect(Boolean bConnect)
    {
        this.m_bConnect = bConnect;
    }
    public void                 SetRecvHandler(Handler Recvhandler)
    {
        m_Recvhandler = Recvhandler;
    }

    public void connect(String strIp, int nPort)
    {
        this.m_strIp        = strIp;
        this.m_nPort        = nPort;
        switch (m_SocketType) {
            case SocketDefine.SOCKET_TCP:        m_pTcpSocket.coonect();                                      break;
            case SocketDefine.SOCKET_UDP:        m_pUdpSocket.coonect();                                      break;
        }
    }

    public void close()
    {
        if(GetSocket() != null)
        {
            switch (GetSocketType()) {
                case SocketDefine.SOCKET_TCP:        m_pTcpSocket.close();                                    break;
                case SocketDefine.SOCKET_UDP:        m_pUdpSocket.close();                                    break;
            }
        }

        if(m_RecvThread != null)
            m_RecvThread.stopForever();
    }

    public void send(byte[] arrByte)
    {
        switch (GetSocketType()) {
            case SocketDefine.SOCKET_TCP:        m_pTcpSocket.send(arrByte);                                  break;
            case SocketDefine.SOCKET_UDP:        m_pUdpSocket.send(arrByte);                                  break;
        }
    }

    public void recv(Handler mRecvMesHandler)
    {
        m_RecvThread= new RecvThread(GetSocketType());
        m_RecvThread.setHandler(mRecvMesHandler);
        m_RecvThread.setSocket(this.GetSocket());
        m_RecvThread.start();
    }
}
