package org.dreamcrushed.PortAmp;

public class VarDefinition {
	public String type;
	public String name;
	public String initializer;
	
	public VarDefinition(String type, String name) {
		this.type = type;
		this.name = name;
		this.initializer = null;
	}

	public VarDefinition(String[] split) {
		if (split.length > 1) {
			type = split[split.length - 2];
			name = split[split.length - 1];
			this.initializer = null;
		} else {
			System.out.println("Too Few Informations: " + split.length);
			for (String x : split) {
				System.out.println(x);
			}
		}
	}
	
	public VarDefinition(String type, String name, String initializer) {
		this.type = type;
		this.name = name;
		this.initializer = initializer;
	}

	@Override
	public String toString() {
		if (initializer != null) {
			return type + " " + name + " = " + initializer;
		}
		return type + " " + name;
	}
}
