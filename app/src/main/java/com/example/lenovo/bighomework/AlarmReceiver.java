package com.example.lenovo.bighomework;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ZTXsetactivity", "AlarmReceiver");
        Intent intentService=new Intent(MyApplication.getContext(),AlarmDialogService.class);
        intentService.putExtra("data",intent.getStringArrayExtra("data"));
        MyApplication.getContext().startService(intentService);

        }
    }





