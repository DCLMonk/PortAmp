package org.dreamcrushed.PortAmp;

import java.util.ArrayList;
import java.util.List;

public class Clazz {
	private String fileName;
	private String[] superClass;
	private List<Function>[] functions;
	private List<Function>[] constructors;
	private List<VarDefinition>[] vars;

	@SuppressWarnings("unchecked")
	Clazz(String fileName) {
		this.fileName = fileName;
		this.superClass = new String[0];
		this.functions = new List[] {
				new ArrayList<Function>(),
				new ArrayList<Function>(),
				new ArrayList<Function>(),
		};
		this.constructors = new List[] {
				new ArrayList<Function>(),
				new ArrayList<Function>(),
				new ArrayList<Function>(),
		};
		this.vars = new List[] {
				new ArrayList<VarDefinition>(),
				new ArrayList<VarDefinition>(),
				new ArrayList<VarDefinition>(),
		};
	}

	@SuppressWarnings("unchecked")
	Clazz(String fileName, String[] superClass) {
		this.fileName = fileName;
		this.superClass = superClass;
		this.functions = new List[] {
				new ArrayList<Function>(),
				new ArrayList<Function>(),
				new ArrayList<Function>(),
		};
		this.constructors = new List[] {
				new ArrayList<Function>(),
				new ArrayList<Function>(),
				new ArrayList<Function>(),
		};
		this.vars = new List[] {
				new ArrayList<VarDefinition>(),
				new ArrayList<VarDefinition>(),
				new ArrayList<VarDefinition>(),
		};
	}
	
	public int I(Privacy p) {
		switch (p) {
		case PUBLIC:
			return 0;
		case PROTECTED:
			return 1;
		}
		return 2;
	}

	public void addConstructor(Function func, Privacy p) {
		constructors[I(p)].add(func);
	}
	
	public void addFunction(Function func, Privacy p) {
		functions[I(p)].add(func);
	}
	
	public void addVar(VarDefinition var, Privacy p) {
		vars[I(p)].add(var);
	}

	public void writeOut(String string) {
		
	}

	public String getName() {
		return fileName;
	}

}
