package com.jd.analysis.data;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xudi1 on 2017/3/7.
 */
public class ProduceData {
    private static String temp_data_location = "E:\\Java-Project\\weixin-analysis\\data\\chat_temp";
    private static String data_location = "E:\\Java-Project\\weixin-analysis\\data\\chatparsing";
    private static String begin_time = "2017-03-20 00:00:00";
    private static String end_time = "2017-03-26 00:00:00";
    private static int[] duration_time ={
            100,200,3000,1234,3456,6754,1245,9876,3321,1232,
            4786,6723,8537};
    private static String[] Place = {
            "大雁塔","兵马俑","西安邮电大学","西安市政法大学","陕西师范大学","城南新天地","西北政法大学","航天城地铁站",
            "樱花广场","智慧城","西北大学","西安电子科技大学","西安外国语大学"};
    private static String[] Chat = {
        "钢铁侠3好看吗","雷神好阿斯顿发","绿巨人sadfa","金刚狼阿斯顿发阿斯顿发","蜘蛛侠拉科技",
            "蝙蝠侠拉科技","超人黄金时代","死侍34523","神奇女侠流量卡历历可见",
            "美国队长埃里克森脚离开","神奇四侠lkjlkajsdf","加勒比海盗拉开了空间",
            "奇异博士福利科技离开极乐空间"};
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
            for(int i = 0; i < 1000; i++){
                String begintime = Begin_time();
                String endtime = End_time(begintime,duration_time[radom_select(12,0)]);
                String place = Place[radom_select(12,0)];
                String chat = Chat[radom_select(12,0)];
                stringBuffer.append("{" + ID() + "," + ID() + "}\t" + begintime + "\t" + endtime
                        + "\t" + place + "\t" + chat  ).append("\r\n");
                outputStream.write(stringBuffer.toString().getBytes("utf-8"));
            }
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
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
    public static int ID() {
        int max=10020;
        int min=10000;
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

    /**
     * 生成聊天结束时间
     * @throws ParseException
     */
    public static String End_time(String begin_time,int num) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date  currdate = format.parse(begin_time);
        //System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.SECOND, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        //System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }
    /*
    随机生成聊天开始时间
     */
    public static String Begin_time(){
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(begin_time);//构造开始日期
            Date end = format.parse(end_time);//构造结束日期
            //getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if(start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(),end.getTime());
            String result = format.format(date);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        //如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }

}
