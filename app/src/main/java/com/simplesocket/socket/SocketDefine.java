package com.simplesocket.socket;

public class SocketDefine {
    //ANDROID SOCKET TYPE
    public  static  final  int      SOCKET_NONE                          =   0x00;
    public  static  final  int      SOCKET_TCP                           =   0x01;
    public  static  final  int      SOCKET_UDP                           =   0x02;


    //ANDROID MSG TYPE
    public  static  final  int      SOCKET_CONNECT_MSG                  =   1004;
    public  static  final  int      SOCKET_FUNCITON_MSG                 =   1005;
    public  static  final  int      SOCKET_CLOSE_MGS                    =   -1;

    //ANDROID PACKET TYPE
    public  static  final  int      SOCKET_NOT_PROTOCAL                   =   0x00;
    public  static  final  int      SOCKET_PROTOCAL                       =   0x01;

    //Not Packet
    public byte[] m_bArrData;				                           //정적 데이터

    //Packet
    /*
    public byte[] bType = new byte[1];							        //종류
    public byte[] bLength = new byte[4];							    //길이
    public byte[] bData;                                                //데이터
    public byte[] bEtx= new byte[1];				                    //종료
    */
}
