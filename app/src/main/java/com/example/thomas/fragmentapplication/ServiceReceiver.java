package com.example.thomas.fragmentapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.thomas.services.MyService;

public class ServiceReceiver extends BroadcastReceiver {
    public ServiceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyService.class));
        Toast.makeText(context, "Service started", Toast.LENGTH_LONG).show();
    }
}
