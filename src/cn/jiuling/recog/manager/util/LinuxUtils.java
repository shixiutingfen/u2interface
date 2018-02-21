package cn.jiuling.recog.manager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class LinuxUtils {
	protected static Logger logger = Logger.getLogger(LinuxUtils.class.getName());

	public static int execueteCommand(String command) throws IOException{
		int exitStatus = -1;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String result = sb.toString();
	        if(result.length()> 0){
	        	logger.info("command:"+ command +"\n"+ result); 
	        }
	        BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
	        while ((line = errorBuffer.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        result = sb.toString(); 
	        if(result.length() > 0){
				logger.error("command:"+ command +" exitStatus:"+exitStatus+" result:"+ result); 
			}
			p.waitFor();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		exitStatus = p.exitValue();
		return exitStatus;
	}
	
	public static int execueteCommand(String[] command) throws IOException{
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command);
		int exitStatus = -1;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String result = sb.toString();
	        if(result.length()> 0){
	        	logger.info("command:"+ command[2] + result); 
	        }
	        BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
	        while ((line = errorBuffer.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        result = sb.toString();  
	        p.waitFor();
			exitStatus = p.exitValue();
			if(result.length() > 0){
				logger.error("command:"+ command[2] +" exitStatus:"+exitStatus+" result:"+ result); 
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} 
		return exitStatus;
	}

	public static void main(String[] args) {
		try {
			LinuxUtils.execueteCommand("sh ../../u2s/manager/apache-tomcat-8.5.23/bin/catlog.sh");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
