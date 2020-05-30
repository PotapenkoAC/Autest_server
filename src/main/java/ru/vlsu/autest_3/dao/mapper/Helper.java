package ru.vlsu.autest_3.dao.mapper;

public class Helper {

    public static Long getQAIdFromString(String line){ //line in format id@name
        return Long.parseLong(line.split("@")[0]);
    }
}
