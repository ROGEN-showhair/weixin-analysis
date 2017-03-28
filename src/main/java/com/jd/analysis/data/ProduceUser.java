package com.jd.analysis.data;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xudi1 on 2017/3/28.
 */
public class ProduceUser {
    private static String temp_data_location = "E:\\Java-Project\\weixin-analysis\\data\\user_temp";
    private static String data_location = "E:\\Java-Project\\weixin-analysis\\data\\user";
    private static String begin_time = "2017-03-20 00:00:00";
    private static String end_time = "2017-03-26 00:00:00";
    private static String[] names = { "赵一","钱二","孙三","李四","周五",
            "吴六","郑七","王八","冯九","陈十","褚一","卫二","蒋三","沈四","韩五",
    "杨六","朱七","秦八","尤九","许十"};
    private static String[] ID = {
            "10001","10002","10003","10004","10005",
            "10006","10007","10008","10009","10010","10011","10012","10013","10014","10015",
            "10016","10017","10018","10019","10020"
    };
    private static String[] gender = {"男","女","男","女","男","女","男","女","男","女"};
    private static String[] vocations = {
      "餐饮","教育","金融","律师","娱乐","军人","体育","建筑","无业"};


    public static void main(String[] args){
        OutputTemp(temp_data_location);
        RemoveRepetition(temp_data_location,data_location);


    }

    /**
     * 随机产生气象数据，写入temp文件
     * @param tempPath
     */
    public static void OutputTemp(String tempPath){
        try {
            File file = new File(tempPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file, true);
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0; i < 19; i++){
                int age = radom_select(60,15);
                String sex = gender[radom_select(9,0)];
                String vocation = vocations[radom_select(8,0)];
                stringBuffer.append(ID[i] + "\t"+names[i]+"\t"+age+"\t"+sex+"\t"
                +vocation+"\t"+ID()+","+ID()+","+ID()+","+ID()+","+ID()).append("\r\n");
                outputStream.write(stringBuffer.toString().getBytes("utf-8"));
            }
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 对temp文件中的数据去重，存入data
     * @param tempPath
     * @param path
     */
    public static void RemoveRepetition(String tempPath,String path){
        Set<String> values = new LinkedHashSet<String>();
        try{
            /*
            生成一个空文件
             */
            File file = new File(path);
            if( !file.exists()){
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file,true);

            /*
            读取有重复的临时数据
             */
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempPath)));
            String value = "";
            while((value = reader.readLine()) != null){
                values.add(value);
            }

            for(Object string : values){
                System.out.println(string);
                string = string + "\n";
                outputStream.write(string.toString().getBytes("utf-8"));
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /**
     * 随机数，生成聊天ID
     * @return
     */
    public static int age() {
        int max=15;
        int min=60;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
    public static int ID() {
        int max=10020;
        int min=10001;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
    /*
    随机数，用来从持续时间和地点中选一个值
     */
    public static int radom_select(int max,int min) {
        //int max=12;
        //int min=0;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }



}
