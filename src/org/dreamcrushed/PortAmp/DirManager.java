package org.dreamcrushed.PortAmp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirManager {

	private File dir;
	private Map<String, Clazz> classes;
	private List<DirManager> subDirs;

	public DirManager(String dir) {
		this.dir = new File(dir);
		classes = new HashMap<String, Clazz>();
		subDirs = new ArrayList<DirManager>();
	}

	public DirManager(File file) {
		this.dir = file;
		classes = new HashMap<String, Clazz>();
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
					handler.parseFile(file, this);
				}
			}
		}
	}

	private void writeOut(String output) {
		String myName = dir.getName();
		File out = new File(output + File.separator + myName);
		if (!out.exists()) {
			out.mkdir();
		}
		for (DirManager manager : subDirs) {
			manager.writeOut(output + File.separator + myName);
		}
		for (Clazz clazz : classes.values()) {
			clazz.writeOut(output + File.separator + myName);
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
		String output = "../output/";//args[1];
		if (args.length > 2) {
			dir = args[1];
			output = args[2];
		}

		DirManager manager = new DirManager(dir);
		manager.parseDir();
		manager.writeOut(output);
	}

	public Clazz getAddClass(String className, String[] superClasses) {
		if (!classes.containsKey(className)) {
			classes.put(className, new Clazz(className, superClasses));
		}
		return classes.get(className);
	}

}
