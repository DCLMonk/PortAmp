package org.dreamcrushed.PortAmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class FileHandler {

	public static FileHandler getHandler(File file) {
		if (file.getName().endsWith("java")) {
			return new JavaHandler();
		}
		return null;
	}

	final public void parseFile(File file, DirManager dirManager) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			while ((line = br.readLine()) != null) {
				parseLine(line, dirManager);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void parseLine(String line, DirManager dirManager) {
		// Subclasses handle this
	}

	// Subclasses should remove file extension
	protected String baseName(String name) {
		return name;
	}

}
