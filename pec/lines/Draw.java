package pec.lines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class Draw {
	public static void normal(String service, double x1, double x2, double y1, double y2, double height, String particle_type, int useless) throws Exception {
		if (service != "line" && service != "parabola") { throw new Exception("Not Inplemented or Incorrect Input");}
		File directory = new File(".\\Pec Output");
		directory.mkdir();
		double distance = x2 - x1; 						
		double slope = (y2 - y1) / (x2 - x1); 			
		double k = 1;							
		double startX = x1;							
		double startY = y1;							
		double relativeX = 0;
		double a = -(2 / distance);			
		for (double t = 0; t < distance; t++) {					
			String name = Integer.toString((int)(x1 + t));		
			File function = new File("Pec Output\\" + name + ".mcfunction");		
			while (function.exists()) {										
				File replacement = new File("Pec Output\\" + name + '_' + (int)(k) + ".mcfunction");	
				function.renameTo(replacement);										
				k += 1 / distance;									
			}
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(function)), true);	
			for (double i = 0; i < 1; i += 0.05) {				
				double y = slope * i + startY + 0.5;				
				double h = height + 0.5;
				if(service == "parabola") {
					h = a * Math.pow(i + relativeX, 2) + 2 * (i + relativeX) + height + 0.5;	
				}
				double x = i + startX + 0.5;							
				String func_command = String.format("particle minecraft:%s %.10f %.10f %.10f", particle_type, x, h, y);
				output.println(func_command);							
				System.out.println(func_command);							
			}
			startY += slope;							
			startX++;	
			relativeX++;
			output.close();
		}
	}
	
	public static void movable(String service, double x1, double x2, double y1, double y2, double height, String particle_type, int count) throws Exception {
		if (service != "spiralLine" && service != "spiralParabola") { throw new Exception("Not Inplemented or Incorrect Input");}
		File directory = new File(".\\Pec Output");
		directory.mkdir();
		BigDecimal RotateConstant = new BigDecimal(0.05);
		BigDecimal yDestination = new BigDecimal(0);		
		BigDecimal hDestination = new BigDecimal(-1);		
		double distance = x2 - x1;							
		double relativeX = 0;
		double a = -(2 / distance);				
		double slope = (y2 - y1) / (x2 - x1);
		double startX = x1;
		double startY = y1;
		double k = 1;			
		int flag = 1;			
		for(double t = 0; t < distance; t++) {						
			String name = Integer.toString((int) (x1 + t));		
			File function = new File("Pec Output\\" + name + ".mcfunction");
			while (function.exists()) {									
				File replacement = new File("Pec Output\\" + name + '_' + (int)(k) + ".mcfunction");
				function.renameTo(replacement);
				k += 1 / distance;
			}
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(function)), true);		
			for(double i = 0; i < 1; i += 0.025) {							
				String func_command;										
				double y = slope * i + startY + 0.5;						
				double x = startX + i + 0.5;								
				double h = height + 0.5;
				if (service == "spiralParabola") {
					h = a * Math.pow(i + relativeX, 2) + 2 * (i + relativeX) + height;	
				}
				if (flag == 1) {													
					func_command = String.format("particle minecraft:%s_%.2f_%.2f %.10f %.10f %.10f", particle_type, yDestination, hDestination, x, h, y);
				} else {
					func_command = String.format("particle minecraft:%s_%.2f_%.2f %.10f %.10f %.10f", particle_type, yDestination.negate(), hDestination.negate(), x, h, y);
				}
				output.println(func_command); 		
				System.out.println(func_command);		
												
				if ((startX - x1) % 2 == 0 && i < 0.5) {		
					yDestination = yDestination.add(RotateConstant);	
					hDestination = hDestination.add(RotateConstant);
				} else if ((startX - x1) % 2 == 0 && i >= 0.5) {	
					yDestination = yDestination.subtract(RotateConstant);
					hDestination = hDestination.add(RotateConstant);	
				} else if ((startX - x1) % 2 == 1 && i < 0.5) {		
					yDestination = yDestination.subtract(RotateConstant);	
					hDestination = hDestination.subtract(RotateConstant);	
				} else {											
					yDestination = yDestination.add(RotateConstant);	
					hDestination = hDestination.subtract(RotateConstant);	
				}
				if (count == 2) {		
					flag = 0 - flag;		
				}
			}
			startX++;				
			startY += slope;	
			relativeX++;
			output.close();
		}
	}
	public static void main(String[] args) throws Exception {
		Draw.normal("parabola", 10, 30, 5, 20, 30, "endRod", 0);
	}
}
