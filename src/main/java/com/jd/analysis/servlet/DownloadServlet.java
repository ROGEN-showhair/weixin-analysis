package com.jd.analysis.servlet;

import com.jd.analysis.data.DownloadData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xudi1 on 2017/3/27.
 */
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = -5810265716484428158L;
    private static String local_location = "E:\\Java-Project\\weather-analysis\\input\\test\\download";
    private static String download_location = "hdfs://192.168.217.130:9000/user/hadoop/weixininput/data";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String filename = request.getParameter("fileName");
        String filepath = request.getParameter("filePath");
        String localLocation = filepath+filename;
        DownloadData.Download(localLocation,download_location);
        request.getRequestDispatcher("success.jsp").forward(request,response);

    }
}
