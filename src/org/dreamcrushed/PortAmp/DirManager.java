package org.dreamcrushed.PortAmp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirManager {

	private File dir;
	private List<Clazz> classes;
	private List<DirManager> subDirs;

	public DirManager(String dir) {
		this.dir = new File(dir);
		classes = new ArrayList<Clazz>();
		subDirs = new ArrayList<DirManager>();
	}

	public DirManager(File file) {
		this.dir = file;
		classes = new ArrayList<Clazz>();
		subDirs = new ArrayList<DirManager>();
	}

	private void parseDir() {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				DirManager manager = new DirManager(file);
				manager.parseDir();
				subDirs.add(manager);
			} else {
				FileHandler handler = FileHandler.getHandler(file);
				if (handler != null) {
					classes.add(handler.parseFile(file));
				}
			}
		}
	}

	private void writeOut(String output) {
		String myName = dir.getName();
		File out = new File(output + File.pathSeparator + myName);
		if (!out.exists()) {
			out.mkdir();
		}
		for (DirManager manager : subDirs) {
			manager.writeOut(output + File.pathSeparator + myName);
		}
		for (Clazz clazz : classes) {
			clazz.writeOut(output + File.pathSeparator + myName);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = ".";
		if (args.length == 1) {
			System.out.print("Usage: " + args[0] + " [inputDir] outputDir");
			return;
		}
		String output = args[1];
		if (args.length > 2) {
			dir = args[1];
			output = args[2];
		}
		
		DirManager manager = new DirManager(dir);
		manager.parseDir();
		manager.writeOut(output);
	}

}
