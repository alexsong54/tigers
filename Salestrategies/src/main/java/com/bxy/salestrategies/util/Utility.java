package com.bxy.salestrategies.util;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Utility {
    public static ThreadPoolExecutor executorPool;
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

}
