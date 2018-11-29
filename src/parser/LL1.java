package parser;

import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LL1 {
	// Predict Table
	public HashMap<String, String> predict_map;
	public ArrayList<GrammarRule> grammar_list;
	public TreeSet<String> terminals;
	public TreeSet<String> non_terminals;
	
	public HashMap<String, ArrayList<String>> first_set;
	public HashMap<String, ArrayList<String>> follow_set;
	
	public LL1() {
		predict_map = new HashMap<String, String>();
		grammar_list = new ArrayList<GrammarRule>();
		terminals = new TreeSet<String>();
		non_terminals = new TreeSet<String>();
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
				if(non_terminals.contains(r) || r.equals("$")) {
					continue;
				}
				else {
					terminals.add(r);
				}
			}
		}
	}
	
	
	private void getFirst() {
		ArrayList<String> first;
		
		// terminals' first set
		for(String terminal : terminals) {
			first = new ArrayList<String>();
			first.add(terminal);
			first_set.put(terminal, first);
		}
		
		
		
	}
	
	
	private void getFollow() {
		
	}
	
	
	
	// syntactic analysis
	public String syntacticAnalysis(String input) {
		return "";
	}
	
	public static void main(String[] argv) {
		LL1 ll1 = new LL1();
		File file = new File("D:\\Coding\\Java\\naive-c\\src\\parser\\grammar.txt");
		RandomAccessFile randomAccessFile;
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
		
			while((line = randomAccessFile.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		ll1.buildPredictMap(stringBuilder.toString());
		
		System.out.println(ll1.grammar_list);
		System.out.println(ll1.terminals);
		System.out.println(ll1.non_terminals);
		
	}
	
}
