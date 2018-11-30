package parser;

import java.util.ArrayList;


public class GrammarNode {
	public String name; // non-terminal name
	public ArrayList<GrammarNode> children; 
	
	public GrammarNode(String name) {
		this.name = name;
		this.children = new ArrayList<GrammarNode>();
	}
}
