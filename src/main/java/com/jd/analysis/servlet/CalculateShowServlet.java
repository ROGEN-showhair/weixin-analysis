package com.jd.analysis.servlet;

import com.jd.analysis.bean.ResultBean;
import com.jd.analysis.bean.WeixinUserBean;
import com.jd.analysis.hadooptestjob.TestJob;
import com.jd.analysis.mapreduce.WeixinMapper;
import com.jd.analysis.mapreduce.WeixinReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xudi1 on 2017/3/29.
 */
public class CalculateShowServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.217.130:9000/user/hadoop/weixinout"), conf);
        Path out = new Path("hdfs://192.168.217.130:9000/user/hadoop/weixinout");
        if (fs.exists(out)) {
            fs.delete(out, true);
        }
        //开始生成处理天气数据的Map/Reduce任务job信息，然后提交给Hadoop集群开始执行
        Job job = new Job(conf, "Parsing Meteorological Data");
        job.setJarByClass(TestJob.class);
        Path in = new Path("hdfs://192.168.217.130:9000/user/hadoop/weixininput/data");
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);

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
            ShowResult();
            request.setAttribute("Result",ShowResult());
            request.getRequestDispatcher("showresult.jsp").forward(request, response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, ResultBean> ShowResult() {
        Map<String, ResultBean> resultMap = new HashMap<String, ResultBean>();
        FileSystem fs;
        try {
            //开始读取分析数据文件
            fs = FileSystem.get(new Configuration());
            FileStatus[] uploadparsing = fs.listStatus(new Path("hdfs://192.168.217.130:9000/user/hadoop/weixinout/file1.result"));
            for (FileStatus ele : uploadparsing) {
                FSDataInputStream fsdis = fs.open(ele.getPath());
                String fileName = ele.getPath().getName().split("\\.")[0];
                BufferedReader br = new BufferedReader(new InputStreamReader(fsdis, "UTF-8"));
                String line = null;
                while((line = br.readLine()) != null){
                    ResultBean result = new ResultBean();
                    System.out.println(line);
                    String[] res = line.split("\t");
                    if(res.length == 5){
                        result.setCommID(res[0]);
                        result.setBeginTime(res[1]);
                        result.setEndTime(res[2]);
                        result.setLocation(res[3]);
                        result.setContent(res[4]);

                        resultMap.put(fileName,result);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
