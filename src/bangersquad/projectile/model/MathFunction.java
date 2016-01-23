/**
 * 
 */
package bangersquad.projectile.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

import bangersquad.projectile.util.RandomNumberUtil;


/**
 * @author feilan
 *
 */
public class MathFunction {
	
	public enum Type {
		QUADRATIC_STANDARD_FORM("a*_^2 + b*_ + c"),
		QUADRATIC_FACTORED_FORM("a*(_ - s)*(_ - r)"),
		QUADRATIC_VERTEX_FORM("a*(_ - h)^2 + k");
		
		private final String baseEquation;
		
		/**
		 * 
		 * @param eq
		 */
		Type(String eq) {
			baseEquation = eq;
		}
		
		/**
		 * 
		 * @param independentVariable
		 * @return
		 */
		public String getBaseEquation(String independentVariable) {
			return baseEquation.replaceAll("_", independentVariable); 
		}
		
	}	
	
	private Type type;
	private Map<String, Integer> variables = new HashMap<>();
	private Set<String> blankVariables = new LinkedHashSet<>();
	private Set<String> visibleVariables = new HashSet<>();
	private String baseEquation;
	private String equation;
	private String partialEquation;
	private String independentVariable = "x";
	private String blankValue = "_";
	private int min;
	private int max;
	
	/**
	 * 
	 * @param type
	 */
	public MathFunction(Type type, int min, int max) {
		this.type = type;
		this.min = min;
		this.max = max;
		
		generateVariables();
		updateBaseEquation();
		
		updateEquation();
		
		determineBlankVariables();	// FIXME won't work if updateEquation() hasn't been called before
		updatePartialEquation();
	}
	
	public Type getType() {
		return type;
	}
	
	/**
	 * 
	 * @param format
	 * @return
	 */
	public String getEquation(boolean format) {
		return format ? formatEquation(equation) : equation;
	}
	
	/**
	 * 
	 * @param format
	 * @return
	 */
	public String getPartialEquation(boolean format, boolean blankOut) {
		String equation = partialEquation;
		
		if (format) {
			equation = formatEquation(equation);
		}
		
		if (blankOut) {
			equation = blankOut(equation);
		}
		
		return equation;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getIndependentVariable() {
		return independentVariable;
	}
	
	public String[] getBlankVariables() {
		return blankVariables.toArray(new String[0]);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setIndependentVariable(String name) {
		independentVariable = name;
		
		updateBaseEquation();
		updateEquation();
		updatePartialEquation();
	}
	
	/**
	 * 
	 * @param format
	 * @return
	 */
	public List<String> getSplitPartialEquation(boolean format, boolean blankOut) {
		String eq = format ? formatEquation(partialEquation) : partialEquation;
		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m = p.matcher(eq);
		List<String> equationParts = new ArrayList<>();
		int previousStart = 0;
		int previousEnd = 0;
		String part, previousPart;
		
		while (m.find()) {
			part = eq.substring(m.start(), m.end());
			
			if (hasVariable(part)) {
				previousEnd = m.start();
				
				if (previousEnd > 0) {
					previousPart = eq.substring(previousStart, previousEnd);
					equationParts.add(previousPart);
				}
				
				equationParts.add(blankOut ? blankOut(part) : part);
				
				previousStart = m.end();
			}
		}
		
		if (previousStart != eq.length()) {
			equationParts.add(eq.substring(previousStart, eq.length()));
		}
		
		return equationParts;
	}	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}
	
	private void registerVariable(String name, Integer value) {
		if (!name.equals(independentVariable) && !hasVariable(name)) {
			variables.put(name, value);
		}
	}
	
	// TODO: getFormattedEquation
	private String formatEquation(String equation) {
		// possible components:
		// regex
		// Unicode for superscripts
		
		// remove "*"
		
		// exponential:
		// keep "^"
		
		// everything else:
		// convert "^" and raised stuff to unicode superscript
		
		return equation;
	}
	
	private String clean(String exp) {
		exp = exp.replaceAll("\\+\\s*\\-", "- ");	// 3 + -3 ---> 3 - 3
		exp = exp.replaceAll("\\-\\s*\\-", "+ ");	// 3 - -3 ---> 3 + 3
		return exp;
	}
	
	private String blankOut(String exp) {
		StringBuilder regex = new StringBuilder();
		Iterator<String> iter = blankVariables.iterator();
		int i = 0;
		int j = blankVariables.size();
		
		while (iter.hasNext()) {
			regex.append(iter.next());
			
			if (i < j - 1) {
				regex.append('|');
			}
			
			i++;
		}

		return exp.replaceAll(regex.toString(), blankValue);
	}
	
	private void generateVariables() {
		int var;
		
		variables.clear();
		
		switch (this.type) {
		case QUADRATIC_FACTORED_FORM:	// f(x) = a(x - s)(x - r)
			// TODO: add coefficients for x
			// a
			do {
				var = RandomNumberUtil.randInt(min, max);
			} while (var == 0);
			registerVariable("a", var);
			
			// s
			var = RandomNumberUtil.randInt(min, max);
			registerVariable("s", var);

			// r
			do {
				var = RandomNumberUtil.randInt(min, max);
			} while (var == 0);
			registerVariable("r", var);
			
			break;
		case QUADRATIC_STANDARD_FORM:	// f(x) = ax^2 + bx + c
			// a
			do {
				var = RandomNumberUtil.randInt(min, max);
			} while (var == 0);
			registerVariable("a", var);
			
			// b
			var = RandomNumberUtil.randInt(min, max);
			registerVariable("b", var);

			// c
			var = RandomNumberUtil.randInt(min, max);
			registerVariable("c", var);
			
			break;
		case QUADRATIC_VERTEX_FORM:		// f(x) = a(x - h)^2 + k
			// a
			do {
				var = RandomNumberUtil.randInt(min, max);
			} while (var == 0);
			registerVariable("a", var);

			// h
			do {
				var = RandomNumberUtil.randInt(min, max);
			} while (var == 0);
			registerVariable("h", var);

			// k
			do {
				var = RandomNumberUtil.randInt(min, max);
			} while (var == 0);
			registerVariable("k", var);

			break;
		default:
			break;
		}				
	}
	
	private void determineBlankVariables() {
		Iterator<String> iter = visibleVariables.iterator();
		String name;
		int numReplaced = 0;
		int i = 0;
		int j = visibleVariables.size();
		
		blankVariables.clear();
		
		while (iter.hasNext()) {
			name = iter.next();
			
			if ((Math.random() > 0.5) || (numReplaced == 0 && i == j - 1)) {
				blankVariables.add(name);
				numReplaced++;
			}
			
			i++;
		}
	}

	private void updateBaseEquation() {
		int var;
		
		baseEquation = type.getBaseEquation(independentVariable);
		
		switch (this.type) {
		case QUADRATIC_FACTORED_FORM:	// f(x) = a(x - s)(x - r)
			// TODO: add coefficients for x
			// a
			var = variables.get("a");
			if (var == 1) {
				baseEquation = baseEquation.replaceAll("a\\*", "");
			} else if (var == -1) {
				baseEquation = baseEquation.replaceAll("a\\*", "-");
			} else {
				visibleVariables.add("a");
			}
			
			
			// s
			var = variables.get("s");
			if (var == 0) {
				baseEquation = baseEquation.replaceAll("\\(x - s\\)", "x");
			} else {
				visibleVariables.add("s");
			}			

			// r
			visibleVariables.add("r");
			
			break;
		case QUADRATIC_STANDARD_FORM:	// f(x) = ax^2 + bx + c
			// a
			var = variables.get("a");
			if (var == 1) {
				baseEquation = baseEquation.replaceAll("a\\*", "");
			} else if (var == -1) {
				baseEquation = baseEquation.replaceAll("a\\*", "-");
			} else {
				visibleVariables.add("a");
			}
			
			// b
			var = variables.get("b");
			if (var == 0) {
				baseEquation = baseEquation.replaceAll(" \\+ b\\*x", "");
			} else if (var == 1) {
				baseEquation = baseEquation.replaceAll("b\\*", "");
			} else if (var == -1) {
				baseEquation = baseEquation.replaceAll("b\\*", "-");
			} else {
				visibleVariables.add("b");
			}			

			// c
			var = variables.get("c");
			if (var == 0) {
				baseEquation = baseEquation.replaceAll(" \\+ c", "");
			} else {
				visibleVariables.add("c");
			}
			
			break;
		case QUADRATIC_VERTEX_FORM:		// f(x) = a(x - h)^2 + k
			// a
			var = variables.get("a");
			if (var == 1) {
				baseEquation = baseEquation.replaceAll("a\\*", "");
			} else if (var == -1) {
				baseEquation = baseEquation.replaceAll("a\\*", "-");
			} else {
				visibleVariables.add("a");
			}

			// h
			visibleVariables.add("h");

			// k
			visibleVariables.add("k");
			
			break;
		default:
			break;
		}
	}

	private void updateEquation() {
		equation = baseEquation;
		
		for (String var : visibleVariables) {
			equation = equation.replaceAll(var, Integer.toString(variables.get(var)));
		}
		
		// TODO: create variables to represent the coefficients, maybe inside MathFunctionType
		// TODO: to reduce repetitive code, see if a common behaviour can be defined for plugging in different types of variables

		equation = clean(equation);
	}
	
	private void updatePartialEquation() {
		HashSet<String> intactVariables = new HashSet<>(visibleVariables);
		Integer val;
		
		intactVariables.removeAll(blankVariables);
		
		partialEquation = baseEquation;		
		
		// plug in
		for (String var : intactVariables) {
			val = variables.get(var);
			partialEquation = partialEquation.replaceAll(var, Integer.toString(val));
		}
		
		partialEquation = clean(partialEquation);		
	}
	
	static public void test(MathFunction.Type... types) {
		Scanner s = new Scanner(System.in);
		MathFunction func;
		MathFunction.Type[] funcTypes = (types.length == 0) ? MathFunction.Type.values() : types;
		int i = 0;
		
		while (true) {
			func = new MathFunction(funcTypes[i], -10, 10);
			System.out.println(func.getEquation(false));
			System.out.println(func.getPartialEquation(false, false));
			System.out.println(func.getPartialEquation(false, true));

			i = ++i % funcTypes.length;
			s.nextLine();
		}
	}
	
}
