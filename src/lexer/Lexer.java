package lexer;

import java.util.ArrayList;


/**
 * Created by Yuchen Zhong on 2018-10-12
 */
public class Lexer {
	// Since many tokens like +, -, int, void etc. have no value, 
	// so replace it with a NO_VALUE constant
	static final int NO_VALUE = 0;
	
	// A status means current char is out of comments.
	static final int OUT_CMT = 0;
		
	// A status means current char is between "//" comments.
	static final int IN_CMT = 1;
	
	// A status means current char is between "/*" comments.
	static final int IN_LCMT = 2;
	
	// line counter
	int line_cnt = 1;
	
	// Symbol table contains strings of identifiers and numbers.
	// It allows symbols of the same name simultaneously in this list.
	public ArrayList<String> symbol_table = new ArrayList<String>(); 
	
	// Token list contains token of form <token-name, attribute-value>.
	public ArrayList<Token> token_list = new ArrayList<Token>();  
	
	
	// getter 
	
	public String getSymbolTable(){
		StringBuilder sb = new StringBuilder("Symbol Table: ");
		for(String string:symbol_table) {
			sb.append(string);
			sb.append(", ");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public String getTokenList(){
		StringBuilder sb = new StringBuilder("Token List:");
		for(Token token:token_list) {
			sb.append("<");
			sb.append(token.toString());
			sb.append(">, ");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	
	// clear
	
	public void clear() {
		line_cnt = 0;
		symbol_table.clear();
		token_list.clear();
	}
	
	
	// helper functions
	
	private boolean isDigit(char ch) {
		if(ch >= '0' && ch <= '9')
			return true;
		return false;
	}
	
	private boolean isLetter(char ch) {
		if(ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z')
			return true;
		return false;
	}
	

	private boolean isIdentifier(String input) {
		int index = 0;
		
		// first character must be letter
		if(isLetter(input.charAt(index))) {
			for(++index; index < input.length(); ++index) {
				// identifier should only contain digits or letters.
				if(!(isDigit(input.charAt(index)) || 
						isLetter(input.charAt(index))))
					return false;
			}
		}
		else {
			return false;
		}
		
		return true;
	}
	
	private boolean isNumber(String input) {
		// decimal dot counter
		int dot_cnt = 0;
		
		for(int index = 0; index < input.length(); ++index) {
			// decimal dot can be everywhere but 
			if(input.charAt(index) == '.') {
				dot_cnt += 1;
				if(dot_cnt > 1) {
					return false;
				}
				continue;
			}
				
			if(!isDigit(input.charAt(index)))
				return false;
		}
		return true;
	}
	
	
	// lexical analysis main function
	public String lexAnalysis(String input) {
		// token candidate
		StringBuilder token = new StringBuilder();
		
		// "in comment" means current character belongs to comments.
		int in_comment = OUT_CMT;
		
		String[] lines = input.split("\n");
		
		// main loop 
		// for each line
		for(String line: lines) {
			// for each char in the line
			for(int index = 0;index < line.length(); ++index) {
				char cur_char = line.charAt(index);
				
				// ignore white spaces
				if(cur_char == ' ' || cur_char == '\t') {
					continue;
				}
				
				// candidate gets longer by one char.
				token.append(cur_char);
				String token_string = token.toString();
				
				// main if-else
				if(token_string.equals("#")) {
					Token item = new Token(token_string, NO_VALUE);
					token_list.add(item);
					
					// clear buffer of candidate string
					token.setLength(0);
				}
				else if(token_string.equals("//")) {
					Token item = new Token(token_string, NO_VALUE);
					token_list.add(item);
					token.setLength(0);
					
					in_comment = IN_CMT;  // in "//" comments
				}
				else if(token_string.equals("/*")) {
					Token item = new Token(token_string, NO_VALUE);
					token_list.add(item);
					token.setLength(0);
					
					in_comment = IN_LCMT;  // between /* comments */
				}
				else if(token_string.equals("*/")) {
					Token item = new Token(token_string, NO_VALUE);
					token_list.add(item);
					token.setLength(0);
					
					in_comment = OUT_CMT;  // out of comments
				}
				else if(Token.isDelimiter(token_string)) {
					// when out of comments
					if(in_comment == OUT_CMT) {
						Token item = new Token(token_string, NO_VALUE);
						token_list.add(item);
					}
					
					token.setLength(0);
				}
				else if(Token.isOperator(token_string)) {
					// when next char is '=', the current char is not end yet.
					if(index + 1 < line.length() && 
						line.charAt(index + 1) == '=') {
						continue;
					}
					
					// when current char is '/' and next char is '/' or '*',
					// the current '/' is not end yet.
					if(token_string.equals("/") && 
						(line.charAt(index+1) == '/' || 
							line.charAt(index + 1) == '*')) {
						continue;
					}
					
					// when current char is '*' and next char is '/',
					// the current '*' is not end yet.
					if(token_string.equals("*") && 
							line.charAt(index + 1) == '/') {
						continue;
					}
					
					// when out of comments
					if(in_comment == OUT_CMT) {
						Token item = new Token(token_string, NO_VALUE);
						token_list.add(item);
					}

					token.setLength(0);
				}
				else if(Token.isKeyword(token_string)) {
					// check the next char
					if(index + 1 < line.length()) {
						char next_ch = line.charAt(index + 1);
						// then it is not a keyword
						if(isDigit(next_ch) || isLetter(next_ch)) 
							continue;
					}
					
					// when out of comments
					if(in_comment == OUT_CMT) {
						Token item = new Token(token_string, NO_VALUE);
						token_list.add(item);
					}

					token.setLength(0);
				}
				else if(isIdentifier(token_string)) {
					// check the next char
					if(index + 1 < line.length()) {
						char next_ch = line.charAt(index + 1);
						// then it is not end yet
						if(isDigit(next_ch) || isLetter(next_ch)) 
							continue;
					}
					
					if(in_comment == OUT_CMT) {
						symbol_table.add(token_string);
						Token item = new Token("ID", symbol_table.size());
						token_list.add(item);
					}
						
					token.setLength(0);
				}
				else if(isNumber(token_string)) {
					// check the next char
					if(index + 1 < line.length()) {
						char next_ch = line.charAt(index + 1);
						// then it is not end yet
						if(isDigit(next_ch) || isLetter(next_ch) || next_ch == '.') 
							continue;
					}
					
					if(in_comment == OUT_CMT) {
						symbol_table.add(token_string);
						Token item = new Token("NUMBER", symbol_table.size());
						token_list.add(item);
					}
						
					token.setLength(0);
				}
				else {
					// Run here? This means that C-like language source code has some errors...
					// For now I just deal with it with simple error msg.
					String error = "Fatal: Line " + line_cnt + ", \"" + token_string + "\" is undefined!\n" +
								   "Fatal: Lexical Analysis Terminated!\n";

					return error;
				} // end of if
			} // end of line
			line_cnt += 1;
			
		} // end of input

		
		return "OK";
	}
	
	
}
