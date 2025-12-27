package backend;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import types.LogCategory;
import types.LogLevel;

public class FileLogger implements Logger{
	
	
	  private static volatile FileLogger instance = new FileLogger();

	    private FileLogger() {}
	    
	    private static String resolveFile(LogCategory category) {
	    	
	     switch(category) {
	     case SYSTEM : return "logs/system.log";
         case USER : return "logs/user.log";
         case TRANSACTION : return "logs/transaction.log";
	     default : return "null";}

	    }

	    public static FileLogger getInstance() {
	    	
	        if (instance == null) {
	            synchronized (FileLogger.class) {
	                if (instance == null) {
	                    instance = new FileLogger();
	                }
	            }
	        }
	        return instance;
	    }
	    @Override
	    public synchronized void log(LogLevel level, LogCategory category, String message) {
	        String filePath = resolveFile(category);

	        try (FileWriter writer = new FileWriter(filePath, true)) {

	            writer.write(
	                "[" + LocalDateTime.now() + "] " +
	                "[" + level + "] " +
	                message + System.lineSeparator()
	            );

	        } catch (IOException e) {
	            System.err.println("Logger failure: " + e.getMessage());
	        }
	    }
	    }
