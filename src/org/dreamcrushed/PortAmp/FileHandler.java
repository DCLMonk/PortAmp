package org.dreamcrushed.PortAmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class FileHandler {

	public static FileHandler getHandler(File file) {
		return null;
	}

	final public Clazz parseFile(File file) {
		Clazz clazz = new Clazz(baseName(file.getName()));
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			while ((line = br.readLine()) != null) {
				parseLine(line, clazz);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	protected void parseLine(String line, Clazz clazz) {
		// Subclasses handle this
	}

	// Subclasses should remove file extension
	protected String baseName(String name) {
		return name;
	}

}
