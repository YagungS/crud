package com.pract.crud.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public final class Util {
    public static Date strToDate(String strDate, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(strDate);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String dateToStr(Date date, String pattern){
        try{
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Period getAge(Date date) {
        try{
            return Period.between(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()), LocalDate.now());
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    public static int getAgeYear(String date) {
        try{
            Date userDate = strToDate(date,"yyyy-mm-dd");
            return Period.between(LocalDate.ofInstant(userDate.toInstant(), ZoneId.systemDefault()), LocalDate.now()).getYears();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

}
