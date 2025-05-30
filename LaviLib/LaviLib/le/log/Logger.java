package le.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


import le.install.AbstractInstall;

/**
 * This class used for logging the program's activity.
 * */
public class Logger {
	
	protected AbstractInstall install;
	
	
	public boolean printInConsole = true;
	public PrintStream console = System.out;
	public PrintStream err = System.err;
	private boolean addTimeStamp = true;
	private PrintStream logger;
	private PrintStream errorLogger;
	private PrintStream liveLogger;
	private StringBuffer log = new StringBuffer();
	private StringBuffer errorLog = new StringBuffer();
	public int errorCount = 0;
	
	private PrintStream logListener = null;
	
	public Logger(AbstractInstall install) {
		this.install = install;
	}
	
	public void initializeLogger() {
		logger = new PrintStream(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				Logger.this.liveappend((char)b);
				if (printInConsole) {
					console.append((char)b);
				}
				if (logListener != null) {
					logListener.append((char)b);
				}
				log.append((char)b);
			}
		}) {
			@Override
			public void println(String x) {
				super.println(x + (isAddTimeStamp()?" (timestamp " + System.currentTimeMillis() + ")":""));
			}
		};
		errorLogger = new PrintStream(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				Logger.this.liveappend((char)b);
				errorLog.append((char)b);
				if (printInConsole) {
					err.print((char)b);
				}
				if (logListener != null) {
					logListener.append((char)b);
				}
			}
		});
		if (this.install.isInstalled()) {
			initializeLiveLogger();
		}else {
			liveLogger = new PrintStream(new OutputStream() {
				
				@Override
				public void write(int b) throws IOException {
					// TODO Auto-generated method stub
					
				}
			});
		}
		System.setOut(logger);
		System.setErr(errorLogger);
	}
	protected void liveappend(char b) {
		liveLogger.append(b);
	}

	public void initializeLiveLogger() {
		try {
			File liveLog = this.install.getFile("Data\\Logs\\live log.txt");
			liveLog.createNewFile();
			liveLog.deleteOnExit();
			liveLogger = new PrintStream(this.install.getFile("Data\\Logs\\live log.txt"));
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void exportTo(File f) {
		PrintStream ps = null;
		try {
			ps = new PrintStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ps.println("Error Log:");
		ps.println(getErrorLog());
		ps.println("Standart Log:");
		ps.println(getLog());
	}
	public String getLog() {
		return log.toString();
	}
	public String getErrorLog() {
		return errorLog.toString();
	}
	public void stop() {
		logger = null;
		errorLogger = null;
		liveLogger = null;
		log = null;
		errorLog = null;
		System.setOut(console);
		System.setErr(err);
		console = null;
		err = null;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public boolean isAddTimeStamp() {
		return addTimeStamp;
	}
	public void enableTimeStamp() {
		addTimeStamp = true;
	}
	public void disableTimeStamp() {
		addTimeStamp = false;
	}
	public PrintStream getErrorLogger() {
		return errorLogger;
	}

	public PrintStream getLogListener() {
		return logListener;
	}

	public void setLogListener(PrintStream logListener) {
		this.logListener = logListener;
	}
}