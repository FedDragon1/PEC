package pec.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Combine all functions in a time to one
 * 
 * @author 乐观的辣条
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
	 * @since alpha 1.0.0
	 */
	public static void combine(int end, String path) throws IOException {								
		for (int xValue = 0; xValue <= end; xValue++) {		
			String name = Integer.toString(xValue);						
			File OutputFunc = new File(path + "\\Func" + name + ".mcfunction");
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(OutputFunc)), true);			
			String command = "function " + name;					
			output.println(command);										
			System.out.println(command);	
			File function = new File(path + "\\" + name + '_' + 1 + ".mcfunction");	
			int func_suffix = 1;
			while (function.exists()) {
				command = "function " + name + '_' + func_suffix;	
				output.println(command);
				func_suffix++;
				function = new File(path + "\\" + name + '_' + func_suffix + ".mcfunction");	
			}
			if (xValue > 24) {
				output.println("tp @p " + (xValue - 24) + " 211 0");
			}
			output.close();
		}
	}
}
