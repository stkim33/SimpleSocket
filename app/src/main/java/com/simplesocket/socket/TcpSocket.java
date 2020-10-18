package com.simplesocket.socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpSocket extends  Thread {
    private     final int 		    SOCKET_TIME_OVER 	= 10000;

    private     DataOutputStream    m_DataOutputStream  = null;
    private     DataInputStream     m_DataInputStream   = null;
    private     BaseSocket          m_BaseSocket;

    public TcpSocket(BaseSocket BaseSocket)
    {
        m_BaseSocket = BaseSocket;
        open();
    }

    public void coonect() {
        start();
    }

    public DataOutputStream GetDataOutputStream(){return m_DataOutputStream;}
    public DataInputStream GetDataInputStream() {return m_DataInputStream;}

    public boolean open()
    {
        Socket Socket = (Socket) m_BaseSocket.GetSocket();
        if (Socket == null) {
            Socket = new Socket();
            m_BaseSocket.SetSocket(Socket);
            Log.e("Socket Open", "new");
            return true;
        }
        else
            return false;
    }

    public void close() {
        try
        {
            if(m_DataInputStream != null) {
                m_DataInputStream.close();
                m_DataInputStream = null;
            }

            if(m_DataOutputStream != null) {
                m_DataOutputStream.close();
                m_DataOutputStream = null;
            }

            Socket Socket = (Socket) m_BaseSocket.GetSocket();
            if(Socket != null) {
                Socket.close();
                m_BaseSocket.SetSocket(null);
            }

            m_BaseSocket.SetConnect(false);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public boolean connect()
    {
        try
        {
            Socket Socket = (Socket) m_BaseSocket.GetSocket();
            if (Socket != null)
            {
                InetAddress serverAddr = InetAddress.getByName(m_BaseSocket.GetIp());
                Socket.connect(new InetSocketAddress(serverAddr, m_BaseSocket.GetPort()), SOCKET_TIME_OVER);
                m_BaseSocket.SetConnect(true);
                return true;
            }
        }
        catch(Exception e)
        {
            m_BaseSocket.SetSocket(null);
            m_BaseSocket.SetConnect(false);
            return false;
        }
        return false;
    }

    public Socket getSocket() throws IOException, InterruptedException {
        Socket Socket = null;
        if (connect()) {
            Socket = (Socket) m_BaseSocket.GetSocket();
            if (Socket != null) {
                Socket.getLocalAddress().toString();

                m_DataOutputStream = new DataOutputStream(new BufferedOutputStream(
                        Socket.getOutputStream()));

                m_DataInputStream = new DataInputStream(new BufferedInputStream(
                        Socket.getInputStream()));
            }
        }

        return Socket;
    }

    public void run() {
        Message socket_message = Message.obtain();
        try {

            if (getSocket() != null)
            {
                socket_message.obj = true;
            }
            else {
                socket_message.obj = false;
                Log.e("Socket connect fail !","fail");
            }
        } catch(Exception e) {
            socket_message.obj = false;
            //e.printStackTrace();
        }

        socket_message.what = SocketDefine.SOCKET_CONNECT_MSG;
        if(m_BaseSocket.GetRecvHandler() != null)
            m_BaseSocket.GetRecvHandler().sendMessage(socket_message);
    }

    public void send(byte[] arrSendData)
    {
        if(m_BaseSocket.GetConnect()) {
            if (m_DataOutputStream != null) {
                try {
                    m_DataOutputStream.write(arrSendData);
                    m_DataOutputStream.flush();
                    sleep(10);
                } catch (Exception e) {}
            }
        }
    }
}
