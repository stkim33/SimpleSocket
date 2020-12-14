## SimpleSocket 

### Language : Java  ,  IDE : Android Studio
### targetSdkVersion : 29 , minSdkVersion : 21

#### Simple Socket Connection Object Init

```Java
   //Object
    public String               m_strIp             = "127.0.0.1";
    public int                  m_nPort             = 5000;
    public RecvSocketHandler    m_RecvHandler;
    public BaseSocket           m_SimpleSocket;
```

####  Simple Socket Connection Object Function Call
```Java
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
```
####  Simple Socket Send MsgFunc
```Java
   public void SendMsg(int nSockcode, String SockValue)
    {
        try {
            // Socket Coonect Chk
            if (m_SimpleSocket.GetConnect() == true) {
                // socket Obj Get
                TcpSsocketocket pSocket = (TcpSocket) m_SimpleSocket.GetSocket();
                
                // socket Protocal Code
                if(nSockcode != SocketDefine.SOCKET_NOT_PROTOCAL )
                    pSocket.GetDataOutputStream().writeInt(nSockcode);
                
                // socket Value
                pSocket.GetDataOutputStream().writeInt(SockValue.length());
                
                // Send
                pSocket.send(SockValue.getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
```
 
####  Simple Socket Recv MsgFunc
```Java
    public void run(){
        //Recv Thread Loop
        switch (m_BaseSocket.GetSocketType())
        {
            case SocketDefine.SOCKET_TCP:   TcpRecv();          break;
            case SocketDefine.SOCKET_UDP:   UdpRecv();          break;
        }
    }
```
