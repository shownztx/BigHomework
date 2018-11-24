package com.example.lenovo.bighomework;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import java.util.Calendar;

public class NewActivity extends AppCompatActivity {
    private  int count;
    private FloatingActionButton floatingActionButton;
    private EditText editTextName;
    private EditText editTextLocation;
    private EditText editTextDate;
    private EditText editTextTime;
    private Switch aSwitch;
    private static int AlarmOrNot;
    private static String [] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        count=1;//初始化count Id
        AlarmOrNot=0;
        floatingActionButton=findViewById(R.id.floatingActionButton);
        editTextName=findViewById(R.id.editTextName);
        editTextLocation=findViewById(R.id.editTextLocation);
        editTextDate=findViewById(R.id.editTextDate);
        editTextTime=findViewById(R.id.editTextTime);
        aSwitch=findViewById(R.id.switch1);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AlarmOrNot=1;
                    //Log.d("ZTXalarm",String.valueOf(AlarmOrNot));
                }
                else{
                    AlarmOrNot=0;
                }
            }
        });
        editTextDate.setOnTouchListener(new View.OnTouchListener() {//日期输入框点击事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    showDateDialog(editTextDate);
                    return true;
                }
                return false;
            }
        });
        editTextTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    showTimeDialog(editTextTime);
                }
            return false;
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {//写入日程的点击事件
            @Override
            public void onClick(View v) {
                Thing thing=new Thing();
                thing.setId(count++);

                Log.d("ZTXrefresh","new_count:"+thing.getId());
                thing.setName(String.valueOf(editTextName.getText()));
                thing.setLocation(String.valueOf(editTextLocation.getText()));
                thing.setDate(String.valueOf(editTextDate.getText()));
                thing.setTime(String.valueOf(editTextTime.getText()));
                thing.setAlarmOrNot(AlarmOrNot);
                thing.save();
                getTime(String.valueOf(editTextName.getText()),
                        String.valueOf(editTextLocation.getText()),
                        String.valueOf(editTextTime.getText()),
                        String.valueOf(editTextDate.getText()),
                        String.valueOf(count-1)
                );//保存时间数据用于提醒
                Intent intent=new Intent();
                intent.putExtra("time",data);
                setResult(1,intent);
                finish();
            }
        });
    }

    public  void showDateDialog(final EditText editTextDate)/*显示日期加载对话框*/{
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(NewActivity.this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                editTextDate.setText(year+"-"+month+"-"+dayOfMonth);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        datePickerDialog.show();
    }

    public void showTimeDialog(final  EditText editTextTime)/*显示时间加载对话框*/{
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(NewActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourString=String.valueOf(hourOfDay);
                String minuteString=String.valueOf(minute);
                if(hourString.length()==1){
                    hourString="0"+hourString;
                }
                if(minuteString.length()==1){
                    minuteString="0"+minuteString;
                }
                //alarm
                //alarm(hourOfDay,minute);
                editTextTime.setText(hourString+":"+minuteString);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }

   public void getTime(String name, String location , String time,String date,String id){ //保存时间数据
        data=new String[8];
        String []HourMinute={"",""};
       if(!time.isEmpty()){
           HourMinute=time.split(":");
       }
       String []MonthDay={"","",""};
       if(!date.isEmpty()){
           MonthDay=date.split("-");
       }
       Log.d("ZTXdate",MonthDay[0]+"-"+MonthDay[1]+"-"+MonthDay[2]);
        data[0]=HourMinute[0];//小时
        data[1]=HourMinute[1];//分钟
        data[2]=String.valueOf(AlarmOrNot);//是否提醒
        data[3]=name;//名称
        data[4]=location;//地点
        data[5]=MonthDay[1];//月份
        data[6]=MonthDay[2];//日期
        data[7]=id;
        AlarmOrNot=0;//还原

    }

}
