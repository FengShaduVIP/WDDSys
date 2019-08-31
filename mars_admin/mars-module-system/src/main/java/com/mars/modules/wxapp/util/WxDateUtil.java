package com.mars.modules.wxapp.util;

import com.mars.common.util.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class WxDateUtil {

    public static Date wxDateToStrDate(String wxDate) throws ParseException {
        String month = wxDate.substring(0,2);
        String day = wxDate.substring(3,5);
        String time = wxDate.split(" ")[1];
        Calendar calendar = DateUtils.getCalendar();
        String year = calendar.get(1)+"";
        return DateUtils.parseDate(year+"-"+month+"-"+day+" "+ time+":00","yyyy-MM-dd hh:mm:ss");
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(wxDateToStrDate(""));
    }
}
