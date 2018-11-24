package com.example.lenovo.bighomework;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    public RecyclerView recyclerView;
    private List<Thing> ToDoList = new ArrayList<>();//容纳并显示日程信息
    private static String[]HourMinAlarmNameLocDate;//接受日期，时间等数据
    private static int AlarmNum;//闹钟的个数
    private static int lastID;//最后一个闹钟id
    //private AlarmManager alarmManager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("ZTXrefresh", "onCreate" );
        HourMinAlarmNameLocDate=new String[8];
        for (int i=0;i<8;i++){
            HourMinAlarmNameLocDate[i]="";
        }
        AlarmNum=0;
        lastID=1;
        /*ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                1);*/
        super.onCreate(savedInstanceState);

        LitePal.getDatabase();//建立数据库
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyleview);//控件赋值
        floatingActionButton = findViewById(R.id.floatingActionButton);
        Log.d("ZTXcreate","onCreate");
        initAdapter();//初始化容器
        initData();//初始化数据

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//浮动按钮的点击事件
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(intent,1);//跳转到新建活动
            }
        });
    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu)/*选择menu布局*/{
         getMenuInflater().inflate(R.menu.toolbar,menu);
         return  true;
      }

     @Override
     public boolean onOptionsItemSelected(MenuItem item)/*menu点击事件*/ {
        switch (item.getItemId()){
            case R.id.delete:
                LitePal.deleteAll(Thing.class);
                initEmpty();
                break;
            default:
        }
          return true;
      }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data)/*返回activity执行*/{
        switch (requestCode){
            case 1:
                initBackData();/*清除所有再重新读取*/
                if (resultCode==1)/*新建返回*/{
                    Log.d("ZTXresult","from NEW");
                    HourMinAlarmNameLocDate=data.getStringArrayExtra("time");//保存新建的时间
                    AlarmNum=Integer.parseInt(HourMinAlarmNameLocDate[7]);//赋值，实现count的增加
                   if (HourMinAlarmNameLocDate[2].equals("1")){
                       SetAlarm(HourMinAlarmNameLocDate);//新建定时器
                    }
                }

                if (resultCode==2)/*删除返回*/{
                    Log.d("ZTXresult","from DELETE");
                    DeleteAlarm(HourMinAlarmNameLocDate[7]);//删除定时器
                    Log.d("ZTXrefresh", "DDDDDDDDDDDDDDDDDDDeleteAlarm:" + HourMinAlarmNameLocDate[7]);
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
    *   data[0]=HourMinute[0];//小时
        data[1]=HourMinute[1];//分钟
        data[2]=String.valueOf(AlarmOrNot);//是否提醒
        data[3]=name;//名称
        data[4]=location;//地点
        data[5]=MonthDay[1];//月份
        data[6]=MonthDay[2];//日期
        data[7]=id;
        AlarmOrNot=0;//还原*/

    public void initData() {//从数据库加载日程
        List<Thing> thingList = LitePal.findAll(Thing.class);
        Log.d("ZTXrefresh", "initData" );

        for (Thing thing : thingList) {
            ToDoList.add(thing);
        }
    }

    public  void initAdapter(){
        Log.d("ZTXrefresh", "initAdapter" );
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyleview);//显示日程
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        thingAdapter adapter = new thingAdapter(ToDoList);
        recyclerView.setAdapter(adapter);
    }

    public void initBackData()/*清除所有再重新读取*/ {
        Log.d("ZTXrefresh","initBackDataBBBBBBBBBBBBBBBBBBBBBBBBBB");
        List<Thing> thingList = LitePal.findAll(Thing.class);
        ToDoList.clear();
        DeleteAllAlarm(ToDoList.size());
        for (Thing thing : thingList) {
            ToDoList.add(thing);
            Log.d("ZTXrefresh","===AlarmOrNot"+thing.getAlarmOrNot());

            if(thing.getAlarmOrNot()==0){
                Log.d("ZTXrefresh","DeleteAlarm");
                //DeleteAlarm(thing.getAll_2());
                DeleteAlarm(HourMinAlarmNameLocDate[7]);
                Log.d("ZTXrefresh", "DDDDDDDDDDDDDDDDDDDeleteAlarm:" + HourMinAlarmNameLocDate[7]);
            }
        }
        initAdapter();
    }


    public  void initEmpty()/*在清空时 清空ArrayList以实现刷新界面*/{
        Log.d("ZTXrefresh", "initEmpty" );
        ToDoList.clear();
        DeleteAllAlarm(ToDoList.size());
        thingAdapter adapter = new thingAdapter(ToDoList);
        recyclerView.setAdapter(adapter);
        Log.d("ZTXom","om null");
        if(!(HourMinAlarmNameLocDate[7].isEmpty())){
            Log.d("ZTXdelete","lastid="+HourMinAlarmNameLocDate[7]);
            DeleteAllAlarm(Integer.parseInt(HourMinAlarmNameLocDate[7]));
        }
    }
    public void SetAlarm(String[] HourMinAlarmNameLocDate){
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("data",HourMinAlarmNameLocDate);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
                Integer.parseInt(HourMinAlarmNameLocDate[7]),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("ZTXrefresh","SSSSSSSSSSSSSSSSSSetAlarm:"+HourMinAlarmNameLocDate[7]);
        if(!(HourMinAlarmNameLocDate[0].isEmpty())&&
                !(HourMinAlarmNameLocDate[5].isEmpty()))/*防止不输入时间*/{
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            //c.set(Calendar.MONTH,Integer.parseInt(HourMinAlarmNameLocDate[5]));
            //c.set(Calendar.DATE,Integer.parseInt(HourMinAlarmNameLocDate[6]));
            c.set(Calendar.HOUR, Integer.parseInt(HourMinAlarmNameLocDate[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(HourMinAlarmNameLocDate[1]));
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            //alarmMgr.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+3000, pi);

            }
    }
/*
    public void DeleteAlarm(String[] HourMinAlarmNameLocDate){

            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            //intent.putExtra("data",HourMinAlarmNameLocDate);
            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
                    Integer.parseInt(HourMinAlarmNameLocDate[7]), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(pi);
            Log.d("ZTXrefresh", "DDDDDDDDDDDDDDDDDDDeleteAlarm:" + HourMinAlarmNameLocDate[7]);

    }
 */
public void DeleteAlarm(String id){

    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
            Integer.parseInt(id),intent,PendingIntent.FLAG_UPDATE_CURRENT);
    alarmMgr.cancel(pi);
    /*Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
    //intent.putExtra("data",HourMinAlarmNameLocDate);
    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
            Integer.parseInt(id), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    alarmMgr.cancel(pi);*/
    Log.d("ZTXrefresh", "DDDDDDDDDDDDDDDDDDDeleteAlarm:" + id);
}


    public void DeleteAllAlarm(int AlarmNum){
        Log.d("ZTXdelete","num="+AlarmNum);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        for (int i=lastID;i<=AlarmNum;i++){
            Log.d("ZTXdelete","i="+i+"");
            PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
                    i,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(pi);
        }
        lastID=AlarmNum;
    }
}


