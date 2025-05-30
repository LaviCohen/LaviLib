package le.install;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * AbstractInstall class is the way to access your installation files easily.
 * This is an abstract class, which meant to be extended, to customize the "install" method.
 * */
public abstract class AbstractInstall {
	public String path;
	public AbstractInstall(String path) {
		this.path = path;
	}
	public abstract boolean install() throws InstallationException;
	public boolean isInstalled() {
		return new File(path).exists();
	}
	public boolean unInstall() {
		removeDirectory(new File(path));
		return !(new File(path).exists());
	}
	public String getPath(String resPath) {
		if (resPath.startsWith(path)) {
			return resPath;
		}
		return path + "\\" + resPath;
	}
	public File getFile(String resPath) {
		return new File(getPath(resPath));
	}
	public String getText(String resPath) {
		try (FileInputStream fis = new FileInputStream(getFile(resPath))) {
			String text = "";
			int c = 0;
			while ((c = fis.read()) != -1) {
				text += (char)c;
			}
			return text;
		} catch (IOException e) {
			return null;
		}
	}
	public void writeToFile(File f, String text) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.write(text.getBytes());
			fos.close();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void unZip(String pathToZip, String targetFolder) throws IOException {
		try (ZipFile zipFile = new ZipFile(pathToZip)) {
			  Enumeration<? extends ZipEntry> entries = zipFile.entries();
			  while (entries.hasMoreElements()) {
				  ZipEntry entry = entries.nextElement();
				  File entryDestination = new File(targetFolder,  entry.getName());
				  if(entry.getName().startsWith("Res")) {
					  if(entry.isDirectory()) {
						  entryDestination.mkdirs();
					  }else {
						  entryDestination.getParentFile().mkdirs();
						  InputStream in = zipFile.getInputStream(entry);
						  Files.copy(in, entryDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
					  }
				  }
			  }
		}
	}
	public void copyDirectory(File sourceLocation , File targetLocation)
		    throws IOException {
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			String[] children = sourceLocation.list();
			for (int i=0; i<children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
		} else {
			Files.copy(sourceLocation.toPath(), new FileOutputStream(targetLocation));
		}
	}
	public void removeDirectory(File f) {
		if (f.isDirectory()) {
			String[] children = f.list();
			for (int i = 0; i < children.length; i++) {
				removeDirectory(new File(f.getAbsoluteFile() + "\\" + children[i]));
			}
			f.delete();
		}else {
			f.delete();
		}
	}
}
