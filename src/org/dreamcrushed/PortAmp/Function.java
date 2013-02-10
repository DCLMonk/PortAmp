package org.dreamcrushed.PortAmp;

import java.util.ArrayList;
import java.util.List;

public class Function {
	public String retType;
	public String name;
	public List<VarDefinition> parameters;
	public List<String> code;
	public boolean isVirtual;
	
	public Function(String name) {
		this.name = name;
		retType = "";
		parameters = new ArrayList<VarDefinition>();
		code = new ArrayList<String>();
		isVirtual = false;
	}
	
	public void setReturnType(String type) {
		this.retType = type;
	}
	
	public void addParameter(String type, String name) {
		parameters.add(new VarDefinition(type, name));
	}

	public void addParameter(VarDefinition def) {
		parameters.add(def);
	}
	
	public void setVirtual() {
		isVirtual = true;
	}

	public void addCode(String line) {
		code.add(line);
	}
	
	@Override
	public String toString() {
		String func = retType + " " + name + "(";
		int i;
		if (parameters.size() > 0) {
			for (i = 0; i < parameters.size() - 1; i++) {
				func = func + parameters.get(i) + ", ";
			}
			func = func + parameters.get(i) + ");";
		} else {
			func = func + ");";
		}
		return func;
	}
	
}
