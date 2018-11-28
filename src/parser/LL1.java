package parser;

import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;


public class LL1 {
	// Predict Table
	private HashMap<String, String> predict_map;
	private ArrayList<GrammarRule> grammar_list;
	private TreeSet<String> terminals;
	private TreeSet<String> non_terminals;
	
	public LL1() {
		predict_map = new HashMap<String, String>();
		grammar_list = new ArrayList<GrammarRule>();
		terminals = new TreeSet<String>();
		non_terminals = new TreeSet<String>();
	}
	
	
	private void getFirst() {
	
	}
	
	
	private void getFollow() {
		
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
			
			// left is non_terminal
			non_terminals.add(left);
			
		}
	}
	
	// syntactic analysis
	public String syntacticAnalysis(String input) {
		return "";
	}
	
}
