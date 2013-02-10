package org.dreamcrushed.PortAmp;

import java.util.Stack;

// TODO/KNOWN UNSUPPORTED LIST
// enums
// multi-line member variable initialization
// array member initialization
// interfaces
// Comments an definition lines (like class of method defs)
public class JavaHandler extends FileHandler {

	protected Stack<Parser> parsers = new Stack<Parser>();
	protected DirManager currentDirManager;

	// There are far better ways to parse files and seek out the structuring
	// but this was quick and easy.
	// TODO: implement better java parser
	@Override
	protected void parseLine(String line, DirManager dirManager) {
		this.currentDirManager = dirManager;
		String noStrings = line.replaceAll("\".*\"", "").replaceAll("\'.*\'", "").replaceAll("//.*$", "");
		//System.out.println(parsers.size() + ": " + line);
		if (noStrings.contains(" class ")) {
			String className = line.replaceAll(
					"^.*\\sclass\\s([A-Za-z0-9]*)\\s.*\\{", "$1");
			String[] superClasses = new String[0];
			if (noStrings.contains(" extends ")) {
				String extending = line.replaceAll("^.*extends(.*)\\{.*$","$1");
				//System.out.println("Extending: " + extending);
				String[] list = extending.replaceAll("implements", ",").split(",");
				superClasses = new String[list.length];
				for (int i = 0; i < list.length; i++) {
					superClasses[i] = list[i].replaceAll("\\s", "");
				}
			} else if (noStrings.contains(" implements ")) {
				String extending = line.replaceAll("^.*implements(.*)\\{.*$","$1");
				//System.out.println("Extending: " + extending);
				String[] list = extending.split(",");
				superClasses = new String[list.length];
				for (int i = 0; i < list.length; i++) {
					superClasses[i] = list[i].replaceAll("\\s", "");
				}
			}
			//System.out.println("Push Class");
			String classPrint = className + " :";
			for (String classS : superClasses) {
				classPrint = classPrint + " " + classS;
			}
			parsers.push(new ClassParser(dirManager.getAddClass(className, superClasses)));
			System.out.println("Class: " + classPrint);
		} else if (noStrings.contains("}")) {
			if (parsers.size() > 0) {
				//System.out.println("Popping From: " + line);
				//System.out.println("Pop " + parsers.peek().getClass().getName());
				// This needs to avoid } inside strings
				parsers.peek().handleLine(line.substring(0, line.indexOf('}'))); 
				parsers.pop();
				if (parsers.size() > 0) {
					// This needs to avoid } inside strings
					parsers.peek().handleLine(line.substring(line.indexOf('}') + 1));
				} else {
					String l = line.substring(line.indexOf('}') + 1);
					if (l.replaceAll("\\s", "").length() > 0) {
						System.out.println("Unparsable Line: " + l);
					}
				}
			} else {
				String l = line.substring(line.indexOf('}') + 1);
				if (l.replaceAll("\\s", "").length() > 0) {
					System.out.println("Unparsable Line: " + l);
				}
				//System.out.println("Unparsable Line: " + line.substring(line.indexOf('}') + 1));
			}
		} else {
			if (parsers.size() > 0) {
				parsers.peek().handleLine(line);
			} else {
				//System.out.println("Unparsable Line: " + line);
			}
		}
	}

	@Override
	protected String baseName(String name) {
		return name.replaceAll("\\.java", "");
	}

	protected abstract class Parser {
		public abstract void handleLine(String line);
	}

	protected class ClassParser extends Parser {
		private Clazz myClass;

		public ClassParser(Clazz clazz) {
			this.myClass = clazz;
		}

		@Override
		public void handleLine(String line) {
			if ((line.contains("{") || line.contains("abstract")) && line.contains("(")) {
				//System.out.println("Def Detected: " + line);
				String preDef = line.substring(0, line.indexOf('('));
				String[] split = preDef.split("\\s");
				Function function = new Function(split[split.length - 1]);
				Privacy mode = Privacy.PROTECTED;
				for (int i = 0; i < split.length - 1; i++) {
					if (split[i].equals("public")) {
						mode = Privacy.PUBLIC;
						System.out.println("Mode: PUBLIC");
					} else if (split[i].equals("protected")) {
						mode = Privacy.PROTECTED;
						System.out.println("Mode: PROTECTED");
					} else if (split[i].equals("private")) {
						mode = Privacy.PRIVATE;
						System.out.println("Mode: PRIVATE");
					} else {
						function.setReturnType(split[i]);
					}
				}
				
				String defs = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
				if (defs.length() > 0) {
					for (String param : defs.split(",")) {
						String[] s = param.split("\\s");
						VarDefinition def = new VarDefinition(s);
						function.addParameter(def);
					}
				}
				if (function.name.equals(myClass.getName())) {
					System.out.println("Constructor: " + function);
					myClass.addConstructor(function, mode);
				} else {
					System.out.println("Function: " + function);
					myClass.addFunction(function, mode);
				}
				
				//System.out.println("Push Function");
				parsers.push(new FunctionParser(function));
			} else if (line.contains("{")) {
				//System.out.println("Push Function Extra");
				parsers.push(parsers.peek());
			} else if (line.contains(";")) {
				String l = line.substring(0, line.indexOf(';'));
				String initializer = null;;
				if (l.contains("=")) {
					initializer = l.substring(l.indexOf('=') + 1);
					l = l.substring(0, l.indexOf('='));
				}
				String[] split = l.split("\\s");
				String varType = "void";
				Privacy mode = Privacy.PROTECTED;
				for (int i = 0; i < split.length - 1; i++) {
					if (split[i].equals("public")) {
						mode = Privacy.PUBLIC;
						System.out.println("Mode: PUBLIC");
					} else if (split[i].equals("protected")) {
						mode = Privacy.PROTECTED;
						System.out.println("Mode: PROTECTED");
					} else if (split[i].equals("private")) {
						mode = Privacy.PRIVATE;
						System.out.println("Mode: PRIVATE");
					} else {
						varType = split[i];
					}
				}
				String name = split[split.length - 1];
				VarDefinition var = new VarDefinition(varType, name, initializer);
				System.out.println("Var: " + mode + " " + var + ";");
				myClass.addVar(var, mode);
			}
		}
	}

	protected class FunctionParser extends Parser {
		public Function myFunction;

		public FunctionParser(Function function) {
			this.myFunction = function;
		}

		@Override
		public void handleLine(String line) {
			String noStrings = line.replaceAll("\".*\"", "");
			if (noStrings.contains("{")) {
				//System.out.println("Push Function Extra");
				parsers.push(this);
			}
			if (line.replaceAll("\\s", "").length() > 0) {
				myFunction.addCode(line);
			}
		}

	}
}
