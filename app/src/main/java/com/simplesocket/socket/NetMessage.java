package com.simplesocket.socket;

import android.os.Handler;
import android.os.Message;

public class NetMessage extends Thread{

    static boolean SendMessage(Handler handler, int nWhat, Object obj) {
        if (nWhat < 0 || obj == null)
            return false;

        Message pMessage = null;
        pMessage = Message.obtain();

        if (pMessage != null) {
            pMessage.what = nWhat;
            pMessage.obj = obj;
            if (handler != null)
                handler.sendMessage(pMessage);
        } else
            return false;
        return true;
    }

    static boolean SsndEmptyMessage(Handler handler, int nWhat) {
        if (nWhat < 0)
            return false;

        Message pMessage = null;
        pMessage = Message.obtain();

        if (pMessage != null) {
            pMessage.what = nWhat;
            if (handler != null)
                handler.sendEmptyMessage(nWhat);
        } else
            return false;
        return true;
    }
}
