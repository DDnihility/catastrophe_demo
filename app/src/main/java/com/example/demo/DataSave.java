package com.example.demo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class DataSave {
    private static String mPath = "Downloads/data/";
    private static FileWriter mFileWriter;
    private static File mFileDir = new File(mPath);
    //使用系统的文件选择器SAF在公共目录Download下读写文件
    public static void saveData(long timestamp, float accX, float accY, float accZ, float gyrX, float gyrY, float gyrZ,String mFileName) {
        try {
            if (!mFileDir.exists()) {
                mFileDir.mkdirs();
                System.out.println("文件创建成功");
            }
            mFileWriter = new FileWriter( mPath + mFileName, true);
            mFileWriter.append(timestamp + "," + accX + "," + accY + "," + accZ + "," + gyrX + "," + gyrY + "," + gyrZ + " ");
            mFileWriter.flush();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
