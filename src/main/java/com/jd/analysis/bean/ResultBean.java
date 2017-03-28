package com.jd.analysis.bean;

/**
 * Created by xudi1 on 2017/3/29.
 */
public class ResultBean {
    private String CommID;
    private String BeginTime;
    private String EndTime;
    private String Location;
    private String Content;

    public ResultBean(){

    }
    public ResultBean(String commID, String beginTime, String endTime, String location, String content) {
        CommID = commID;
        BeginTime = beginTime;
        EndTime = endTime;
        Location = location;
        Content = content;
    }

    public String getCommID() {
        return CommID;
    }

    public void setCommID(String commID) {
        CommID = commID;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "CommID='" + CommID + '\'' +
                ", BeginTime='" + BeginTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", Location='" + Location + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }
}
