package com.ayi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/1.
 */
public class Data_time_cuo  {


    /**
     *
     * @param beginDate
     * @return 格式日期
     */
    public static String gettime(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)*1000));
       return sd;
    }
    /**
     *
     * @param beginDate
     * @return 格式日期
     */
    public static String gettime6(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)*1000));
        return sd;
    }
    /**
     *
     * @param beginDate
     * @return 格式日期
     */
    public static String gettime2(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)*1000));
        return sd;
    }
    /**
     *
     * @param beginDate
     * @return 格式日期
     */
    public static String gettime3(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)*1000));
        return sd;
    }
    /**
     *
     * @param beginDate
     * @return 格式日期
     */
    public static String gettime4(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)*1000));
        return sd;
    }
    /**
     *
     * @param beginDate
     * @return 格式日期
     */
    public static String gettime5(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)*1000));
        return sd;
    }

    /**
     * 比较两个时间
     * @param start_unix_time 最后期限
     * @param l 当前时间的毫秒数/1000
     * @return
     */
    public static boolean comp_time_cuo(String start_unix_time, long l) {
        long i=Long.parseLong(start_unix_time);
        if(l>i){
            //过时
            return false;
        }else {
            return  true;
        }

    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
