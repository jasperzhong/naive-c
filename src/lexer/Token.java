package lexer;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yuchen Zhong on 2018-10-12
 */
public class Token{
	
	// rules of C-like lang
	private static List<String> keywords = new ArrayList<String>();
	private static List<String> delimiters = new ArrayList<String>();
	private static List<String> operators = new ArrayList<String>();
	private static List<String> demicals = new ArrayList<String>();
	
	static {
		// add keywords
		Token.keywords.add("int");
		Token.keywords.add("void");
		Token.keywords.add("if");
		Token.keywords.add("else");
		Token.keywords.add("while");
		Token.keywords.add("return");
		
		Token.keywords.add("float");
		
		// add delimiters
		Token.delimiters.add(";");
		Token.delimiters.add(",");
		Token.delimiters.add("(");
		Token.delimiters.add(")");
		Token.delimiters.add("{");
		Token.delimiters.add("}");
		
		// decimal dot
		Token.demicals.add(".");
		
		// add operators
		Token.operators.add("+");
		Token.operators.add("-");
		Token.operators.add("*");
		Token.operators.add("/");
		Token.operators.add("=");
		Token.operators.add("==");
		Token.operators.add("<");
		Token.operators.add("<=");
		Token.operators.add(">");
		Token.operators.add(">=");
	}
	
	// a token of form: <token-name, attribute-value>
	private String token_name;
	private Integer attr_value;
	
	
	//constructor
	
	public Token() {}
	
	public Token(String token_name, Integer attr_value) {
		this.token_name = token_name;
		this.attr_value = attr_value;
	}
	
	
	// getter 
	
	public String getTokenName() {
		return token_name;
	}
	
	public Integer getAttrValue() {
		return attr_value;
	}
	
	
	// judge token type
	
	public static boolean isKeyword(String token) {
		return keywords.contains(token);
	}
	
	public static boolean isDelimiter(String token) {
		return delimiters.contains(token);
	}
	
	public static boolean isOperator(String token) {
		return operators.contains(token);
	}
	
	
	// toString 
	@Override
	public String toString() {
		if(attr_value == 0) {
			return token_name;
		}
		else {
			return token_name + ":" + attr_value;
		}
	}
}


