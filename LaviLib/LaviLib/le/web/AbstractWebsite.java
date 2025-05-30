package le.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;


import le.log.ExceptionUtils;

/**
 * Represents the website of the product to use its web services.
 * */
public abstract class AbstractWebsite {
	protected String webAddress;
	
	protected boolean hasInternet;
	protected boolean websiteAvaliable;
	
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	public boolean isHasInternet() {
		return hasInternet;
	}
	public void setHasInternet(boolean hasInternet) {
		this.hasInternet = hasInternet;
	}
	public boolean isWebsiteAvaliable() {
		return websiteAvaliable;
	}
	public void setWebsiteAvaliable(boolean websiteAvaliable) {
		this.websiteAvaliable = websiteAvaliable;
	}
	public AbstractWebsite(String webAdress) {
		this.webAddress = webAdress;
	}
	public void openInBrowser() {
		openInBrowser("chrome");
	}
	public void openInBrowser(String browser) {
		try {
			Runtime.getRuntime().exec("cmd /c start " + browser + " " + webAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String sendReport(String name, String title, String report) {
		if (!hasInternet) {
			return "There is no internet connection, please try again later";
		}
		if (!websiteAvaliable) {
			return "Web services are unavaliable right now, please try again later";
		}
		String urlParams = "name=" + name.replaceAll(" ", "+") + "&title=" + title.replaceAll(" ", "+") + 
		"&body=" + report.replaceAll(" ", "+");
		return getGetResponse(webAddress + "/report.php?" + urlParams);
	}
	public void sendAutoReport(Exception e, Thread t) {
		if (!hasInternet || !websiteAvaliable) {
			return;
		}
        @SuppressWarnings("unused")
		String urlParams = "exception=" + e.toString().replaceAll(" ", "+") + 
        		"&description=" + ExceptionUtils.exceptionToString(e, t, -1).replace(" ", "+") + 
		"&place=" + getErrorPlace(e).replaceAll(" ", "+");
	}
	public boolean checkInternetConnection(){
		try {
			new URL("http://www.google.com").openStream();
			hasInternet = true;
		} catch (Exception e) {
			hasInternet = false;
			websiteAvaliable = false;
		}
		return hasInternet;
	}
	public boolean checkWebsite(){
	    try {
			new URL(webAddress).openStream().close();;
			websiteAvaliable = true;
		} catch (Exception e) {
			websiteAvaliable = false;
		}
		return websiteAvaliable;
	}
	public long download(String url, File target) {
		if (!url.startsWith(webAddress)) {
			url = webAddress + url;
		}
		long downloadSize = 0;
		try {
			downloadSize = Files.copy(new URL(url).openStream(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return downloadSize;
	}
	public String getResponse(String url, String params, String method) {
		if (!url.startsWith(webAddress)) {
			url = webAddress + url;
		}
		if (method.toUpperCase().equals("POST")) {
			return getPostResponse(url, params);
		}else {
			return getGetResponse(url + "?" + params);
		}
	}
	private String getGetResponse(String url) {
		URL adress = null;
		try {
			adress = new URL(URLDecoder.decode(url, "UTF-32").replaceAll(" ", "+")
					.replaceAll("\t", "").replaceAll("\n", ""));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner reader = null;
		try {
			reader = new Scanner(adress.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = "";
		while(reader.hasNext()) {
			s += reader.nextLine();
			if (reader.hasNext()) {
				s += "\n";
			}
		}
		return s;
	}
	private String getPostResponse(String address, String params) {
		try {
		    URL url = new URL(address);
			// Convert string to byte array, as it should be sent
		    byte[] postDataBytes = params.toString().getBytes("UTF-8");

		    // Connect, easy
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    // Tell server that this is POST and in which format is the data
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		    conn.setDoOutput(true);
		    conn.getOutputStream().write(postDataBytes);

		    // This gets the output from your server
		    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    String response = "";
		    for (int c; (c = in.read()) >= 0;) {
		        response += (char)c;
		    }
		    return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getErrorPlace(Exception e) {
		String place = "";
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		for (int i = 0; i < stackTraceElements.length; i++) {
			if (stackTraceElements[i].getLineNumber() == -1) {
				place = place + stackTraceElements[i].getClassName() + "(Unknown Source)<br/>";	
			}else {
				place = place + stackTraceElements[i].getClassName() + "(line:" + stackTraceElements[i].getLineNumber() + ")<br/>";	
			}
		}
		return place;
	}
}