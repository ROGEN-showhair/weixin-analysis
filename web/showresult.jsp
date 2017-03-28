<%@ page import="java.util.Map" %>
<%@ page import="com.jd.analysis.bean.ResultBean" %>
<%@ page import="com.jd.analysis.bean.WeixinParsingBean" %><%--
  Created by IntelliJ IDEA.
  User: xudi1
  Date: 2017/3/27
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>分析结果</title>
</head>
<body>
<%
    Map<String,ResultBean> rMap = (Map<String,ResultBean>)request.getAttribute("Result");
    for(Map.Entry<String,ResultBean> me:rMap.entrySet()) {
        out.println("通信人：" + me.getValue().getCommID());
        out.println("开始时间：" + me.getValue().getBeginTime());
        out.println("结束时间：" + me.getValue().getEndTime());
        out.println("通信地点：" + me.getValue().getLocation());
        out.println("通信内容：" + me.getValue().getContent());
    }
%>
</body>
</html>
