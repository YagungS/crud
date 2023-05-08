package com.pract.crud.util;

import com.pract.crud.dto.UserSettingDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, String> UserSettingtoMap(List<UserSettingDto> settings) {
        Map<String, String> result = new HashMap<>();
        if (settings != null && !settings.isEmpty()) {
            settings.forEach(setting -> {
                result.put(setting.getKey(), setting.getValue());
            });
        }
        return result;
    }
}
