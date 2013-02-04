package org.dreamcrushed.PortAmp;

import java.util.ArrayList;
import java.util.List;

public class Function {
	public String retType;
	public String name;
	public List<VarDefinition> parameters;
	public boolean isVirtual;
	
	public Function(String name) {
		this.name = name;
		parameters = new ArrayList<VarDefinition>();
		isVirtual = false;
	}
	
	public void setReturnType(String type) {
		this.retType = type;
	}
	
	public void addParameter(String type, String name) {
		parameters.add(new VarDefinition(type, name));
	}
	
	public void setVirtual() {
		isVirtual = true;
	}
	
}
