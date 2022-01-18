package pec.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Generate string effects
 * 
 * @author 乐观的辣条
 * @since alpha 1.0.0
 */
public class Line extends Pen {
	private HashMap<String, String[]> lines = new HashMap<>(); 
	
	/**
	 * An object which draws string
	 * @param PATH File output path
	 */
	public Line(String PATH) {
		String[] line = {"false", "t", "(y2-y1)/(x2-x1)t", "(z2-z1)/(x2-x1)t"};
		lines.put("line", line);
		String[] parabola = {"false", "t+((y2-y1)/(x2-x1))((2t^2)/(x2-x1)-2t)", "(t*(y2-y1)-2t^2)/(x2-x1)+2t", "(z2-z1)/(x2-x1)t"};
		lines.put("parabola", parabola);
		String[] sl = {"true", "t", "(y2-y1)/(x2-x1)t", "(z2-z1)/(x2-x1)t", "vertical", "2"};
		lines.put("spiralLine", sl);
		String[] sp = {"true", "t+((y2-y1)/(x2-x1))((2t^2)/(x2-x1)-2t)", "(t*(y2-y1)-2t^2)/(x2-x1)+2t", "(z2-z1)/(x2-x1)t", "horizontal", "2"};
		lines.put("spiralParabola", sp);
		
		super.PATH = PATH;
	}
	
	@Override
	public void printData() {
		String[] movable = {};
		String[] stable = {};
		
		for (String key: lines.keySet()) {
			if (Boolean.parseBoolean(lines.get(key)[0])) {
				movable = Arrays.copyOf(movable, movable.length + 1);
				movable[movable.length - 1] = key;
			} else {
				stable = Arrays.copyOf(stable, stable.length + 1);
				stable[stable.length - 1] = key;
			}
		}
		
		System.out.println("Lines with dynamic particles:\n");
		for (String key : movable) {
			System.out.println("  " + key + ": particle set = " + lines.get(key)[lines.get(key).length-2] + 
					"\n\tParametric Functions:\n\tx = " + lines.get(key)[1] + "\n\ty = " + lines.get(key)[2] + "\n\tz = " + lines.get(key)[3] + '\n');
		}
		System.out.println("Lines with static particles:\n");
		for (String key : stable) {
			System.out.println("  " + key + ":\n\tParametric Functions:\n\tx = " + lines.get(key)[1] + "\n\ty = " + lines.get(key)[2] + "\n\tz = " + lines.get(key)[3] + '\n');
		}
	}
	
	@Override
	public void draw(String key, int x1, int y1, int z1, int x2, int y2, int z2, String particle) throws InvalidInputException, IOException {
		String[] data = lines.get(key);
		if (x1 >= x2) throw new InvalidInputException("Cannot connect backward, (" + x1 + ", " + y1 + ", " + z1 + ") to (" + x2 + ", " + y2 + ", " + z2 + ")");
		if (data == null) throw new InvalidInputException("Undefined string type: " + key);
		int k = 1;
		int delta = DEFAULT_STATIC_PARTICLE_COUNT;
		boolean flag = true;
		BigDecimal s1 = new BigDecimal(-1);
		BigDecimal s2 = new BigDecimal(0);
		BigDecimal rc = new BigDecimal(data[data.length - 1].equals("1") ? 
				0.1 : data[data.length - 1].equals("2") ? 0.05 : 0.025);
		
		if (Boolean.parseBoolean(data[0])) {	//dynamic
			delta = DEFAULT_DYNAMIC_PARTICLE_COUNT;
		}
		
		for (int i = 0; i < (x2 - x1); i++) {
			String name = Integer.toString(x1 + i);	
			File function = new File(PATH + "\\" + name + ".mcfunction");		
			while (function.exists()) {										
				function = new File(PATH + "\\"  + name + '_' + (int)(k) + ".mcfunction");									
				k += 1;									
			}
			k = 1;
			
			try {
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(function)), true);
			} catch (FileNotFoundException e) {
				new File(PATH).mkdirs();
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(function)), true);
			}
			
			for (double t = 0; t < 1; t += 1/(double)delta) {
				HashMap<String, Double> hm = new HashMap<>();
				hm.put("t", i + t); hm.put("x1", (double)x1); hm.put("x2", (double)x2); hm.put("y1", (double)y1);
				hm.put("y2", (double)y2); hm.put("z1", (double)z1); hm.put("z2", (double)z2); 
				double x = new ExpressionBuilder(data[1]).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate() + x1 + 0.5;
				double y = new ExpressionBuilder(data[2]).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate() + y1 + 0.5;
				double z = new ExpressionBuilder(data[3]).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate() + z1 + 0.5;
				String funcParticle = particle;
				if (Boolean.parseBoolean(data[0])) {	//dynamic
					funcParticle = particle + "_%s_%." + data[data.length - 1] + "f_%." + data[data.length - 1] + "f";
					if(FLIP && flag) funcParticle = String.format(funcParticle, data[data.length - 2], round0(s1.negate()), round0(s2.negate()));
					else funcParticle = String.format(funcParticle, data[data.length - 2], s1, s2);
					flag = !flag;
				}
				String func = String.format("particle %s %.10f %.10f %.10f", funcParticle, x, y, z);
				ps.println(func);
				//System.out.println(func);
				//System.out.println(t);
				
				if (i % 2 == 0 && t < 0.5) {		
					s1 = s1.add(rc);	
					s2 = s2.add(rc);
				} else if (i % 2 == 0 && t >= 0.5) {	
					s1 = s1.add(rc);
					s2 = s2.subtract(rc);	
				} else if (i % 2 == 1 && t < 0.5) {		
					s1 = s1.subtract(rc);	
					s2 = s2.subtract(rc);	
				} else {											
					s1 = s1.subtract(rc);	
					s2 = s2.add(rc);	
				}
			}
			ps.close();
		}
	}
	
	@Override
	public void save(String key, boolean dynamic, String paramx, String paramy, String paramz, String particle, int decimal) throws InvalidInputException {
		if(lines.get(key) != null) LOGGER.log(Level.INFO, "Overriding an Existing Figure: " + key + ", on line " + new Throwable().getStackTrace()[1].getLineNumber());
		HashMap<String, Double> hm = new HashMap<>();
		for (double i = 0; i < 5; i += 0.05) {
			hm.put("t", i); hm.put("x1", 0d); hm.put("x2", 10d); hm.put("y1", 0d);
			hm.put("y2", 0d); hm.put("z1", 0d); hm.put("z2", 0d); 
			try {
				@SuppressWarnings("unused")
				double x = new ExpressionBuilder(paramx).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate();
				@SuppressWarnings("unused")
				double y = new ExpressionBuilder(paramy).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate();
				@SuppressWarnings("unused")
				double z = new ExpressionBuilder(paramz).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate();
			} catch (Exception e) {
				e.printStackTrace();
				throw new InvalidInputException("Error occurred when validating parametric functions");
			}
		}
		if (dynamic) {
			String[] arr = {"true", paramx, paramy, paramz, particle, Integer.toString(decimal)};
			lines.put(key, arr);
		} else {
			String[] arr = {"false", paramx, paramy, paramz};
			lines.put(key, arr);
		}
	}
	
	@Override
	public void save(String key, String paramx, String paramy, String paramz) throws InvalidInputException {
		if(lines.get(key) != null) LOGGER.log(Level.INFO, "Overriding an Existing Figure: " + key + ", on line " + new Throwable().getStackTrace()[1].getLineNumber());
		HashMap<String, Double> hm = new HashMap<>();
		for (double i = 0; i < 5; i += 0.05) {
			hm.put("t", i); hm.put("x1", 0d); hm.put("x2", 0d); hm.put("y1", 0d);
			hm.put("y2", 0d); hm.put("z1", 0d); hm.put("z2", 0d); 
			try {
				@SuppressWarnings("unused")
				double x = new ExpressionBuilder(paramx).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate();
				@SuppressWarnings("unused")
				double y = new ExpressionBuilder(paramy).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate();
				@SuppressWarnings("unused")
				double z = new ExpressionBuilder(paramz).variables("t", "x1", "x2", "y1", "y2", "z1", "z2").build().
						setVariables(hm).evaluate();
			} catch (Exception e) {
				e.printStackTrace();
				throw new InvalidInputException("Error occurred when validating parametric functions");
			}
		}
		String[] arr = {"false", paramx, paramy, paramz};
		lines.put(key, arr);
	}
	
	/**
	 * Avoid -0.00 output
	 * 
	 * @param b A BigDecimal object
	 * @return b if b != 0, 0.00 if b == -0.00
	 */
	private static BigDecimal round0(BigDecimal b) {
		if (Math.round(b.doubleValue()*100) == 0) return new BigDecimal(0.00);
		else return b;
	}
}

