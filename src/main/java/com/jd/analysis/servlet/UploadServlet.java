package com.jd.analysis.servlet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by xudi1 on 2017/3/27.
 */
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = -1194384455875004126L;
    private static String upload_location = "hdfs://192.168.217.130:9000/user/hadoop/weixininput/data";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

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
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                line = line + "\n";
                out.write(line.toString().getBytes("UTF-8"));
            }
            reader.close();
            request.getRequestDispatcher("success.jsp").forward(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(reader);
            IOUtils.closeStream(out);
        }

    }
}
