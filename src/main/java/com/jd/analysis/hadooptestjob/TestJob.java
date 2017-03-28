package com.jd.analysis.hadooptestjob;

import com.jd.analysis.mapreduce.WeixinMapper;
import com.jd.analysis.mapreduce.WeixinReducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.net.URI;

/**
 * Created by xudi1 on 2017/3/13.
 */
public class TestJob {
    private static final Log LOG = LogFactory.getLog(TestJob.class);
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.217.130:9000/user/hadoop/weixinout"),conf);
        Path out = new Path("hdfs://192.168.217.130:9000/user/hadoop/weixinout");
        if(fs.exists(out)){
            fs.delete(out,true);
        }
        //开始生成处理天气数据的Map/Reduce任务job信息，然后提交给Hadoop集群开始执行
        Job job = new Job(conf,"Parsing Meteorological Data");
        job.setJarByClass(TestJob.class);
        Path in = new Path("hdfs://192.168.217.130:9000/user/hadoop/weixininput/data");
        FileInputFormat.setInputPaths(job,in);
        FileOutputFormat.setOutputPath(job,out);

        job.setMapperClass(WeixinMapper.class);
        job.setReducerClass(WeixinReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //job.setNumReduceTasks(1);
        //生成job信息完成
        try {
            //提交job给hadoop集群，然后hadoop集群开始执行
            job.submit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
