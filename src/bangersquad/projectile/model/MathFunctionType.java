package bangersquad.projectile.model;

public enum MathFunctionType {
	QUADRATIC_STANDARD_FORM("f(x) = a*x^2 + b*x + c"),
	QUADRATIC_FACTORED_FORM("f(x) = a*(x - s)*(x - r)"),
	QUADRATIC_VERTEX_FORM("f(x) = a*(x - h)^2 + k");
	
	public final String BASE_EQUATION;
	
	MathFunctionType(String eq) {
		BASE_EQUATION = eq;
	}
}
