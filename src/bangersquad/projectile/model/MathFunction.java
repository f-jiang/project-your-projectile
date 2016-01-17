/**
 * 
 */
package bangersquad.projectile.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
	
	/**
	 * 
	 */
	public static int min = -10;
	
	/**
	 * 
	 */
	public static int max = 10;
	
	private MathFunctionType type;
	private Map<String, Integer> variables = new HashMap<>();
	private Set<String> missingVariables = new LinkedHashSet<>();
	private Set<String> visibleVariables = new HashSet<>();
	private String equation;
	private String partialEquation;
	private String independentVariable = "x";
	private String blankValue = "_";
	
	/**
	 * 
	 * @param type
	 */
	public MathFunction(MathFunctionType type) {
		this.type = type;
		
		generateVariables();
		updateEquation();
		
		determineMissingVariables();	// FIXME won't work if updateEquation() hasn't been called before
		updatePartialEquation();
	}
	
	public MathFunctionType getType() {
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
	
	public String[] getMissingVariables() {
		return missingVariables.toArray(new String[0]);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setIndependentVariable(String name) {
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
		Iterator<String> iter = missingVariables.iterator();
		int i = 0;
		int j = missingVariables.size();
		
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
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("a", var);
			
			// s
			var = RandomNumberUtil.getRandomInt(min, max);
			registerVariable("s", var);

			// r
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("r", var);
			
			break;
		case QUADRATIC_STANDARD_FORM:	// f(x) = ax^2 + bx + c
			// a
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("a", var);
			
			// b
			var = RandomNumberUtil.getRandomInt(min, max);
			registerVariable("b", var);

			// c
			var = RandomNumberUtil.getRandomInt(min, max);
			registerVariable("c", var);
			
			break;
		case QUADRATIC_VERTEX_FORM:		// f(x) = a(x - h)^2 + k
			// a
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("a", var);

			// h
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("h", var);

			// k
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("k", var);

			break;
		default:
			break;
		}				
	}
	
	private void determineMissingVariables() {
		Iterator<String> iter = visibleVariables.iterator();
		String name;
		int numReplaced = 0;
		int i = 0;
		int j = visibleVariables.size();
		
		missingVariables.clear();
		
		while (iter.hasNext()) {
			name = iter.next();
			
			if ((Math.random() > 0.5) || (numReplaced == 0 && i == j - 1)) {
				missingVariables.add(name);
				numReplaced++;
			}
			
			i++;
		}
	}

	private void updateEquation() {
		int var;
		
		equation = type.getBaseEquation(independentVariable);
		
		// TODO: create variables to represent the coefficients, maybe inside MathFunctionType
		// TODO: to reduce repetitive code, see if a common behaviour can be defined for plugging in different types of variables
		switch (this.type) {
		case QUADRATIC_FACTORED_FORM:	// f(x) = a(x - s)(x - r)
			// TODO: add coefficients for x
			// a
			var = variables.get("a");
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(var));
				visibleVariables.add("a");
			}
			
			
			// s
			var = variables.get("s");
			if (var == 0) {
				equation = equation.replaceAll("\\(x - s\\)", "x");
			} else {
				equation = equation.replaceAll("s", Integer.toString(var));
				visibleVariables.add("s");
			}			

			// r
			var = variables.get("r");
			equation = equation.replaceAll("r", Integer.toString(var));
			visibleVariables.add("r");
			
			break;
		case QUADRATIC_STANDARD_FORM:	// f(x) = ax^2 + bx + c
			// a
			var = variables.get("a");
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(var));
				visibleVariables.add("a");
			}
			
			// b
			var = variables.get("b");
			if (var == 0) {
				equation = equation.replaceAll(" \\+ b\\*x", "");
			} else if (var == 1) {
				equation = equation.replaceAll("b\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("b\\*", "-");
			} else {
				equation = equation.replaceAll("b", Integer.toString(var));
				visibleVariables.add("b");
			}			

			// c
			var = variables.get("c");
			if (var == 0) {
				equation = equation.replaceAll(" \\+ c", "");
			} else {
				equation = equation.replaceAll("c", Integer.toString(var));
				visibleVariables.add("c");
			}
			
			break;
		case QUADRATIC_VERTEX_FORM:		// f(x) = a(x - h)^2 + k
			// a
			var = variables.get("a");
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(var));
				visibleVariables.add("a");
			}

			// h
			var = variables.get("h");
			equation = equation.replaceAll("h", Integer.toString(var));
			visibleVariables.add("h");

			// k
			var = variables.get("k");
			equation = equation.replaceAll("k", Integer.toString(var));
			visibleVariables.add("k");
			
			break;
		default:
			break;
		}
		
		equation = clean(equation);
	}
	
	// FIXME 0s and 1s are shown in partial equation (don't plug into base equation, might need to refactor class)
	private void updatePartialEquation() {
		HashSet<String> intactVariables = new HashSet<>(variables.keySet());
		Integer val;
		
		intactVariables.removeAll(missingVariables);
		
		partialEquation = type.getBaseEquation(independentVariable);		
		
		// plug in
		for (String var : intactVariables) {
			val = variables.get(var);
			partialEquation = partialEquation.replaceAll(var, Integer.toString(val));
		}
		
		partialEquation = clean(partialEquation);		
	}
	
}
