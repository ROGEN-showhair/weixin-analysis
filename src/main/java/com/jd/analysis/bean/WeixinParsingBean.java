package com.jd.analysis.bean;

import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by xudi1 on 2017/3/24.
 */
public class WeixinParsingBean {
    private long TimePoint;
    private int DurationTime;
    private String Gender;
    private String Friend;
    private int MaxAge;
    private int MinAge;
    private String Vocations;
    private String[] CommunicationPlace;
    private String[] Keywords;

    public WeixinParsingBean(){

    }
    public WeixinParsingBean(long timePoint, int durationTime, String gender, String friend, int maxAge, int minAge, String vocations, String[] communicationPlace, String[] keywords) {
        TimePoint = timePoint;
        DurationTime = durationTime;
        Gender = gender;
        Friend = friend;
        MaxAge = maxAge;
        MinAge = minAge;
        Vocations = vocations;
        CommunicationPlace = communicationPlace;
        Keywords = keywords;
    }

    public String[] getKeywords() {
        return Keywords;
    }

    public void setKeywords(String[] keywords) {
        Keywords = keywords;
    }

    public String[] getCommunicationPlace() {
        return CommunicationPlace;
    }

    public void setCommunicationPlace(String[] communicationPlace) {
        CommunicationPlace = communicationPlace;
    }

    public String getVocations() {
        return Vocations;
    }

    public void setVocations(String vocations) {
        Vocations = vocations;
    }

    public int getMaxAge() {
        return MaxAge;
    }

    public void setMaxAge(int maxAge) {
        MaxAge = maxAge;
    }

    public int getMinAge() {
        return MinAge;
    }

    public void setMinAge(int minAge) {
        MinAge = minAge;
    }

    public String getFriend() {
        return Friend;
    }

    public void setFriend(String friend) {
        Friend = friend;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getDurationTime() {
        return DurationTime;
    }

    public void setDurationTime(int durationTime) {
        DurationTime = durationTime;
    }

    public long getTimePoint() {
        return TimePoint;
    }

    public void setTimePoint(long timePoint) {
        TimePoint = timePoint;
    }

    @Override
    public String toString() {
        return "WeixinParsingBean{" +
                "TimePoint=" + TimePoint +
                ", DurationTime=" + DurationTime +
                ", Gender='" + Gender + '\'' +
                ", Friend='" + Friend + '\'' +
                ", MaxAge=" + MaxAge +
                ", MinAge=" + MinAge +
                ", Vocations='" + Vocations + '\'' +
                ", CommunicationPlace=" + Arrays.toString(CommunicationPlace) +
                ", Keywords=" + Arrays.toString(Keywords) +
                '}';
    }
}
