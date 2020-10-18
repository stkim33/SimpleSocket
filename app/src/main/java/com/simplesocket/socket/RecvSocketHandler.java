package com.simplesocket.socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class RecvSocketHandler extends Handler {

    BaseSocket  m_pSocket = null;
    Context     m_Context = null;

    public RecvSocketHandler(BaseSocket Socket, Context context)
    {
        m_pSocket = Socket;
        m_Context = context;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what)
        {
            case SocketDefine.SOCKET_CONNECT_MSG  :    SocketConnectMsg(msg);  break;
            case SocketDefine.SOCKET_CLOSE_MGS    :    SocketCloseMsg(msg);  break;
            case SocketDefine.SOCKET_FUNCITON_MSG :    SocketFucntionMsg(msg);  break;
        }

    }

    //소켓 연결 관련 메시지
    public void SocketConnectMsg(Message msg)
    {
        if(msg.what == SocketDefine.SOCKET_CONNECT_MSG)
        {
            //소켓 접속 확인
            if((Boolean) msg.obj)
            {
                Toast.makeText(m_Context, "Coonect : TRUE", Toast.LENGTH_LONG).show();
                m_pSocket.recv(this);
            }
            else
            {
                Toast.makeText(m_Context, "Coonect : False", Toast.LENGTH_LONG).show();
            }
        }
    }

    //소켓 종료 관련 메시지
    public void SocketCloseMsg(Message msg)
    {
        if(m_pSocket.GetConnect())
            m_pSocket.close();

        Toast.makeText(m_Context, "서버와 연결이 종료되었습니다. 다시 실행주세요.", Toast.LENGTH_LONG).show();
    }



    //소켓 기능 관련 메시지
    public void SocketFucntionMsg(Message msg)
    {
        String strMsgValue = (String)msg.obj;
        Toast.makeText(m_Context, strMsgValue, Toast.LENGTH_LONG).show();
    }
}


