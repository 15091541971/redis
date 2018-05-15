package com.game.server.cache.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.game.server.cache.common.CacheException;

/**
 * @author Reason.Yea 
 * @version 创建时间：Jan 8, 2014
 */
public class Config {
	private static Logger logger = Logger.getLogger(Config.class);
	
	private static final String config_properties="CacheConfig.properties" ;
	private static final String workflow_properties="workflow.properties" ;
	
	private static Properties properties ;
	private static Properties workFlowProperties ;
	static{
		properties = getConfigFile()  ;
		workFlowProperties = getWorkFlowConfigFile()  ;
	}
	
	
	public static String get(String key){
		return properties.getProperty(key) ;
	}
	
	public static String getWorkFlowByKey(String key){
		return workFlowProperties.getProperty(key) ;
	}
	
	private Config(){
		
	}
	
	public  static Properties getConfigFileProperties(String fileName) {
		InputStream is;
		Properties configFile  = new Properties() ;
		try {
			
			is = getOutFileInputStream(fileName);
			if(is == null){
				is = getFileInputStream(fileName);
			}
			configFile.load(is) ;
			is.close() ;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException(e);
		}
		
		return configFile ;
	}
	
	/**
	 * 工程下配置文件
	 */
	private static InputStream getFileInputStream(String fileName) throws Exception {
		return Config.class.getClassLoader().getResourceAsStream(fileName) ;
	}
	/**
	 * 独立配置的文件
	 */
	private static InputStream getOutFileInputStream(String fileName) throws Exception {
		File file = new File(fileName);
		return new FileInputStream(file);
	}
	
	public  static Properties getConfigFile()  {
		return getConfigFileProperties(config_properties) ;
	}
	
	public  static Properties getWorkFlowConfigFile()  {
		return getConfigFileProperties(Config.workflow_properties) ;
	}
	
	public static void main(String[] args){
		Properties p = getConfigFile() ;
		int i = 0 ; 
	}
	

	
}
