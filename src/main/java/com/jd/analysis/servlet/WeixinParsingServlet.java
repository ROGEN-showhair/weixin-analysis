package com.jd.analysis.servlet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;

/**
 * 把检索条件保存成文件到HDFS
 * Created by xudi1 on 2017/3/24.
 */
public class WeixinParsingServlet extends HttpServlet{
    private static final long serialVersionUID = -2050229673894302491L;
    private static final String parsing_location = "hdfs://192.168.217.130:9000/user/hadoop/weixin/";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String filename = request.getParameter("FileName");
        Configuration conf = new Configuration();
        OutputStream out = null;
        try{
            FileSystem fs = FileSystem.get(URI.create(parsing_location+filename),conf);
            Path upload = new Path(parsing_location+filename);

            if(fs.exists(upload)){
                fs.delete(upload,true);
            }
            out = fs.create(upload);
            String TimePoint = request.getParameter("TimePoint");
            String DurationTime = request.getParameter("DurationTime");
            String Gender = request.getParameter("Gender");
            String Friend = request.getParameter("Friend");
            String AgeRange = request.getParameter("AgeRange");
            String Vocations = request.getParameter("Vocations");
            String CommunicationPlace = request.getParameter("CommunicationPlace");
            String Keywords = request.getParameter("Keywords");

            String line = "timePoint:"+TimePoint+"\tdurationTime:"+DurationTime+
                    "\tgender:"+Gender+"\tisFriend:"+Friend+"\tageSpan:"+AgeRange+
                    "\tvocation:"+Vocations+"\tcommunicationPlace:"+CommunicationPlace+
                    "\tkey-words:"+Keywords+"\n";
            out.write(line.toString().getBytes("UTF-8"));
            System.out.println(line);
            request.getRequestDispatcher("success.jsp").forward(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(out);
        }
        //request.getRequestDispatcher("showresult.jsp").forward(request,response);
    }
}
