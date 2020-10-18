package com.simplesocket.socket;

import android.os.Handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UdpSocket extends Thread {

    private DatagramSocket  m_socket = null;
    private InetAddress     m_serverAddr = null;
    private byte[]          m_data;
    private boolean         m_bExit;
    private     BaseSocket          m_BaseSocket;

    public UdpSocket(BaseSocket BaseSocket) {
        this.m_BaseSocket  = BaseSocket;
        if(open())
            start();
    }

    public DatagramSocket GetDataOutputStream() {
        return m_socket;
    }

    public void coonect() {
        try {
            m_serverAddr = InetAddress.getByName(m_BaseSocket.GetIp());
            if(m_serverAddr == null)
                m_BaseSocket.SetConnect(false);

            m_BaseSocket.SetConnect(true);
        } catch (IOException e) {
            m_BaseSocket.SetConnect(false);
        }
    }

    public boolean open() {

        try {
            if (m_socket == null)
                m_socket = new DatagramSocket();
        } catch (IOException e) {}

        if (m_socket != null)     return true;
        else                      return false;
    }

    public void close() {
        try {
            m_socket.close();
            m_socket = null;
            m_bExit =false;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void send(byte[] arrSendData) {
        m_data = arrSendData;

    }

    @Override
    public void run() {
        super.run();
        while (m_bExit) {

            if(m_data[0] == 0x00)
                continue;

            try {
                DatagramPacket Packet = new DatagramPacket(m_data, m_data.length, m_serverAddr, m_BaseSocket.GetPort());
                m_socket.send(Packet);

                Arrays.fill(m_data, (byte)0x00);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}