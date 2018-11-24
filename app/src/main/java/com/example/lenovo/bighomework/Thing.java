package com.example.lenovo.bighomework;

import org.litepal.crud.LitePalSupport;

public class Thing extends LitePalSupport {
    private String name;
    private String date;
    private String time;
    private String location;
    private int id;
    //private int AlarmOrNot;
    private String AlarmOrNot;

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
    public String[] getAll(){
        String [] allData=new String[5];
        allData[0]=name;
        allData[1]=location;
        allData[2]=date;
        allData[3]=time;
        allData[4]=String.valueOf(id);
        return allData;
    }

    public String[] getAll_2(){
        String [] data=new String[8];
        if (!(time.isEmpty())&&!(date.isEmpty())){
            data[0]=time.split(":")[0];//小时
            data[1]=time.split(":")[1];//分钟
            data[2]=AlarmOrNot;//是否提醒
            data[3]=name;//名称
            data[4]=location;//地点
            data[5]=date.split("-")[0];//月份
            data[6]=date.split("-")[1];//日期
            data[7]=String.valueOf(id);
        }
        return  data;
    }

    //public void setAlarmOrNot(int alarmOrNot) { AlarmOrNot = alarmOrNot; }
    public void setAlarmOrNot(int alarmOrNot) { AlarmOrNot = String.valueOf(alarmOrNot); }

    //public int getAlarmOrNot() { return AlarmOrNot; }
    public int getAlarmOrNot() { return Integer.parseInt(AlarmOrNot); }

    public void setId(int id) { this.id = id; }

    public  int  getId() { return id; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
