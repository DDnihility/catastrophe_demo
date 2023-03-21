package com.example.demo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class DataSave {

    private static String mPath = "storage/emulated/0/Android/data/com.example.demo/files/";
    private static String mFileName = "data.csv";
    private static String mFile = mPath + mFileName;
    private static FileWriter mFileWriter;
    private static File mFileDir = new File(mPath);


    public static void saveData(long timestamp, float accX, float accY, float accZ, float gyrX, float gyrY, float gyrZ) {
        try {
            if (!mFileDir.exists()) {
                mFileDir.mkdirs();
            }
            mFileWriter = new FileWriter(mFile, true);
            mFileWriter.append(timestamp + "," + accX + "," + accY + "," + accZ + "," + gyrX + "," + gyrY + "," + gyrZ + " ");
            mFileWriter.flush();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
