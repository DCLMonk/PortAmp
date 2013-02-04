package org.dreamcrushed.PortAmp;


public class JavaHandler extends FileHandler {

	@Override
	protected void parseLine(String line, Clazz clazz) {

	}
	
	@Override
	protected String baseName(String name) {
		return name.replaceAll("\\.java", "");
	}
}
