package le.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Properties;

/**
 * DataFile is a representation of properties file.
 * */
public class DataFile {
	Properties properties;
	File file;
	public DataFile(File f) {
		this.file = f;
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
			p.load(reader);
			this.properties = p;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DataFile(String path) {
		this(new File(path));
	}
	public String get(String key) {
		return getOrDefault(key, null);
	}
	public String getOrDefault(String key, Object defautl) {
		return (String) properties.getOrDefault(key, defautl);
	}
	public void put(Object key, Object value) {
		properties.put(key.toString(), value.toString());
		try {
			save(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void putWithoutSave(Object key, Object value) {
		properties.put(key.toString(), value.toString());
	}
	public void save(String comment) throws FileNotFoundException, IOException {
		properties.store(new FileOutputStream(file), comment);
	}
	public int length() {
		return properties.size();
	}
	public String[] keys() {
		return properties.keySet().toArray(new String[0]);
	}
	public String[] values() {
		return properties.values().toArray(new String[0]);
	}
}