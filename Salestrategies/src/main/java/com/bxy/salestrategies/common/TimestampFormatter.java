package com.bxy.salestrategies.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampFormatter implements IFormatter {

    @Override
    public String format(String value) {
        long timestamp =  Long.parseLong(value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
        return format.format(new Date(timestamp));   
    }

}
