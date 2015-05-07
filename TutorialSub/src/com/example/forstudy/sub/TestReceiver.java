package com.example.forstudy.sub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// Should enroll receiver at Manifest file
public class TestReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
         
        if (name.equals("com.example.forstudy.main.sendreceiver")){
            Toast.makeText(context, "Received Broadcast", Toast.LENGTH_SHORT).show();
        }
    }
}