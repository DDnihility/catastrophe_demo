package com.example.demo;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GRAVITY;
import static android.hardware.Sensor.TYPE_GYROSCOPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    List<News> mNewsList = new ArrayList<>();
    float ACC_X,ACC_Y,ACC_Z,GYR_X,GYR_Y,GYR_Z;
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    DataSave dataSave = new DataSave();
    String filename;
    SensorEventListener eventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //每0.02s获取传感器加速度与角速度读数与时间戳，保存为csv文件
            switch (event.sensor.getType()){
                case TYPE_ACCELEROMETER:
                    ACC_X=event.values[0];
                    ACC_Y=event.values[1];
                    ACC_Z=event.values[2];
                    break;
                case TYPE_GYROSCOPE:
                    GYR_X=event.values[0];
                    GYR_Y=event.values[1];
                    GYR_Z=event.values[2];
                break;
            }
            mNewsList.get(0).setContent(ACC_X);
            mNewsList.get(1).setContent(ACC_Y);
            mNewsList.get(2).setContent(ACC_Z);
            mNewsList.get(3).setContent(GYR_X);
            mNewsList.get(4).setContent(GYR_Y);
            mNewsList.get(5).setContent(GYR_Z);
            mMyAdapter.notifyItemRangeChanged(0, 6);
            dataSave.saveData(event.timestamp,ACC_X,ACC_Y,ACC_Z,GYR_X,GYR_Y,GYR_Z,filename);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化传感器
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview);
        // 构造一些数据
        mMyAdapter = new MyAdapter();
        mNewsList.add(new News("X方向加速度：", 0));
        mNewsList.add(new News("Y方向加速度：", 0));
        mNewsList.add(new News("z方向加速度：", 0));
        mNewsList.add(new News("X方向角速度：", 0));
        mNewsList.add(new News("Y方向角速度：", 0));
        mNewsList.add(new News("z方向角速度：", 0));
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        Button buttonStart =  findViewById(R.id.button);
        Button buttonEnd = findViewById(R.id.button2);
        buttonStart.setEnabled(true);
        buttonEnd.setEnabled(false);   
        //按钮点击事件，开始时注册传感器监听器，结束时取消注册
        buttonStart.setOnClickListener(view -> {
            Sensor mSensor = mSensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
            mSensorManager.registerListener(eventListener,mSensor,SensorManager.SENSOR_DELAY_GAME);
            mSensor = mSensorManager.getDefaultSensor(TYPE_GYROSCOPE);
            mSensorManager.registerListener(eventListener,mSensor,SensorManager.SENSOR_DELAY_GAME);
            //记录当前时间生成新的文件名供保存数据使用
            filename = "data"+System.currentTimeMillis();
            //按下后按钮变灰，防止重复点击；结束按钮可点击
            buttonStart.setEnabled(false);
            buttonEnd.setEnabled(true);
        });
        buttonEnd.setOnClickListener(view -> {
            mSensorManager.unregisterListener(eventListener);
            buttonStart.setEnabled(true);
            buttonEnd.setEnabled(false);   
        });
    }
    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(eventListener);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.item_list, null);
            return new MyViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.mTitleTv.setText(news.title);
            holder.mTitleContent.setText(Float.toString(news.content));
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        TextView mTitleContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView);
            mTitleContent = itemView.findViewById(R.id.textView2);
        }
    }
}

