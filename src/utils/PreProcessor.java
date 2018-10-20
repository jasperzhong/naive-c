package utils;

public class PreProcessor {
	
    public PreProcessor() {  	
    	
    }
    
    // Check whether the input file is like "xxx.cl".
    public boolean CheckExt(String fileName) {
		String ext_name;
		int dot_pos = fileName.lastIndexOf('.');
		boolean flag = true;
		if((dot_pos > -1) && (dot_pos < (fileName.length() - 1))) {
			ext_name = fileName.substring(dot_pos + 1);
			
			if(!ext_name.equals("cl"))
				flag = false;
		}
		else {
			flag = false;
		}
		
		return flag;
    }
    
    // When called, it reads the next line and remove comments.
    public String preprocess(String input) {  
    	StringBuilder str = new StringBuilder();
    	String []lines = input.split("\n");
    	
		for(String line: lines) {
			// remove leading and tailing white spaces 
			line = line.trim();
			
			// remove comment line
			if(line.length() > 2 && line.substring(0, 2).equals("//")) {
				continue;
			}
			
			str.append(line+"\n");
		}
		return str.toString();
    }
    
}
