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
	private DirManager owner;

	public DirManager(File file, DirManager owner) {
		this.owner = owner;
		this.dir = file;
		classes = new HashMap<String, Clazz>();
		subDirs = new ArrayList<DirManager>();
	}

	public DirManager(String dir) {
		this.owner = null;
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
				DirManager manager = new DirManager(file, this);
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
		manager.tagVirts();
		manager.writeOut(output);
	}

	public Clazz getAddClass(String className, String[] superClasses) {
		if (!classes.containsKey(className)) {
			classes.put(className, new Clazz(className, superClasses));
		}
		return classes.get(className);
	}

	public Clazz getClass(String className) {
		if (!classes.containsKey(className)) {
			return null;
		}
		return classes.get(className);
	}

	public void tagVirts() {
		for (Clazz clazz : classes.values()) {
			clazz.tagVirts(this);
		}
		for (DirManager dir : subDirs) {
			dir.tagVirts();
		}
	}
	
	public Clazz findClazz(String clazz) {
		Clazz ret = getClass(clazz);
		if (ret != null) return ret;
		for (DirManager dm : subDirs) {
			ret = dm.findClazz(clazz);
			if (ret != null) return ret;
		}
		return null;
	}
	
	public Clazz findClazzFull(String clazz) {
		DirManager manager = this;
		
		while (manager != null) {
			Clazz c = manager.findClazz(clazz);
			if (c != null) return c;
			manager = manager.getOwner();
		}
		return null;
	}
	
	public DirManager getOwner() {
		return owner;
	}

}
