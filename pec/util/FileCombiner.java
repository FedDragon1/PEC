package pec.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Combine all functions in a time to one
 * 
 * @author FedeleWu
 * @since alpha 1.0.0
 */
public class FileCombiner {		
	/**
	 * Combine raw function files to one
	 * 
	 * @param end The x coordinate of the end of effects
	 * @param time The max functions in a single time
	 * @param path Path of functions and output
	 * @throws IOException
	 */
	public static void combine(int end, int time, String path) throws IOException {								
		for (int xValue = 0; xValue <= end; xValue++) {		
			String name = Integer.toString(xValue);						
			File OutputFunc = new File(path + "\\Func" + name + ".mcfunction");
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(OutputFunc)), true);			
			String command = "function " + name;					
			output.println(command);										
			System.out.println(command);							
			for (int func_suffix = 1; func_suffix <= time; func_suffix++) {			
				File function = new File(path + "\\" + name + '_' + func_suffix + ".mcfunction");	
				if (function.exists()) {								
					command = "function " + name + '_' + func_suffix;	
					output.println(command);							
					//System.out.println(command);					
				}
			}
			output.close();
		}
	}
}
