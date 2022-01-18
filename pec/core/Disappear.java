package pec.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apfloat.Apfloat;
import net.objecthunter.exp4j.*;

/**
 * Generate Disappear Effects
 * 
 * @author 乐观的辣条
 * @since alpha 1.0.0
 */
public class Disappear extends Pen {
	private HashMap<String, double[][]> edgesMap = new HashMap<>();
	private HashMap<String, String[][]> paramMap = new HashMap<>();
	private HashMap<String, String> rippleMap = new HashMap<>(); 
	
	/**
	 * Initialize built-in figures and output path 
	 * 
	 * @param PATH File Output Path
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public Disappear(String PATH) {
		String[][] octahedron = {{"(sin(t))^3", "0", "(cos(t)^3)"}, {"(sin(t))^3", "(cos(t)^3)", "0"}, {"0", "(sin(t))^3", "(cos(t)^3)"}};
		paramMap.put("octahedron", octahedron);
		String[][] sphere = {{"sqrt(1-((2t-1)/120-1)^2)cos(2tπφ)","sqrt(1-((2t-1)/120-1)^2)sin(2tπφ)","(2t-1)/120-1"}, {"120"}};
		paramMap.put("sphere", sphere);
		double[][] cube = {{0,0,0,0,0,1}, {0,0,0,0,1,0}, {0,0,0,1,0,0}, {0,1,0,1,1,0}, {0,1,0,0,1,1}, {1,0,0,1,1,0}, {1,0,0,1,0,1}, {1,0,1,1,1,1}, {0,0,1,0,1,1}, {0,1,1,1,1,1}, {0,0,1,1,0,1}, {1,1,0,1,1,1}, {1,1,1}};
		edgesMap.put("cube", cube);
		double[][] tetrahedron = {{0.75, 0.75, (Math.sqrt(3) / 3 + 0.5) * 1.5, 0, 0.75, (-Math.sqrt(3) / 6 + 0.5) * 1.5}, 
				{0, 0.75, (-Math.sqrt(3) / 6 + 0.5) * 1.5, 1.5, 0.75, (-Math.sqrt(3) / 6 + 0.5) * 1.5}, 
				{1.5, 0.75, (-Math.sqrt(3) / 6 + 0.5) * 1.5, 0.75, 0.75, (Math.sqrt(3) / 3 + 0.5) * 1.5},
				{0.75, 0.75, (Math.sqrt(3) / 3 + 0.5) * 1.5, 0.75, (Math.sqrt(6) / 3 + 0.5) * 1.5, 0.75},
				{0.75, (Math.sqrt(6) / 3 + 0.5) * 1.5, 0.75, 1.5, 0.75, (-Math.sqrt(3) / 6 + 0.5) * 1.5},
				{0.75, (Math.sqrt(6) / 3 + 0.5) * 1.5, 0.75, 0, 0.75, (-Math.sqrt(3) / 6 + 0.5) * 1.5}, 
				{0.75, (Math.sqrt(6) / 3 + 0.5) * 1.5, 0.75}};
		edgesMap.put("tetrahedron", tetrahedron);
		rippleMap.put("circle", "%s_circle_");
		rippleMap.put("sineSpiral", "%s_sineSpiral_");
		rippleMap.put("star", "%s_star_");
		rippleMap.put("flower", "%s_flower_");
		super.PATH = PATH;
	}
	
	@Override
	public void save(String key, double[][] edges) throws InvalidInputException {
		boolean valid = false;
		int edgeCount = 1;
		
		for(double[] edge : edges) {
			if (edgeCount == edges.length && edgeCount != 1) {
				if (edge.length == 3) valid = true;
			} else {
				edgeCount++;
			}
			if (edge.length != 6) break;
		}
		
		if (!valid) throw new InvalidInputException("Exception occurred during customize figure validation");
		
		edgesMap.put(key, edges);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void save(String key, String[][] paramFuncs) {
		List<ArrayList<Expression>> allParams = new ArrayList<>(paramFuncs.length);
		ArrayList<Expression> singleParam = new ArrayList<>(3);
		
		for(String[] figure: paramFuncs) {
			for(String candidate: figure) {
				Expression func = new ExpressionBuilder(candidate).variable("t").build();
				singleParam.add(func);
			}
			allParams.add((ArrayList<Expression>)singleParam.clone());
			singleParam.clear();
		}
		
		check(paramFuncs);
		paramMap.put(key, paramFuncs);
	}
	
	@Override
	public void save(String key, String particle) throws InvalidInputException {
		if (particle.matches("^%s_.+_$")) rippleMap.put(key, particle);
		else throw new InvalidInputException("Incorrect Format");
	}
	
	/**
	 * Checks if the parametric functions contain an {@link ArithmeticException}
	 * 
	 * @param paramFuncs Functions checking
	 * @return always true if no Exception is thrown
	 */
	private static boolean check(String[][] paramFuncs) {
		final Apfloat step = new Apfloat(0.05);
		final Apfloat end = new Apfloat(paramFuncs[paramFuncs.length - 1].length == 1 ? Double.parseDouble(paramFuncs[paramFuncs.length - 1][0]) : 2*Math.PI);
		
		for (Apfloat start = new Apfloat(0); start.compareTo(end) < 0; start = start.add(step)) {
			for(String[] curve : paramFuncs) {
				if(curve.length == 1) break;
				for (String expr : curve) {
					@SuppressWarnings("unused")
					double expression = new ExpressionBuilder(expr).variable("t").build().setVariable("t", start.doubleValue()).evaluate();
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void printData() {
		System.out.println("Defined by Parametic Functions: ");
		for(String key : paramMap.keySet()) {
			System.out.println("  " + key + ": " + Arrays.deepToString(paramMap.get(key)));
		}
		System.out.println("\nDefined by Edges: ");
		for(String key : edgesMap.keySet()) {
			System.out.println("  " + key + ": " + Arrays.deepToString(edgesMap.get(key)));
		}
		System.out.println("\nDefined as Ripple Effects: ");
		for(String key : rippleMap.keySet()) {
			System.out.println("  " + key + ": " + rippleMap.get(key));
		}
		System.out.println();
	}
	

	@Override
	public void draw(int x, int y, int z, String key, int perSideParticle, String particle) throws InvalidInputException, IOException {
		if (perSideParticle <= 0) throw new InvalidInputException("Negative or 0 particles per side / figure");
		if(paramMap.containsKey(key)) {
			drawParam(x, y, z, key, perSideParticle, particle);
		} else if (edgesMap.containsKey(key)) {
			drawEdges(x, y, z, key, perSideParticle, particle);
		} else if (rippleMap.containsKey(key)) {
			drawRipple(x, y, z, key, perSideParticle, particle);
		} else {
			throw new InvalidInputException("Unknown Figure");
		}
	}
	
	/**
	 * Draw a figure with given coordinates and defined key
	 * 
	 * @param x Minecraft Coordinate x
	 * @param y Minecraft Coordinate y
	 * @param z Minecraft Coordinate z
	 * @param key The Effect
	 * @param perSideParticle particles per side
	 * @param particle texture uses
	 * @throws IOException
	 * 
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	private void drawEdges(int x, int y, int z, String key, int perSideParticle, String particle) throws IOException {
		
		double[][] data = edgesMap.get(key);
		
		int suffix = 1;
		File function = new File(PATH + "\\" + x + ".mcfunction");
		while (function.exists()) {	
			function = new File(PATH + "\\" + x + '_' + suffix + ".mcfunction");	
			suffix++;			
		}
		
		try {
			ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(function)), true);
		} catch (FileNotFoundException e) {
			new File(PATH).mkdirs();
			ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(function)), true);
		}
		
		for (int i = 0; i < data.length - 1; i++) {
			double stepX = (double)(data[i][3] - data[i][0]) / perSideParticle;
			double stepY = (double)(data[i][4] - data[i][1]) / perSideParticle;
			double stepZ = (double)(data[i][5] - data[i][2]) / perSideParticle;
			for(int j = 0; j < perSideParticle; j++) {
				double deltaX = stepX * j + data[i][0] + x;
				double deltaY = stepY * j + data[i][1] + y;
				double deltaZ = stepZ * j + data[i][2] + z;
				String func = String.format("particle %s %.10f %.10f %.10f", particle, deltaX, deltaY, deltaZ);
				ps.println(func);
			}
		}
		
		double[] extra = data[data.length - 1];
		String func = String.format("particle %s %.10f %.10f %.10f", particle, extra[0] + x, extra[1] + y, extra[2] + z);
		ps.println(String.format("setblock %d %d %d air", x, y, z));
		ps.println(func);
		ps.flush();
		
	}
	
	/**
	 * Draw a figure with defined Parametric Functions and Coordinate
	 * 
	 * @param x Minecraft Coordinate x
	 * @param y Minecraft Coordinate y
	 * @param z Minecraft Coordinate z
	 * @param key The Effect
	 * @param perFigureParticle particles per parametric function
	 * @param particle texture uses
	 * @throws IOException
	 * 
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	private void drawParam(int x, int y, int z, String key, int perFigureParticle, String particle) throws IOException {
		
		String[][] data = paramMap.get(key);
		int[] coordinate = {x, y, z};
		int figures = data[data.length - 1].length == 1 ? data.length - 1 : data.length;
		final Apfloat end = new Apfloat(data[data.length - 1].length == 1 ? Double.parseDouble(data[data.length - 1][0]) : 2*Math.PI);
		Apfloat step = new Apfloat(0);
		step = end.divide(new Apfloat(perFigureParticle));
		
		int suffix = 1;
		File function = new File(PATH + "\\" + x + ".mcfunction");
		while (function.exists()) {	
			function = new File(PATH + "\\" + x + '_' + suffix + ".mcfunction");	
			suffix++;			
		}
		
		try {
			ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(function)), true);
		} catch (FileNotFoundException e) {
			new File(PATH).mkdirs();
			ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(function)), true);
		}

		for(int i = 0; i < figures; i++) {
			for (Apfloat start = new Apfloat(0); start.compareTo(end) < 0; start = start.add(step)) {
				double[] outCoor = new double[3];
				for (int j = 0; j < 3; j++) {
					double value = new ExpressionBuilder(data[i][j]).variable("t").build().setVariable("t", start.doubleValue()).evaluate();
					outCoor[j] = value + coordinate[j];
				}
				String func = String.format("particle %s %.10f %.10f %.10f", particle, outCoor[0], outCoor[1], outCoor[2]);
				ps.println(func);
			}
		}
		ps.println(String.format("setblock %d %d %d air", x, y, z));
		ps.flush();
	}
	
	/**
	 * Draw Ripple Effects
	 * 
	 * @param x Minecraft Coordinate x
	 * @param y Minecraft Coordinate y
	 * @param z Minecraft Coordinate z
	 * @param key The Effect
	 * @param decimals Count of decimals for particle file
	 * @param particle texture uses
	 * @throws IOException
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	private void drawRipple(int x, int y, int z, String key, int decimals, String particle) throws IOException {
		Apfloat suff1 = new Apfloat(0);
		Apfloat suff2 = new Apfloat(-1);
		Apfloat delta = new Apfloat(decimals == 1 ? 0.1 : decimals == 2 ? 0.05 : 0.025);
		String model = rippleMap.get(key);
		//System.out.println(1);
		
		int suffix = 1;
		File function = new File(PATH + "\\" + x + ".mcfunction");
		while (function.exists()) {	
			function = new File(PATH + "\\" + x + '_' + suffix + ".mcfunction");	
			suffix++;	
		}
		ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(function)));
		
		for (int now = 0, fileCount = decimals == 1 ? 40 : decimals == 2 ? 80 : 160, quarter = fileCount / 4; now < fileCount; now++) {
			String func = String.format(model, particle);
			func = model + "%." + decimals + "f_%." + decimals + "f %d %.1f %d";
			func = "particle " + String.format(func,
					particle, suff1.doubleValue(), suff2.doubleValue(), x, y + 0.5, z);
			ps.println(func);
			
			if (now < quarter) {
				suff1 = suff1.add(delta);
				suff2 = suff2.add(delta);
			} else if (now < 2*quarter) {
				suff1 = suff1.subtract(delta);
				suff2 = suff2.add(delta);
			} else if (now < 3*quarter) {
				suff1 = suff1.subtract(delta);
				suff2 = suff2.subtract(delta);
			} else {
				suff1 = suff1.add(delta);
				suff2 = suff2.subtract(delta);
			}
		}
		ps.println(String.format("setblock %d %d %d air", x, y, z));
		ps.flush();
		
	}
	
}
