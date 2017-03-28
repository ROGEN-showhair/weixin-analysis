package com.jd.analysis.mapreduce;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 *
 * Created by xudi1 on 2017/3/25.
 * reduce任务，主要用来收集map任务过程中产生的符合条件的结果信息，然后对这些结果进行用户归并，并把这些结果
 * 写入到hadoop的HDFS文件系统中
 */
public class WeixinReducer extends Reducer<Text,Text,Text,Text>{
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException {
        String content = "";
        //遍历对结果进行归并
        for(Text value:values){
            content += value.toString() + "\n";
            System.out.println("Reduce:   "+value.toString());
        }
        //context.write(new Text(title), new Text(content));
        //把结果写入HDFS中
        FileSystem hdfs = FileSystem.get(context.getConfiguration());
        System.out.println("Reduce:   "+key.toString());
        Path path = new Path("hdfs://192.168.217.130:9000/user/hadoop/weixinout/" + key.toString() + ".result");
        if(hdfs.exists(path)){
            hdfs.delete(path,true);
        }
        FSDataOutputStream hdfsOut = hdfs.create(path);
        hdfsOut.write(content.getBytes("UTF-8"));
        hdfsOut.close();
        //写入完成并关闭相应资源
    }
    /**
     * 处理用户提交的分析微信数据的云计算请求，客户端提交任务后，服务端负责生成job信息，然后把这个job信息传递给hadoop集群
     * 让Hadoop集群开始执行分析任务
     */
}
