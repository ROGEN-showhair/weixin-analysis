package com.jd.analysis.data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;

/**
 * Created by xudi1 on 2017/3/13.
 */
public class DownloadData {
    private static String local_location = "E:\\Java-Project\\weather-analysis\\input\\test\\download";
    private static String download_location = "hdfs://192.168.217.130:9000/user/hadoop/upload/data";

    public static void main(String[] args){
        Download(local_location,download_location);
    }
    public static void Download(String localLocation,String downloadLocation){
        FileOutputStream out = null;
        FSDataInputStream in = null;
        try{
            /*
            file文件写入
             */
            File file = new File(localLocation);
            if(!file.exists()){
                file.createNewFile();
            }
            out = new FileOutputStream(file,false);
            /*
            hdfs读取
             */
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(downloadLocation),conf);
            Path hdfs = new Path(downloadLocation);
            if(fs.exists(hdfs)){
                in = fs.open(hdfs);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                String line = null;
                while((line = reader.readLine()) != null){
                    line = line + "\n";
                    out.write(line.toString().getBytes("UTF-8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }


    }
}
