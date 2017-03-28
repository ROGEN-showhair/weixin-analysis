package com.jd.analysis.mapreduce;

import com.jd.analysis.bean.WeixinParsingBean;
import com.jd.analysis.bean.WeixinUserBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xudi1 on 2017/3/24.
 */
public class WeixinMapper extends Mapper<LongWritable,Text,Text,Text> {
    //private static final String uploadparsing = "hdfs://192.168.217.130:9000/user/hadoop/weixin/uploadparsing";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static final byte[] lock = new byte[0];
    private static Map<String,WeixinParsingBean> parsingMap = null;
    private static Map<String,WeixinUserBean> weixinuserMap = null;
    public static enum Counters{
        ROWS
    }
    //每个任务块开始执行之前进行的设置信息，这里主要是从数据库中和分析条件文件中读取预处理的信息，为数据文件的分析做准备
    public void setup(Context context){
        synchronized (lock){
            if(parsingMap == null || weixinuserMap == null){
                parsingMap = new HashMap<String, WeixinParsingBean>();
                weixinuserMap = new HashMap<String, WeixinUserBean>();
                FileSystem fs;
                try{
                    //开始读取分析数据文件
                    fs = FileSystem.get(context.getConfiguration());
                    FileStatus[] uploadparsing = fs.listStatus(new Path("hdfs://192.168.217.130:9000/user/hadoop/weixin"));
                    for(FileStatus ele:uploadparsing){
                        FSDataInputStream fsdis = fs.open(ele.getPath());
                        String fileName = ele.getPath().getName().split("\\.")[0];
                        BufferedReader br = new BufferedReader(new InputStreamReader(fsdis,"UTF-8"));
                   // Configuration conf = new Configuration();
                   // FileSystem fs = FileSystem.get(URI.create(uploadparsing),conf);
                        String line = null;
                        //timePoint: 2013-03-05 13:57:40 durationTime :56
                        //gender:男男
                        //isFriend:否 ageSpan:22至22 vocation:null
                        //communicationPlace:北京市海淀区，北京市朝阳区 key-words:我爱你
                        try{
                            while((line = br.readLine()) !=null){
                                WeixinParsingBean wpb = new WeixinParsingBean();
                                String[] original = line.split("\t");
                                if(original.length == 8){
                                    if(! original[0].contains("null")){
                                        wpb.setTimePoint(sdf
                                                .parse(original[0]
                                                        .substring(original[0]
                                                                .indexOf(":") + 1))
                                                .getTime());
                                    }
                                    if(! original[1].split(":")[1].equals("null")){
                                        wpb.setDurationTime(Integer.parseInt(original[1].split(":")[1]));
                                    }
                                    String gender = original[2].split(":")[1];
                                    if(! gender.equals("null")){
                                        if(gender != null){
                                            if(gender.equals("男男")){
                                                wpb.setGender("11");
                                            }else if(gender.equals("女女")){
                                                wpb.setGender("00");
                                            }else if(gender.equals("异性")){
                                                wpb.setGender("01");
                                            }else {
                                                wpb.setGender("ALL");
                                            }
                                        }
                                    }
                                    if(! original[3].split(":")[1].equals("null")){
                                        if(original[3].split(":")[1].equals("是")){
                                            wpb.setFriend("true");
                                        }else if(original[3].split(":")[1].equals("否")){
                                            wpb.setFriend("flase");
                                        }else{
                                            wpb.setFriend("ALL");
                                        }
                                    }


                                    String ageSpan = original[4].split(":")[1];
                                    if(! ageSpan.equals("null")){
                                        wpb.setMinAge(Integer.parseInt(ageSpan.split("至")[0]));
                                        wpb.setMaxAge(Integer.parseInt(ageSpan.split("至")[1]));
                                    }

                                    if(! "null".equals(original[5].split(":")[1])){
                                        wpb.setVocations(original[5].split(":")[1]);
                                    }
                                    if(! "null".equals(original[6].split(":")[1])){
                                        wpb.setCommunicationPlace(original[6].split(":")[1].split(","));
                                    }
                                    if(! "null".equals(original[7].split(":")[1])){
                                        wpb.setKeywords(original[7].split(":")[1].split(","));
                                    }
                                }
                                System.out.println(wpb.toString());
                                parsingMap.put(fileName,wpb);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fsdis.close();
                    }
                //读取数据库的微信人物信息
                    Path path = new Path("hdfs://192.168.217.130:9000/user/hadoop/weixinuser/user");
                    FSDataInputStream fsdis = fs.open(path);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fsdis,"UTF-8"));
                    String line = null;
                    while((line = br.readLine()) != null){
                        String[] array = line.split("\t");
                        try{
                            if(array.length == 6){
                                WeixinUserBean wub = new WeixinUserBean();
                                wub.setId(Integer.valueOf(array[0]));
                                wub.setName(array[1]);
                                wub.setAge(Integer.valueOf(array[2]));
                                wub.setVocation(array[4]);
                                wub.setSex(array[3]);
                                wub.setFriends(array[5]);
                                System.out.println(wub.toString());

                                weixinuserMap.put(array[0],wub);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //数据文件里的每一个行数据都会经过该方法的处理，这里主要分析了每条数据是不是符合用户定义的分析条件，如果符合把数据发给reduce任务
    public void map(LongWritable key,Text value,Context context){
        setup(context);
        //{10001,100084} \t 2013-02-16 19:22:46 \t 2013-02-16 21:32:45 \t
        //北京16办中学
        // \t办好了？
        String[] array = value.toString().split("\t");
        if(array.length == 5){
            try{
                //开始对每行数据进行逐条分析，看是不是满足用户定义的条件信息
                long beginTime = sdf.parse(array[1]).getTime();
                long endTime = sdf.parse(array[2]).getTime();
                String[] ids = array[0].substring(
                        array[0].indexOf("{") + 1,array[0].indexOf("}")).split(","); //获取id
                if(ids.length == 2){
                    for(Map.Entry<String,WeixinParsingBean> me:parsingMap.entrySet()){
                       // System.out.println(me.toString());
                        if(me.getValue().getTimePoint() != 0L){
                            if(beginTime < me.getValue().getTimePoint()){
                                continue;
                            }
                        }
                        if(me.getValue().getDurationTime() != 0){
                            if(endTime - beginTime < 1000L * me.getValue().getDurationTime() * 60){
                                continue;
                            }
                        }

                        if(me.getValue().getCommunicationPlace() != null){
                            boolean pass = false;
                            for(String ele:me.getValue().getCommunicationPlace()){
                                if(ele.trim().equals(array[3].trim())){
                                    pass = true;
                                    break;
                                }
                            }
                            if(! pass){
                                continue;
                            }
                        }

                        if(me.getValue().getKeywords() != null){
                            boolean nopass = false;
                            for(String ele:me.getValue().getKeywords()){
                                if(! array[4].contains(ele)){
                                    nopass = true;
                                    break;
                                }
                            }
                            if(nopass){
                                continue;
                            }
                        }
                        System.out.println(me.getValue().getGender().equals("ALL"));
                        if(! (me.getValue().getGender().equals("ALL"))){
                            if(me.getValue().getGender().equals("00")){
                                if(!(weixinuserMap.get(ids[0]).getSex().equals("女"))){
                                   // System.out.println(weixinuserMap.get(ids[0]).getSex());
                                    continue;
                                }
                                if(!(weixinuserMap.get(ids[1]).getSex().equals("女"))){
                                    //System.out.println(weixinuserMap.get(ids[1]).getSex());
                                    continue;
                                }
                            }else if(me.getValue().getGender().equals("11")){
                                if(!(weixinuserMap.get(ids[0]).getSex().equals("男"))){
                                   // System.out.println(weixinuserMap.get(ids[0]).getSex());
                                    continue;
                                }
                                if(!(weixinuserMap.get(ids[1]).getSex().equals("男"))){
                                    //System.out.println(weixinuserMap.get(ids[1]).getSex());
                                    continue;
                                }
                            }else if(me.getValue().getGender().equals("01")){
                                if(weixinuserMap.get(ids[0]).getSex().equals(weixinuserMap.get(ids[1]).getSex())){
                                    continue;
                                }
                            }
                        }

                        if(! (me.getValue().getFriend().equals("ALL"))){
                            if(me.getValue().getFriend().equals("true")){
                                if(!(weixinuserMap.get(ids[0]).getFriends().contains(ids[1]))){
                                    continue;
                                }
                                if(!(weixinuserMap.get(ids[1]).getFriends().contains(ids[0]))){
                                    continue;
                                }
                            }else if(me.getValue().getFriend().equals("false")){
                                if(weixinuserMap.get(ids[0]).getFriends().contains(ids[1]) &&
                                        weixinuserMap.get(ids[1]).getFriends().contains(ids[0])){
                                    continue;
                                }
                            }
                        }

                        if(me.getValue().getMinAge() > 0 && me.getValue().getMaxAge() > 0 ){
                            System.out.println(me.getValue().getMinAge());
                            System.out.println(me.getValue().getMaxAge());
                            System.out.println(weixinuserMap.get(ids[0]).getAge());
                            if(weixinuserMap.get(ids[0]).getAge() < me.getValue().getMinAge() ||
                                    weixinuserMap.get(ids[0]).getAge() > me.getValue().getMaxAge()){
                                System.out.println("1age right!!!");
                                continue;
                            }
                            if(weixinuserMap.get(ids[1]).getAge() < me.getValue().getMinAge() ||
                                    weixinuserMap.get(ids[1]).getAge() > me.getValue().getMaxAge()){
                                System.out.println("2age right!!!");
                                continue;
                            }
                        }

                        if(me.getValue().getVocations() != null){
                            System.out.println(me.getValue().getVocations());
                            System.out.println(weixinuserMap.get(ids[0]).getVocation());
                            System.out.println(weixinuserMap.get(ids[1]).getVocation());
                            if(! (me.getValue().getVocations().contains(weixinuserMap.get(ids[0]).getVocation()))){
                                System.out.println("1vocations right!!!");
                                continue;
                            }
                            if(! (me.getValue().getVocations().contains(weixinuserMap.get(ids[1]).getVocation()))){
                                System.out.println("2vocations right!!!");
                                continue;
                            }
                        }
                        System.out.println("Key:   "+me.getKey());
                        System.out.println("Value:   "+value.toString());
                        context.write(new Text(me.getKey()), value);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            context.getCounter(Counters.ROWS).increment(1);
        }
    }

}
