package com.simplesocket.socket;

import android.os.Handler;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;


public class RecvThread extends Thread {

    private int                         m_nSocketType;
    private Handler                     m_Handler = null;
    private boolean                     isRun = true;
    private BaseSocket                  m_BaseSocket    = null;
    private DataInputStream             m_TcpInputStream  = null;
    private DatagramSocket              m_UdpInputStream  = null;
    private SocketDefine                m_SocketProtocal = null;


    public RecvThread(int type) {
        m_nSocketType = type;
        m_SocketProtocal = new SocketDefine();
    }


    public void setHandler(Handler handler) { this.m_Handler = handler;}

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void setSocket(Object socket)
    {
        m_BaseSocket = (BaseSocket)socket;
    }

    public void run(){
        //Recv Thread Loop
        switch (m_BaseSocket.GetSocketType())
        {
            case SocketDefine.SOCKET_TCP:   TcpRecv();          break;
            case SocketDefine.SOCKET_UDP:   UdpRecv();          break;
        }
    }

    public void TcpRecv()
    {
        while(isRun) {
            try {
                if (m_BaseSocket.GetConnect() == true) {
                    TcpSocket pSocket = (TcpSocket) m_BaseSocket.GetSocket();

                    //순간 null일 때를  체크
                    if (pSocket.GetDataInputStream() == null) continue;
                    else m_TcpInputStream = pSocket.GetDataInputStream();

                    int nLen = m_TcpInputStream.read(m_SocketProtocal.m_bArrData, 0, m_SocketProtocal.m_bArrData.length);

                    if (nLen <= m_SocketProtocal.SOCKET_CLOSE_MGS) {
                        NetMessage.SendMessage(m_Handler, m_SocketProtocal.SOCKET_CLOSE_MGS, true);
                        break;
                    }
                    //Android to Window
                    //String pValue = new String(ComoonFunction.Windows2Android(m_SocketProtocal.m_bArrData));

                    NetMessage.SendMessage(m_Handler, SocketDefine.SOCKET_FUNCITON_MSG, m_SocketProtocal.m_bArrData);

                    //Protocal Packet Data
                    //if (DataRead(m_Protocal.m_nDataLength))
                    // NetMessage.Message(m_Socket.GetHandler(), SocketDefine.SOCKET_FUNCITON_MSG, m_Protocal.m_bArrHeadData[2], m_Protocal.m_bArrMainData);

                }
            }catch(Exception e){ }
        }
    }

    public void UdpRecv()
    {
        while(isRun) {
            try {
                if (m_BaseSocket.GetConnect() == true) {
                    UdpSocket pSocket = (UdpSocket) m_BaseSocket.GetSocket();

                    if (pSocket.GetDataOutputStream() == null)
                        m_UdpInputStream = pSocket.GetDataOutputStream();

                    DatagramPacket pPacket = new DatagramPacket(m_SocketProtocal.m_bArrData, m_SocketProtocal.m_bArrData.length);
                    m_UdpInputStream.receive(pPacket);

                    if (pPacket.getLength() <= SocketDefine.SOCKET_CLOSE_MGS) {
                        NetMessage.SendMessage(m_Handler, m_SocketProtocal.SOCKET_CLOSE_MGS, true);
                        break;
                    }

                    NetMessage.SendMessage(m_Handler, SocketDefine.SOCKET_FUNCITON_MSG, m_SocketProtocal.m_bArrData);
                }

            }catch(Exception e){ }
        }
    }

    public boolean DataRead(SocketDefine messge, int nSize)
    {
        byte[]  dataTemp = new byte[4096];
        int nReciveCount = 0;

        while (nReciveCount != nSize) {
            int nRecv = 0;
            try {

                switch (m_BaseSocket.GetSocketType())
                {
                    case SocketDefine.SOCKET_UDP:
                        DatagramPacket pPacket = new DatagramPacket(dataTemp, nSize- nReciveCount >= 4096 ? 4096 : nSize - nReciveCount);
                        m_UdpInputStream.receive(pPacket);
                        nRecv = pPacket.getLength();
                        break;

                    case SocketDefine.SOCKET_TCP:
                        nRecv = m_TcpInputStream.read(dataTemp, 0, nSize- nReciveCount >= 4096 ?  4096 : nSize- nReciveCount);
                        break;
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.arraycopy(dataTemp, 0, messge.m_bArrData, nReciveCount,nRecv);
            nReciveCount += nRecv;
        }

        Arrays.fill(dataTemp, (byte) 0);
        if(nReciveCount == nSize)
            return true;
        else
            return false;
    }

}
