package com.jd.analysis.bean;

import javax.naming.ldap.PagedResultsControl;

/**
 * Created by xudi1 on 2017/3/24.
 */
public class WeixinUserBean {
    private int Id;
    private String Name;
    private int Age;
    private String Sex;
    private String Vocation;
    private String Friends;

    public WeixinUserBean(){

    }
    public WeixinUserBean(int id, String name, int age, String sex, String vocation, String friends) {
        Id = id;
        Name = name;
        Age = age;
        Sex = sex;
        Vocation = vocation;
        Friends = friends;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getVocation() {
        return Vocation;
    }

    public void setVocation(String vocation) {
        Vocation = vocation;
    }

    public String getFriends() {
        return Friends;
    }

    public void setFriends(String friends) {
        Friends = friends;
    }

    @Override
    public String toString() {
        return "WeixinUserBean{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Age=" + Age +
                ", Sex='" + Sex + '\'' +
                ", Vocation='" + Vocation + '\'' +
                ", Friends='" + Friends + '\'' +
                '}';
    }
}
