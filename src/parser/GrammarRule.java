package parser;

public class GrammarRule {
	private String left;
	private String[] right;
	
	public GrammarRule(String left, String[] right) {
		this.left = left; 
		this.right = right;
	}
	
	public String getLeft() {
		return left;
	}
	
	public String[] getRight() {
		return right;
	}
}
