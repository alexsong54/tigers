package com.bxy.salestrategies.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.bxy.salestrategies.AccountPage;
import com.bxy.salestrategies.common.IFormatter;
import com.bxy.salestrategies.userlog.LogObj;
import com.google.gson.Gson;

public class Utility {
	public static final Logger logger = Logger.getLogger(AccountPage.class);
    public static ThreadPoolExecutor executorPool;
    public static final String STAT_LOG_IN_OUT = "logInOut";
	public static String readFileAttribure(String fieldName){
		 Properties systemPeroperties = new Properties();
    	 try {
			systemPeroperties.load(Utility.class.getResourceAsStream("/crm.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return systemPeroperties.getProperty(fieldName);
	}
	public static ThreadPoolExecutor getThreadPoolExecutor() {

        if (executorPool == null) {
            synchronized (Configuration.class) {
                ThreadFactory threadFactory = Executors.defaultThreadFactory();
                
                //creating the ThreadPoolExecutor 
                executorPool = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), threadFactory);
            }
        }
        return executorPool;

    }
	public static void printStat(String type, LogObj object,Type typeOfSrc){
        
        UUID uuid  =  UUID.randomUUID(); 
        object.setUuid(uuid.toString());
        Gson gson = new Gson();   
        String js = gson.toJson(object, typeOfSrc);
        logger.info("STAT."+type +"="+js);
    }
	
    public static String formatValue(String formatter, String value) {
        String res = value;
        try {
            if (formatter != null) {
                Class clazz = Class.forName(formatter);
                IFormatter formatterImplement = (IFormatter) clazz.newInstance();
                res = formatterImplement.format(value);
            }
        } catch (Exception e) {
            logger.error("failed to format value",e);
        }
        return res;

    }

}
