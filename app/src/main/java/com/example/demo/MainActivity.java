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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    List<News> mNewsList = new ArrayList<>();

    SensorManager mSensorManager;
    Sensor mAccelerometer;
    SensorEventListener eventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.i("jeff","onSensorChanged event="+event.values.toString());
            switch (event.sensor.getType()){
                case TYPE_ACCELEROMETER:
                    mNewsList.get(0).setContent(event.values[0]);
                    mNewsList.get(1).setContent(event.values[1]);
                    mNewsList.get(2).setContent(event.values[2]);
                    break;
            }
            mMyAdapter.notifyItemRangeChanged(0, 8);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i("jeff","onAccuracyChanged sensor="+sensor.getName()+",accuracy="+accuracy);
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

        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
    }
    protected void onResume() {
        super.onResume();
        Sensor mSensor = mSensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        mSensorManager.registerListener(eventListener,mSensor,SensorManager.SENSOR_DELAY_GAME);
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

