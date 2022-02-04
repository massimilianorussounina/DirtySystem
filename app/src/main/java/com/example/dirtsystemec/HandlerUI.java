package com.example.dirtsystemec;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class HandlerUI extends Handler {
    private MainActivity uiActivity;
    public HandlerUI (MainActivity uiActivity){
        this.uiActivity=uiActivity;
    }
    @Override
    public void handleMessage(Message msg) {
        if(msg.what == 0){

            uiActivity.showMenu(true);
        } else {

            uiActivity.showMenu(false);

        }
    }


}
