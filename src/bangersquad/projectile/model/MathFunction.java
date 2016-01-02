/**
 * 
 */
package bangersquad.projectile.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import bangersquad.projectile.util.RandomNumberUtil;

/**
 * @author feilan
 *
 */
public class MathFunction {
	
	/*
	 * usage:
	 * plot the function (ez)
	 * blank out some random coefficients
	 * 
	 */
	static public int min = -10;
	static public int max = 10;
	
	private MathFunctionType type;
	private Map<String, Integer> variables = new HashMap<>();
	private String equation;
	private String partialEquation;
	
	public MathFunction(MathFunctionType type) {
		this.type = type;
		equation = partialEquation = type.BASE_EQUATION;
		
		generateEquation();
		generatePartialEquation();
	}

	private String clean(String exp) {
		exp = exp.replaceAll("\\+\\s*\\-", "- ");	// 3 + -3 ---> 3 - 3
		exp = exp.replaceAll("\\-\\s*\\-", "+ ");	// 3 - -3 ---> 3 + 3
		return exp;
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
			variables.put("a", var);
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(variables.get("a")));
			}
			
			
			// s
			var = RandomNumberUtil.getRandomInt(min, max);
			variables.put("s", var);
			if (var == 0) {
				equation = equation.replaceAll("\\(x - s\\)", "x");
			} else {
				equation = equation.replaceAll("s", Integer.toString(variables.get("s")));
			}			

			// r
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			variables.put("r", var);
			equation = equation.replaceAll("r", Integer.toString(variables.get("r")));
			
			break;
		case QUADRATIC_STANDARD_FORM:	// f(x) = ax^2 + bx + c
			// a
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			variables.put("a", var);
			if (var == 1) {
				equation = equation.replaceAll("a\\*", "");
			} else if (var == -1) {
				equation = equation.replaceAll("a\\*", "-");
			} else {
				equation = equation.replaceAll("a", Integer.toString(variables.get("a")));
			}
			
			// b
			var = RandomNumberUtil.getRandomInt(min, max);
			variables.put("b", var);
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
			variables.put("c", var);
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
			variables.put("a", var);
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
			variables.put("h", var);
			equation = equation.replaceAll("h", Integer.toString(variables.get("h")));

			// k
			do {
				var = RandomNumberUtil.getRandomInt(min, max);
			} while (var == 0);
			variables.put("k", var);
			equation = equation.replaceAll("k", Integer.toString(variables.get("k")));

			break;
		default:
			break;
		}		
		
		equation = clean(equation);
	}
	
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
		
		// FIXME: if last var is a constant equal to 0, it gets plugged if nothing before it has
		// FIXME: sometimes nothing gets plugged in (don't know why)
		System.out.println(entries.size());
		for (Map.Entry<String, Integer> var : entries) {
			name = var.getKey();
			value = var.getValue();
			
			if ((Math.random() > 0.5 && value != 0 && Math.abs(value) != 1) || (numReplaced == 0 && i == entries.size() - 1)) {
				partialEquation = partialEquation.replaceAll(var.getKey(), Integer.toString(var.getValue()));
				numReplaced++;
			}
			
			i++;
		}
		
		partialEquation = clean(partialEquation);
	}
	
	public MathFunctionType getType() {
		return type;
	}
	
	public String getEquation() {
		return equation;
	}
	
	public String getPartialEquation() {
		return partialEquation;
	}
	
}
