/**
 * 
 */
package bangersquad.projectile.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
	private List<String> missingVariables = new ArrayList<>();
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
		equation = partialEquation = type.getBaseEquation(independentVariable);
		
		generateEquation();
		generatePartialEquation();
	}
	
	public MathFunctionType getType() {
		return type;
	}
	
	/**
	 * 
	 * @param format
	 * @return
	 */
	// TODO: return a copy of string
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
	// TODO: setIndependentVariable()
	public void setIndependentVariable(String name) {
		// independentVariable = "x";
		// set equation and partialEquation back to baseEquation
		// for equation plug in existing values from variables
		// for partialEquation plug in variables that weren't blanked out 
		// (find out which by getting difference of variables.keySet() and set for blanked-out variables) 
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
		
		for (int i = 0, j = missingVariables.size(); i < j; i++) {
			regex.append(missingVariables.get(i));
			
			if (i < j - 1) {
				regex.append('|');
			}
		}
		
		return exp.replaceAll(regex.toString(), blankValue);
	}
	
	private void generateEquation() {
		int var;
		
		// TODO: break into clear steps:
		//		 -determining variable values
		//       -plugging in the values
		// TODO: create variables to represent the coefficients, maybe inside MathFunctionType
		// TODO: to reduce repetitive code, see if a common behaviour can be defined for plugging in different types of variables
		// TODO: prevent x from being stored in variables map
		switch (this.type) {
		case QUADRATIC_FACTORED_FORM:	// f(x) = a(x - s)(x - r)
			// TODO: add coefficients for x
			// a
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("a", var);
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(variables.get("a")));
			}
			
			
			// s
			var = RandomNumberUtil.getRandomInt(min, max);
			registerVariable("s", var);
			if (var == 0) {
				equation = equation.replaceAll("\\(x - s\\)", "x");
			} else {
				equation = equation.replaceAll("s", Integer.toString(variables.get("s")));
			}			

			// r
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("r", var);
			equation = equation.replaceAll("r", Integer.toString(variables.get("r")));
			
			break;
		case QUADRATIC_STANDARD_FORM:	// f(x) = ax^2 + bx + c
			// a
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("a", var);
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(variables.get("a")));
			}
			
			// b
			var = RandomNumberUtil.getRandomInt(min, max);
			registerVariable("b", var);
			if (var == 0) {
				equation = equation.replaceAll(" \\+ b\\*x", "");
			} else if (var == 1) {
				equation = equation.replaceAll("b\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("b\\*", "-");
			} else {
				equation = equation.replaceAll("b", Integer.toString(variables.get("b")));
			}			

			// c
			var = RandomNumberUtil.getRandomInt(min, max);
			registerVariable("c", var);
			if (var == 0) {
				equation = equation.replaceAll(" \\+ c", "");
			} else {
				equation = equation.replaceAll("c", Integer.toString(variables.get("c")));
			}
			
			break;
		case QUADRATIC_VERTEX_FORM:		// f(x) = a(x - h)^2 + k
			// a
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("a", var);
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(variables.get("a")));
			}

			// h
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("h", var);
			equation = equation.replaceAll("h", Integer.toString(variables.get("h")));

			// k
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			registerVariable("k", var);
			equation = equation.replaceAll("k", Integer.toString(variables.get("k")));

			break;
		default:
			break;
		}		
		
		equation = clean(equation);
	}
	
	// TODO: store blanked out variable names into a set
	private void generatePartialEquation() {
		// option 1: start from equation var
		// have a way of distinguishing variables from constants
		// remove some of the variables
		
		// more possibitlies with this version
		// option 2: start from BASE_EQUATION
		// plug in all existing coefficients (so that even ones and zeros show)
		// remove some ones and zeros
		// remove some of the remaining variables
		Set<Map.Entry<String, Integer>> entries = variables.entrySet();		
		String name;
		Integer value;
		int numReplaced = 0;
		int i = 0;
		
		missingVariables.clear();
		
		// FIXME: if last var is a constant equal to 0, it gets plugged if nothing before it has
		// FIXME: sometimes nothing gets plugged in (don't know why)
		for (Map.Entry<String, Integer> var : entries) {
			name = var.getKey();
			value = var.getValue();
			
			if ((Math.random() > 0.5 && value != 0 && Math.abs(value) != 1) || (numReplaced == 0 && i == entries.size() - 1)) {
				partialEquation = partialEquation.replaceAll(name, Integer.toString(value));				
				numReplaced++;
			} else {
				missingVariables.add(name);
			}
			
			i++;
		}
		
		partialEquation = clean(partialEquation);
	}
	
}
