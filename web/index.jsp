<%--
  Created by IntelliJ IDEA.
  User: xudi1
  Date: 2017/3/19
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Weixin-Analysis</title>
  </head>
  <body>
  下载：
  <form method="get" action = "DownloadServlet" >
    <hr>
    <p>FileName：<input type="text" name="fileName" ></p>
    <p>FilePath：<input type="text" name="filePath"></p>
    <input name = "download" type="submit" value="下载数据">
    <hr />
  </form>
  上传：
  <form method="post" action = "UploadServlet" enctype="multipart/form-data">
    <input type="file" name="selectfile" value="选择文件">
    <input name = "upload" type="submit" value="上传数据">
  </form>

  分析条件：
  <hr>
  <form action="WeixinParsingServlet" method="post" >
    <p>文件名：
      <input type="text" name="FileName" /></p>
    <p>通信时间点：
      <input type="text" name="TimePoint" />（格式：2017-03-21 02:20:35 注：大于等于为符合条件）</p>
    <p>通信时长：
      <input type="text" name="DurationTime" />分钟（注：大于等于为符合条件）</p>
    <p>性别选择：
      <input type="radio" value="11" name="Gender" />男男
      <input type="radio" value="00" name="Gender" />女女
      <input type="radio" value="01" name="Gender" />异性
      <input type="radio" value="ALL" name="Gender" />全部</p>
    <p>是否好友关系：
      <input type="radio" value="true" name="Friend" />是
      <input type="radio" value="false" name="Friend" />否
      <input type="radio" value="ALL" name="Friend" />两者都包含</p>
    <p>年龄范围:
      <input type="text" name="AgeRange" />（格式：35至38）</p>
    <p>职业：
      <input type="checkbox" value="餐饮" name="Vocations" />餐饮
      <input type="checkbox" value="教育" name="Vocations" />教育
      <input type="checkbox" value="金融" name="Vocations" />金融
      <input type="checkbox" value="律师" name="Vocations" />律师
      <input type="checkbox" value="娱乐" name="Vocations" />娱乐</p>
    <p><input type="checkbox" value="军人" name="Vocations" />军人
      <input type="checkbox" value="体育" name="Vocations" />体育
      <input type="checkbox" value="建筑" name="Vocations" />建筑
      <input type="checkbox" value="无业" name="Vocations" />无业</p>
    <p>通信地点:
      <input type="text" name="CommunicationPlace" />（多个通信地点用英文逗号隔开）</p>
    <p>通信内容关键词:
      <input type="text" name="Keywords" />（多个关键词用英文逗号隔开）</p>

    <input type="submit" value="上传" />
    <input type="reset" value="重置"/>
  </form>
  <hr />
  计算并显示结果：
  <form action="CalculateShowServlet" method="get">
  <input type="submit" value="开始计算并显示结果">
  </form>
  </body>
</html>
