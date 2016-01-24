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
		 * Returns a String representing the general equation of the given function form.
		 * <p>
		 * <pre>
		 * {@code
		 * MathFunction.Type type = MathFunction.Type.QUADRATIC_VERTEX_FORM;
		 * String eqn = type.getBaseEquation("t");
		 * System.out.println(t);	// output: "a*(t - h)^2 + k"
		 * }
		 * </pre>
		 * 
		 * @param independentVariable 	the variable corresponding to the x-axis of the function's graph
		 * @return 						the general equation with independentVariable as x
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
	 * Creates a new MathFunction object. Use this to initialize variables of type <code>MathFunction</code>.
	 * <p>
	 * All <code>MathFunction</code>s' variables and coefficients, excluding the x-axis variable, are randomly generated.
	 * The range of permitted random values is bound within the <code>min</code> and <code>max</code> arguments. 
	 * <p>
	 * <pre>
	 * {@code
	 * MathFunction.Type type = MathFunction.Type.QUADRATIC_STANDARD_FORM;
	 * MathFunction func = new MathFunction(type, -10, 10);
	 * }
	 * </pre>
	 * 
	 * @param type	the type of this <code>MathFunction</code>
	 * @param min	lowest possible coefficent value
	 * @param max	highest possible coefficient value
	 */
	public MathFunction(Type type, int min, int max) {
		this.type = type;
		this.min = min;
		this.max = max;
		
		generateVariables();
		updateBaseEquation();
		
		updateEquation();
		
		determineBlankVariables();
		updatePartialEquation();
	}
	
	/**
	 * 
	 * Returns the general function form that this <code>MathFunction</code> object corresponds to.
	 * For a list of possible values, see <code>MathFunction.Type</code>. 
	 * 
	 * @return	the type of this <code>MathFunction</code>
	 */
	public MathFunction.Type getType() {
		return type;
	}
	
	/**
	 * 
	 * Returns the <code>String</code> representing this <code>MathFunction</code>'s equation.
	 * 
	 * @return	the equation of this <code>MathFunction</code> 
	 */
	public String getEquation() {
		return equation;
	}
	
	/**
	 * 
	 * Returns this <code>MathFunction</code>'s equation. The function will randomly select some of the variables for removal.
	 * <p>
	 * The removed variables can be replaced with either their names from the general equation or with an underscore (_). This option is set by <code>blankout</code> 
	 * <pre>
	 * {@code
	 * MathFunction.Type type = MathFunction.Type.LINEAR;
	 * MathFunction func = new MathFunction(type, -4, 4);
	 * String equation = func.getEquation();				// "3*x + 6"
	 * String partialEqn = func.getPartialEquation(false);	// "m*x + 6"
	 * String blankEqn = func.getPartialEquation(true);		// "_*x + 6"
	 * }
	 * </pre>
	 * 
	 * @param blankOut	the symbol to use in place of the removed variables
	 * @return			this <code>MathFunction</code>'s equation with some variables removed
	 */
	public String getPartialEquation(boolean blankOut) {
		String equation = partialEquation;
		
		if (blankOut) {
			equation = blankOut(equation);
		}
		
		return equation;
	}
	
	/**
	 * 
	 * Returns a <code>String</code> representing the x-axis variable used in this <code>MathFunction</code>'s equation.
	 * By default, the independent variable has a name of  <code>"x"</code>.
	 * 
	 * @return	the x-axis variable of this <code>MathFunction</code> 
	 */
	public String getIndependentVariable() {
		return independentVariable;
	}
	
	/**
	 * 
	 * Returns an array containing the names of the variables that have been removed from this <code>MathFunction</code>'s partial equation.
	 * 
	 * @return	an array of the variables removed from the equation
	 */
	public String[] getBlankVariables() {
		return blankVariables.toArray(new String[0]);
	}
	
	/**
	 * 
	 * Updates this <code>MathFunction</code>'s independent variable. Subsequent calls to <code>getEquation()</code>, 
	 * <code>getPartialEquation()</code>, and related functions will contain the new independent variable. 
	 * 
	 * @param name	the independent variable's new name
	 */
	public void setIndependentVariable(String name) {
		independentVariable = name;
		
		updateBaseEquation();
		updateEquation();
		updatePartialEquation();
	}

	/**
	 * 
	 * Returns a <code>List</code> containing the equation's pieces. Each removed variable is treated as a separate piece.
	 * <p>
	 * For example, the partial equation <code>"a*x^2 + 3*x - c"</code> would be split into the pieces <code>"a"</code>, 
	 * <code>"*x^2 + 3*x - "</code>, and <code>"c"</code> if <code>blankOut</code> were set to <code>false</code>.
	 * Otherwise, the pieces "a" and "c" would both be underscores.
	 * @param blankOut	whether to replace removed variables with underscores
	 * @return			a <code>List</code> containing the equation pieces
	 */
	public List<String> getSplitPartialEquation(boolean blankOut) {
		String eq = partialEquation;
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
	
	private boolean hasVariable(String name) {
		return variables.containsKey(name);
	}
	
	private void registerVariable(String name, Integer value) {
		if (!name.equals(independentVariable) && !hasVariable(name)) {
			variables.put(name, value);
		}
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

		equation = clean(equation);
	}
	
	private void updatePartialEquation() {
		HashSet<String> intactVariables = new HashSet<>(visibleVariables);
		Integer val;
		
		intactVariables.removeAll(blankVariables);
		
		partialEquation = baseEquation;		
		
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
			System.out.println(func.getEquation());
			System.out.println(func.getPartialEquation(false));
			System.out.println(func.getPartialEquation(true));

			i = ++i % funcTypes.length;
			s.nextLine();
		}
	}
	
}
