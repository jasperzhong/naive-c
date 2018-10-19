package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class PreProcessor {
	private FileInputStream fis;
	private InputStreamReader isr;
	private BufferedReader br;
	
	// read a C-like language file(xxx.cl) and return its content as a string.
    public PreProcessor(String fileName, String encoding) {
    	
    	// if not xxx.cl, exit
    	boolean flag = CheckExt(fileName);
    	
		if(flag) {
			System.err.printf("Fatal: Input file %s should be C-like language source code file(xxx.cl)!\n", fileName);  
			return;
		}
    			
    	try {
			this.fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			System.err.printf("Fatal: Input file %s is not found!\n", fileName); 
			e.printStackTrace();
		}  
    	
        try {
			this.isr = new InputStreamReader(fis, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.printf("Fatal: Your OS does not support %s!\n", fileName, encoding); 
			e.printStackTrace();
		}
        
        this.br = new BufferedReader(isr);
    }
    
    // Check whether the input file is like "xxx.cl".
    public boolean CheckExt(String fileName) {
		String ext_name;
		int dot_pos = fileName.lastIndexOf('.');
		boolean flag = false;
		if((dot_pos > -1) && (dot_pos < (fileName.length() - 1))) {
			ext_name = fileName.substring(dot_pos + 1);
			
			if(!ext_name.equals("cl"))
				flag = true;
		}
		else {
			flag = true;
		}
		
		return flag;
    }
    
    // When called, it reads the next line and remove comments.
    public String preprocess() {
    	String str;
    	try {
			while((str = br.readLine()) != null) {
				// remove leading and tailing white spaces 
				str = str.trim();
				
				// remove comment line
				if(str.length() > 2 && str.substring(0, 2).equals("//")) {
					continue;
				}
				
				return str;
			}
		} catch (IOException e) {
			System.err.printf("Fatal: Reading error occur!\n"); 
			e.printStackTrace();
		}
    	return null;
    }
    
    /* TEST
    public static void main(String args[]) {
		PreProcessor p = new PreProcessor("D:\\Coding\\Java\\naive-c\\src\\lexer\\test.cl", "UTF-8");
		String str;
		while((str = p.preprocess()) != null) {
			System.out.println(str);
			continue;
		}
	}
	*/
}
