package com.example.lenovo.bighomework;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class thingAdapter extends RecyclerView.Adapter<thingAdapter.ViewHolder>{
    private List<Thing> showList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView location;
        private TextView date;
        private TextView time;
        private ImageView imageView;
        public ViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.name);
            location=view.findViewById(R.id.location);
            date=view.findViewById(R.id.date);
            time=view.findViewById(R.id.time);
            imageView=view.findViewById(R.id.image);
        }
    }
    public thingAdapter(List<Thing> List){
        showList=List;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                int position=holder.getAdapterPosition();
                Thing list=showList.get(position);
                /*Toast.makeText(v.getContext(),"name: "+list.getName()+"loaction: " +
                        list.getLocation()+"date: "+list.getDate()+"time: "
                        +list.getTime()+"id:"+list.getId(),Toast.LENGTH_SHORT).show();*/
                Intent intent=new Intent("SelectActivity");
                intent.putExtra("allData",DataSave(list));
                ((Activity) v.getContext()).startActivityForResult(intent,1);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Thing list=showList.get(position);

        viewHolder.name.setText(list.getName());
        viewHolder.location.setText(list.getLocation());
        viewHolder.date.setText(list.getDate());
        viewHolder.time.setText(list.getTime());
        Log.d("ZTXrefresh","getAlarmOrNot "+list.getAlarmOrNot());
        viewHolder.imageView.setImageResource(R.drawable.emptylogo);
        if (list.getAlarmOrNot()==1){
            Log.d("ZTXrefresh","alarmlogo");
            viewHolder.imageView.setImageResource(R.drawable.alarmlogo);
        }else {
            Log.d("ZTXrefresh","emptylogo");
            viewHolder.imageView.setImageResource(R.drawable.emptylogo);
        }
    }
    @Override
    public int getItemCount() {
        return showList.size();
    }

    public String[] DataSave(Thing list){
        String[] data=new String[6];
        data[0]=list.getName();
        data[1]=list.getLocation();
        data[2]=list.getDate();
        data[3]=list.getTime();
        data[4]=String.valueOf(list.getId());
        data[5]=String.valueOf(list.getAlarmOrNot());
        return  data;
    }
}
