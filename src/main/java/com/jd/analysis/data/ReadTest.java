package com.jd.analysis.data;

import com.jd.analysis.bean.WeixinUserBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by xudi1 on 2017/3/28.
 */
public class ReadTest {
    public static void main(String[] args) throws IOException {
        read();
    }
    public static void read() throws IOException {
        FileSystem fs = FileSystem.get(new Configuration());
        //读取数据库的微信人物信息
        Path path = new Path("hdfs://192.168.217.130:9000/user/hadoop/weixinuser/user");
        FSDataInputStream fsdis = fs.open(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fsdis,"UTF-8"));
        String line = null;
        while((line = br.readLine()) != null){
            System.out.println(line);
            String[] array = line.split("\t");
            try{
                System.out.println(array.length);
                if(array.length == 6){
                    WeixinUserBean wub = new WeixinUserBean();
                    wub.setId(Integer.valueOf(array[0]));
                    wub.setName(array[1]);
                    wub.setAge(Integer.valueOf(array[2]));
                    wub.setSex(array[3]);
                    wub.setVocation(array[4]);
                    wub.setFriends(array[5]);
                    System.out.println(wub.toString());

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
