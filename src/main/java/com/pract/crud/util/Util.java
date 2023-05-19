package com.pract.crud.util;

import com.pract.crud.dto.UserSettingDto;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public final class Util {
    public static Date strToDate(String strDate, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String dateToStr(Date date, String pattern) {

        System.out.println(date);
        System.out.println(pattern);
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Period getAge(Date date) {
        try {
            return Period.between(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()), LocalDate.now());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static int getAgeYear(String date) {
        try {
            Date userDate = strToDate(date, "yyyy-mm-dd");
            return Period.between(LocalDate.ofInstant(userDate.toInstant(), ZoneId.systemDefault()), LocalDate.now()).getYears();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public static Map<String, String> userSettingtoMap(List<UserSettingDto> settings) {
        Map<String, String> result = new HashMap<>();
        if (settings != null && !settings.isEmpty()) {
            settings.forEach(setting -> {
                result.put(setting.getKey(), setting.getValue());
            });
        }
        return result;
    }

    public static List<UserSettingDto> mapToUserSetting(Map<String, String> map) {
        List<UserSettingDto> result = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            map.entrySet().forEach(
                    item -> {
                        result.add(new UserSettingDto(item.getKey(), item.getValue()));
                    }
            );

        }
        return result;
    }

    public static Map<String, String> objectToUserSetting(List<Object> objects) {
        Map<String, String> settings = new HashMap<>();
        objects.forEach(o -> {
            for (Field declaredField : o.getClass().getDeclaredFields()) {
                System.out.println(declaredField.getName());
                //declaredField.setAccessible(true);
                //settings.put(declaredField.getName(), declaredField.get(objects).toString());
            }
        });

        return settings;
    }

    public static Map<String, String> jsonToUserSetting(JSONArray objects) throws JSONException {
        Map<String, String> result = new HashMap<>();

        if (objects != null) {
            for (int i = 0; i < objects.length(); i++) {
                JSONObject jsonObject = objects.getJSONObject(i);
                Iterator<?> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    result.put(key, (String) jsonObject.get(key));
                }

            }
        }

        return result;
    }
}


