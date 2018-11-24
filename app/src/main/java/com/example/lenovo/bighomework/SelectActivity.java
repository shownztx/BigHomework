package com.example.lenovo.bighomework;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import org.litepal.LitePal;

import java.util.Calendar;

public class SelectActivity extends AppCompatActivity {
    private FloatingActionButton floatingButtonSave;
    private FloatingActionButton floatingButtonDlete;
    private EditText editTextName;
    private EditText editTextLocation;
    private EditText editTextDate;
    private EditText editTextTime;
    private Switch aSwitch;
    private String id;
    private static  int switchCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switchCheck=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        floatingButtonSave=findViewById(R.id.floatingActionButtonSave);
        floatingButtonDlete=findViewById(R.id.floatingActionButtonDelete);
        editTextName=findViewById(R.id.editTextName);
        editTextLocation=findViewById(R.id.editTextLocation);
        editTextDate=findViewById(R.id.editTextDate);
        editTextTime=findViewById(R.id.editTextTime);
        aSwitch=findViewById(R.id.switch1);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initData();//初始化控件内容

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() /*不点击不知道switchCheck是多少*/{
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchCheck=1;
                    Log.d("ZTXrefresh","isChecked"+switchCheck);
                }
                else{
                    switchCheck=0;
                    Log.d("ZTXrefresh","notChecked"+switchCheck);


                }
            }
        });

        floatingButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Thing thing=new Thing();
                thing.setName(String.valueOf(editTextName.getText()));
                thing.setLocation(String.valueOf(editTextLocation.getText()));
                thing.setDate(String.valueOf(editTextDate.getText()));
                thing.setTime(String.valueOf(editTextTime.getText()));
                //thing.save();
                thing.setAlarmOrNot(switchCheck);
                Log.d("ZTXrefresh","before_save_check "+thing.getAlarmOrNot());
                Log.d("ZTXrefresh","save_check "+switchCheck);

                thing.updateAll("id = ?", id);
                //onBackPressed();
                Log.d("ZTXrefresh","after_save_check "+thing.getAlarmOrNot());
                finish();
            }
        });
        floatingButtonDlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAll(Thing.class,"id = ?",id);
                Intent intent=new Intent();
                setResult(2,intent);
                finish();
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
    }


    public  void initData(){//初始化控件内容
        Intent intent=getIntent();
        String[]allData=new String[6];
        for (int i=0;i<6;i++){
            allData[i]=intent.getStringArrayExtra("allData")[i];
        }
        editTextName.setText(allData[0]);
        editTextLocation.setText(allData[1]);
        editTextDate.setText(allData[2]);
        editTextTime.setText(allData[3]);
        id=allData[4];
        if(Integer.parseInt(allData[5])==1){
            aSwitch.setChecked(true);
            switchCheck=1;
            Log.d("ZTXrefresh","read_true");
        }else{
            aSwitch.setChecked(false);
            switchCheck=0;
            Log.d("ZTXrefresh","read_false");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public  int saveSwitch(Thing thing){
        int alarm=thing.getAlarmOrNot();
        return alarm;
    }

    public  void showDateDialog(final EditText editTextDate)/*显示日期加载对话框*/{
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(SelectActivity.this,
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
        TimePickerDialog timePickerDialog=new TimePickerDialog(SelectActivity.this,
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
}
