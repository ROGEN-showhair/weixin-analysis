package com.jd.analysis.data;

import com.jd.analysis.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;


/**
 * Created by xudi1 on 2017/3/13.
 */
public class UploadData {
    private static String local_location = "E:\\Java-Project\\weather-analysis\\input\\test\\maptemp";
    private static String upload_location = "hdfs://192.168.217.130:9000/user/hadoop/upload/data";
    public static void main(String[] args){
        upload();
    }

    public static void mains(String[] args) throws Exception {
        // 获取读取源文件和目标文件位置参数
        //String local = args[0];
        //String uri = args[1];
        String local = local_location;
        String uri = upload_location;

        OutputStream out = null;
        Configuration conf = new Configuration();
        try {
            // 获取读入文件数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(local)));
            String line = "";

            // 获取目标文件信息
            FileSystem fs = FileSystem.get(URI.create(uri), conf);
            out = fs.create(new Path(uri), new Progressable() {
                @Override
                public void progress() {
                    System.out.println("*");
                }
            });
            while((line = reader.readLine()) != null){
                line = line + "\n";
                out.write(line.toString().getBytes("UTF-8"));
            }
        } finally {
            //IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
    }

    public static void upload(){
        Configuration conf = new Configuration();
        OutputStream out = null;
        BufferedReader reader = null;
        try{
            FileSystem fs = FileSystem.get(URI.create(upload_location),conf);
            Path upload = new Path(upload_location);

            if(fs.exists(upload)){
                fs.delete(upload,true);
            }
            out = fs.create(upload);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(local_location)));
            String line = "";
            while((line = reader.readLine()) != null){
                line = line + "\n";
                out.write(line.toString().getBytes("UTF-8"));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(reader);
            IOUtils.closeStream(out);
        }
    }

    /** * 删除指定目录 * * @param conf * @param dirPath * @throws IOException */
    private static void deleteDir(Configuration conf, String dirPath) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        Path targetPath = new Path(dirPath);
        if (fs.exists(targetPath)) {
            boolean delResult = fs.delete(targetPath, true);
            if (delResult) {
                System.out.println(targetPath + " has been deleted sucessfullly.");
            } else {
                System.out.println(targetPath + " deletion failed.");
            }
        }
    }
}
