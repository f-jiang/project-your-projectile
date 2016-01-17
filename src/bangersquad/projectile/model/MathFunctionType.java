package bangersquad.projectile.model;

public enum MathFunctionType {
	QUADRATIC_STANDARD_FORM("a*_^2 + b*_ + c"),
	QUADRATIC_FACTORED_FORM("a*(_ - s)*(_ - r)"),
	QUADRATIC_VERTEX_FORM("a*(_ - h)^2 + k");
	
	private final String baseEquation;
	
	/**
	 * 
	 * @param eq
	 */
	MathFunctionType(String eq) {
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
