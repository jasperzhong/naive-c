package parser;

import java.util.TreeSet;

import javax.naming.spi.DirStateFactory.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import lexer.*;

public class LL1 {
	// Predict Table 
	public HashMap<String, GrammarRule> predict_map;
	public ArrayList<GrammarRule> grammar_list;
	public TreeSet<String> terminals;
	public TreeSet<String> non_terminals;
	
	public HashMap<String, ArrayList<String>> first_set;
	public HashMap<String, ArrayList<String>> follow_set;
	
	public Stack<String> stack;
	
	public GrammarNode root; // Grammar Tree root
	
	public Lexer lexer;
	
	public StringBuilder result;
	
	public LL1() {
		predict_map = new HashMap<String, GrammarRule>();
		grammar_list = new ArrayList<GrammarRule>();
		terminals = new TreeSet<String>();
		non_terminals = new TreeSet<String>();
		first_set = new HashMap<String, ArrayList<String>>();
		follow_set = new HashMap<String, ArrayList<String>>();
		
		stack = new Stack<String>();
		
		root = new GrammarNode("Program"); // start Symbol
		
		lexer = new Lexer();
		
		result = new StringBuilder();
	}
	
	
	// construct predict map
	public void buildPredictMap(String grammar) {
		String[] lines = grammar.split("\n");
		GrammarRule rule;
		String left, right;
		for(String line : lines) {
			left = line.split("->")[0].trim();
			right = line.split("->")[1].trim();
			rule = new GrammarRule(left, right.split(" "));
			grammar_list.add(rule);
			
			// get non_terminals
			non_terminals.add(left);
		}
		
		
		// get terminals
		String[] rights;
		for(String line : lines) {
			right = line.split("->")[1].trim();
			rights = right.split(" ");
			for(String r : rights) {
				if(non_terminals.contains(r)) {
					continue;
				}
				else {
					terminals.add(r);
				}
			}
		}
	}
	
	// judge whether X has 'X -> $' 
	private boolean hasNull(String left) {
		String[] rights;
		for(GrammarRule grammarRule : grammar_list) {
			if(grammarRule.getLeft().equals(left)) {
				rights = grammarRule.getRight();
				if(rights[0].equals("$")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	// build FIRST SET
	public void buildFirst() {
		ArrayList<String> first;
		
		// terminals' first set
		for(String terminal : terminals) {
			first = new ArrayList<String>();
			first.add(terminal);
			first_set.put(terminal, first);
		}
		
		for(String non_terminal : non_terminals) {
			first = new ArrayList<String>();
			first_set.put(non_terminal, first);
		}
		
		
		String[] rights;
		String left;
		boolean flag = true;
		while(true) {
			flag = true;
			for(GrammarRule grammarRule : grammar_list) {
				left = grammarRule.getLeft();
				rights = grammarRule.getRight();
				
				for(String right : rights) {	
					// X -> a... or X -> $ => add a or $ into FIRST(X)
					if(terminals.contains(right)) {
						if(!first_set.get(left).contains(right)) {
							first_set.get(left).add(right);
							flag = false;
						}
					} // X -> Y...  => add FIRST(Y) - $ into FIRST(X) 
					else if(non_terminals.contains(right)){
						first = first_set.get(right);
						for(String fi : first) {
							if(!fi.equals("$")) {
								if(!first_set.get(left).contains(fi)) {
									first_set.get(left).add(fi);	
									flag = false;
								}
							}
						}
					}
					else {
						System.err.println("YOU GOT SOME BUGS!");
					}
					
					// if X has 'X -> $', then turn to next right symbol
					if(hasNull(right)) {
						continue;
					}else {
						break;
					}
				}
			}
			
			if(flag) {
				break;
			}	
		}
	}
	
	
	// build FOLLOW SET
	public void buildFollow() {
		ArrayList<String> first;
		ArrayList<String> follow;
		// terminals' first set
		for(String non_terminal : non_terminals) {
			follow = new ArrayList<String>();
			follow_set.put(non_terminal, follow);
		}
		
		follow_set.get("Program").add("#"); // Start Symbol, default is S
		
		String[] rights;
		String left;
		String right;
		boolean flag = true;
		boolean flag2 = true;
		while(true) {
			flag = true;
			for(GrammarRule grammarRule : grammar_list) {
				left = grammarRule.getLeft();
				rights = grammarRule.getRight();
				
				for(int i = 0;i < rights.length; ++i) {
					right = rights[i];
					// B -> ...A... 
					if(non_terminals.contains(right)) {
						flag2 = true;
						for(int j = i + 1; j < rights.length; ++j) {
							first = first_set.get(rights[j]);
							for(String fi : first) {
								if(!follow_set.get(right).contains(fi) && !fi.equals("$")) {
									follow_set.get(right).add(fi);
									flag = false;
								}
							}
							
							if(hasNull(rights[j])) {
								continue;
							}else {
								flag2 = false;
								break;
							}
						} // end of rights from j = i + 1 to end
						
						// B -> ...A or B -> ...Ab && b => $ 
						if(flag2) {
							left = grammarRule.getLeft();
							follow = follow_set.get(left);
							for(String fo : follow){
								if(!follow_set.get(right).contains(fo)) {
									follow_set.get(right).add(fo);
									flag = false;
								}
							}
						}
						
					} // end of if non_terminals
				} // end of traverse rights
			} // end of traverse grammar list
			
			if(flag) {
				break;
			}
		}
	}
	
	// build Predict Table
	public void buildPredictMap() {
		ArrayList<String> first;
		ArrayList<String> rights_first;
		ArrayList<String> follow;
		String[] rights;
		String left;
		boolean flag = false;
		for(GrammarRule grammarRule : grammar_list) {
			left = grammarRule.getLeft();
			rights = grammarRule.getRight();
			
			rights_first = new ArrayList<String>();
			
			flag = false;
			for(String right : rights) {
				first = first_set.get(right);
				for(String fi : first) {
					if(!rights_first.contains(fi)) {
						if(fi.equals("$")) {
							flag = true;
						}else {
							rights_first.add(fi);							
						}
					}
				}
				
				if(hasNull(right)) {
					continue;
				}else {
					break;
				}
			}
			
			for(String fi : rights_first) {
				predict_map.put(left + "@" + fi, grammarRule);
			}
			
			// has A -> $
			if(flag) {
				rights_first.add("$");
				
				follow = follow_set.get(left);
				for(String fo : follow) {
					predict_map.put(left + "@" + fo, grammarRule);
				}
			}

		}
	}
	
	
	// syntactic analysis
	public boolean syntacticAnalysis(ArrayList<Token> input) {
		stack.push("#");
		stack.push("Program"); // start Symbol
		input.add(new Token("#", 0));
		
		result.append("·ÖÎöµÄ×Ö·û´®ÊÇ£º");
		result.append(input);
		result.append("\n");
		
		String top, key;
		Token in;
		int index = 0, len = input.size();
		GrammarRule grammarRule;
		String[] rights;
		int cnt = 0;
		GrammarNode cur = root, tmp = null;
		while(!stack.empty() && index < len) {
			top = stack.pop(); 
			in = input.get(index);
			
			if(top.equals(in.getTokenName())) {
				if(top.equals("#")) {
					break;
				}
				index++;
				continue;
			}
			
			key = top + "@" + in.getTokenName();
			
			// System.out.println(cnt + ":" + key);
			grammarRule = predict_map.get(key);
			if(grammarRule != null) {
				rights = grammarRule.getRight();
				
				if(rights[0].equals("$")) {
					continue; 
				}
				
				result.append(cnt);
				result.append(": ");
				result.append(grammarRule);
				result.append("\n");
				
				for(int i = rights.length - 1; i >= 0; --i) {
					stack.push(rights[i]);
					tmp = new GrammarNode(rights[i]);
					cur.children.add(tmp);
				}
				
				cur = tmp; // the last
				
				cnt++;
			}
			else {
				result.append("Reject!\n");
				return false;
			}
		}
		
		result.append("Accept!\n");
		return true;
	}
	
	public void display() {
		result.append("Syntactic Tree:\n");
		dfs(root, 0);
	}
	
	public void dfs(GrammarNode cur, int depth) {
		if(cur != null) {
			for(int i = 0;i < 5 * depth; ++i)
				result.append(" ");
			result.append(cur.name);
			result.append("\n");
			for(GrammarNode grammarNode : cur.children) {
				dfs(grammarNode, depth + 1);
			}
		}
	}
	
}
