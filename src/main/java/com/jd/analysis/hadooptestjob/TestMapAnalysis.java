package com.jd.analysis.hadooptestjob;

import java.io.*;

/**
 * Created by xudi1 on 2017/3/13.
 */
public class TestMapAnalysis {
    private static String data_location = "E:\\Java-Project\\weather-analysis\\input\\data";
    private static String map_temp = "E:\\Java-Project\\weather-analysis\\input\\test\\maptemp";

    public static void main(String[] args){
        try{
            File file = new File(map_temp);
            if( !file.exists()){
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file,true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(data_location)));
            String value = "";
            while((value = reader.readLine()) != null){
                outputStream.write(Mapanalysis(value).toString().getBytes("utf-8"));
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String Mapanalysis(String value){
        return null;
    }
}
