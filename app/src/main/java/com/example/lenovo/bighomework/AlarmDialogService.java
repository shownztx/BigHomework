package com.example.lenovo.bighomework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;

public class AlarmDialogService extends Service {
    private static int flag;
    private String[]HourMinAlarmNameLoc;
    MediaPlayer alarmMusic;
    AlertDialog.Builder dialog=new AlertDialog.Builder(MyApplication.getContext());
    @Override
    public void onCreate(){
        Log.d("ZTXsetactivity","AlarmDialogService");
        AlarmDialogService.flag=0;
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        String[] data=intent.getStringArrayExtra("data");//获取时间闹钟名称地点
        HourMinAlarmNameLoc=data;

        Log.d("ZTXsetactivity","AlarmDialogService");

            alarmMusic = MediaPlayer.create(MyApplication.getContext(), R.raw.music);
            //alarmMusic.prepare();
            alarmMusic.setLooping(true);//循环
            if (AlarmDialogService.flag==0){
                alarmMusic.start();//播放闹钟
                AlarmDialogService.flag=1;
            }

        //dialog
        dialog.setTitle("日程提醒");
            if(!(HourMinAlarmNameLoc[3].isEmpty())&&!(HourMinAlarmNameLoc[4].isEmpty())){
                dialog.setMessage("事件："+HourMinAlarmNameLoc[3]+" 地点："+HourMinAlarmNameLoc[4]);
            }
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarmMusic.stop();
                AlarmDialogService.flag=0;
            }
        });
        /*dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarmMusic.stop();
            }
        });*/
        Dialog d = dialog.create();
        d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        d.setCanceledOnTouchOutside(false);//点击外面区域不会让dialog消失
        d.show();
        //————————————————————————————————————————————————————————————
       /* new Thread(new Runnable() {
            @Override
            public void run() {
            //Looper.prepare();
            //new Handler().post(runnable);//在子线程中直接去new 一个handler
            //Looper.loop();//这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI

                stopSelf();//结束
            }
        }).start();//开启*/
        //————————————————————————————————————————————————————————————
        return  super.onStartCommand(intent,flags,startId);
    }




    //以下是无用重写
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    //@androidx.annotation.Nullable
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
