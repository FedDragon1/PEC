package pec.util;

import java.io.BufferedOutputStream;
import java.io.File;				//文件相关库
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apfloat.Apfloat;
import net.objecthunter.exp4j.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A ParticleGenerator is used to create your own ripple disappear effect.
 * @see {@link oldStuff.Disappear}
 * @author 乐观的辣条
 * @since alpha 1.0.0
 */
public class ParticleGenerator {
	private String PATH = ".";
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private Apfloat end;
	private Apfloat start;
	private Expression[] expressions = new Expression[3];
	
	/**
	 * The {@code String} is used as a model of {@code .json} file
	 * 
	 * @param identifier {@code String} used in Minecraft
	 * @param texture the {@code String} indicates the file name used
	 * @param destination {@code double} value indicates the relative position 
	 * 				({%3$.10f}, {%4$.10f}, {%5$.10f})
	 * @param rate the {@code String} indicates the rate of animation, 
	 * 				default {@code "Math.sqrt(4 - Math.pow(variable.particle_age - 2, 2))"}
	 * @param flipbook Flipbook details
	 * @since alpha 1.0.0
	 * @author 乐观的辣条
	 * 
	 */
	private final static String FILE = "{\r\n"
			+ "  \"format_version\": \"1.10.0\",\r\n"
			+ "  \"particle_effect\":{\r\n"
			+ "    \"description\":{\r\n"
			+ "      \"identifier\": \"minecraft:%1$s\",\r\n"
			+ "      \"basic_render_parameters\": {\r\n"
			+ "        \"material\": \"particles_blend\",\r\n"
			+ "        \"texture\": \"textures/particle/%2$s\"\r\n"
			+ "      }\r\n"
			+ "    },\"components\":{\r\n"
			+ "      \"minecraft:emitter_rate_manual\": {\r\n"
			+ "        \"max_particles\": 50\r\n"
			+ "      },\r\n"
			+ "      \"minecraft:emitter_lifetime_once\": {\r\n"
			+ "        \"active_time\": 10\r\n"
			+ "      },\r\n"
			+ "      \"minecraft:minecraft:emitter_shape_point\": {\r\n"
			+ "      },\r\n"
			+ "      \"minecraft:particle_lifetime_expression\": {\r\n"
			+ "        \"max_lifetime\": 2\r\n"
			+ "      },\r\n"
			+ "      \"minecraft:particle_motion_parametric\": {\r\n"
			+ "        \"relative_position\": [\r\n"
			+ "          \"%3$.10f * (%6$s)\",\r\n"
			+ "          \"%4$.10f * (%6$s)\",\r\n"
			+ "          \"%5$.10f * (%6$s)\"\r\n"
			+ "          ]\r\n"
			+ "      },\r\n"
			+ "      \"minecraft:particle_appearance_billboard\": {\r\n"
			+ "        \"size\": [0.1, 0.1],\r\n"
			+ "        \"facing_camera_mode\": \"lookat_xyz\",\r\n"
			+ "        \"uv\": {\r\n"
			+ "          \"texture_width\": %7$d,\r\n"
			+ "          \"texture_height\": %8$d,\r\n"
			+ "          \"flipbook\": {\r\n"
			+ "            \"base_UV\": %9$s,\r\n"
			+ "            \"size_UV\": %10$s,\r\n"
			+ "            \"step_UV\": %11$s,\r\n"
			+ "            \"frames_per_second\": 8,\r\n"
			+ "            \"max_frame\": %12$d,\r\n"
			+ "            \"stretch_to_lifetime\": true,\r\n"
			+ "            \"loop\": false\r\n"
			+ "          }\r\n"
			+ "        }\r\n"
			+ "      },\r\n"
			+ "      \"minecraft:particle_appearance_tinting\": {\r\n"
			+ "        \"color\": [ \"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - (0.0153 * (1 - Math.pow(0.7, variable.particle_age)) / (1 - 0.7)) : 1.0\", \"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - (0.0387 * (1 - Math.pow(0.7, variable.particle_age)) / (1 - 0.7)) : 1.0\", \"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - (0.0636 * (1 - Math.pow(0.7, variable.particle_age)) / (1 - 0.7)) : 1.0\", \"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - 0.60 * ((variable.particle_age - (variable.particle_lifetime / 2.0)) / (variable.particle_lifetime / 2.0)) : 1.0\" ]\r\n"
			+ "      }\r\n"
			+ "    }\r\n"
			+ "  }\r\n"
			+ "}";
	
	/**
	 * Manipulate the output path
	 * 
	 * @param PATH Destination
	 * @since alpha 1.0.0
	 * @author 乐观的辣条
	 */
	public void setOut(String PATH) {
		this.PATH = PATH;
		LOGGER.log(Level.INFO, "Successfully set output Path to " + PATH);
	}
	
	/**
	 * 
	 * Constructs an {@code ParticleGenerator} object
	 * 
	 * @param functions Parametric function literals with parametric {@code t}, later used to create files.
	 * @since alpha 1.0.0
	 * @author 乐观的辣条
	 * 
	 */
	public ParticleGenerator(String[] functions, Apfloat start, Apfloat end) {
		Expression funcX = new ExpressionBuilder(functions[0]).variable("t").build();
		Expression funcH = new ExpressionBuilder(functions[1]).variable("t").build();
		Expression funcY = new ExpressionBuilder(functions[2]).variable("t").build();
		
		boolean validation = validateFunction(end, funcX, funcH, funcY);
		if(!validation) {
			throw new ArithmeticException("Division by 0 Occured in One or more Function(s)");
		}
		
		this.end = end;
		this.start = start;
		expressions[0] = funcX;
		expressions[1] = funcH;
		expressions[2] = funcY;
	}
	
	/**
	 * Check if valid
	 * 
	 * @param expressions check if they are valid
	 * @param end Constant use in for loop
	 * @return {@code boolean} which indicates if the parametric function is valid
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public static boolean validateFunction(Apfloat end, Expression... expressions) {
		Apfloat step = new Apfloat(0.01, 16);
		for(Apfloat check = new Apfloat(0); check.compareTo(end) < 0; check = check.add(step)) {
			try {
				expressions[0].setVariable("t", check.doubleValue()).evaluate();
				expressions[1].setVariable("t", check.doubleValue()).evaluate();
				expressions[2].setVariable("t", check.doubleValue()).evaluate();
			} catch (ArithmeticException e) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Create custom particle file
	 * 
	 * @param fileCount Number of files going to be generated (40, 80, or 160)
	 * @param name The name of particle
	 * @param texture Array contains details about texture 
	 * @param rate String literal of rate function with {@code variable.particle_age}
	 * @throws IllegalFileCountException
	 * @throws FileNotFoundException
	 * @see {@link #FILE}
	 * @since alpha 1.0.0
	 * @author 乐观的辣条
	 */
	public void create(int fileCount, String name, String[] texture, String rate) throws IllegalFileCountException, FileNotFoundException {
		if (fileCount != 40 && fileCount != 80 && fileCount != 160) throw new IllegalFileCountException("File Count " + fileCount + " is Not a Multiple of 40");
		
		int decimals = fileCount == 40 ? 1 : fileCount == 80 ? 2 : 3;
		Apfloat delta = new Apfloat(fileCount == 40 ? 0.1 : fileCount == 80 ? 0.05 : 0.025);
		Apfloat suffix1 = new Apfloat(0);
		Apfloat suffix2 = new Apfloat(1);
		Apfloat step = new Apfloat(0);
		step = end.subtract(start).divide(new Apfloat(fileCount));
		String suffix4format = "_%." + decimals + "f" + "_%." + decimals + "f";
		String suffix = String.format(suffix4format, suffix1.doubleValue(), suffix2.doubleValue());
		
		for(Apfloat check = start; check.compareTo(end) < 0; check = check.add(step)) {
			
			File file = new File(PATH + "\\" + name + suffix + ".json");
			PrintStream ps;
			
			try {
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true);
			} catch (FileNotFoundException e) {
				new File(PATH).mkdirs();
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true);
			}
			
			double x = expressions[0].setVariable("t", check.doubleValue()).evaluate();
			double h = expressions[1].setVariable("t", check.doubleValue()).evaluate();
			double y = expressions[2].setVariable("t", check.doubleValue()).evaluate();
			String output = String.format(FILE, name + suffix, texture[0], x, h, y, rate, 
					Integer.parseInt(texture[1]), Integer.parseInt(texture[2]), texture[3], texture[4], texture[5],
					Integer.parseInt(texture[6]));
			int time = (int) Math.round(((check.subtract(start)).divide(step)).doubleValue());
			System.out.println(time);
			if ((double) time / (double) fileCount < 0.25) {
				suffix1 = suffix1.add(delta);
				suffix2 = suffix2.subtract(delta);
			} else if (((double) time / (double) fileCount < 0.5)) {
				suffix1 = suffix1.subtract(delta);
				suffix2 = suffix2.subtract(delta);
			} else if (((double) time / (double) fileCount < 0.75)) {
				suffix1 = suffix1.subtract(delta);
				suffix2 = suffix2.add(delta);
			} else {
				suffix1 = suffix1.add(delta);
				suffix2 = suffix2.add(delta);
			}
			System.out.println(suffix);
			suffix = String.format(suffix4format, suffix1.doubleValue(), suffix2.doubleValue());
			
			ps.print(output);
			ps.close();
		}
		
	}
	
	/**
	 * Create custom particle file
	 * 
	 * @param fileCount Number of files going to be generated (40, 80, or 160)
	 * @param name The name of particle
	 * @param texture Array contains details about texture
	 * @throws IllegalFileCountException
	 * @throws FileNotFoundException
	 * @see {@link #FILE}
	 * @since alpha 1.0.0
	 * @author 乐观的辣条
	 */
	public void create(int fileCount, String name, String[] texture) throws IllegalFileCountException, FileNotFoundException {
		if (fileCount != 40 && fileCount != 80 && fileCount != 160) throw new IllegalFileCountException("File Count " + fileCount + " is Not a Multiple of 40");
		
		final String rate = "Math.sqrt(4 - Math.pow(variable.particle_age - 2, 2))";
		int decimals = fileCount == 40 ? 1 : fileCount == 80 ? 2 : 3;
		Apfloat delta = new Apfloat(fileCount == 40 ? 0.1 : fileCount == 80 ? 0.05 : 0.025);
		Apfloat suffix1 = new Apfloat(0);
		Apfloat suffix2 = new Apfloat(1);
		Apfloat step = new Apfloat(0);
		step = end.subtract(start).divide(new Apfloat(fileCount));
		String suffix4format = "_%." + decimals + "f" + "_%." + decimals + "f";
		String suffix = String.format(suffix4format, suffix1.doubleValue(), suffix2.doubleValue());
		
		for(Apfloat check = start; check.compareTo(end) < 0; check = check.add(step)) {
			
			File file = new File(PATH + "\\" + name + suffix + ".json");
			PrintStream ps;
			
			try {
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true);
			} catch (FileNotFoundException e) {
				new File(PATH).mkdirs();
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true);
			}
			
			double x = expressions[0].setVariable("t", check.doubleValue()).evaluate();
			double h = expressions[1].setVariable("t", check.doubleValue()).evaluate();
			double y = expressions[2].setVariable("t", check.doubleValue()).evaluate();
			String output = String.format(FILE, name + suffix, texture[0], x, h, y, rate, 
					Integer.parseInt(texture[1]), Integer.parseInt(texture[2]), texture[3], texture[4], texture[5],
					Integer.parseInt(texture[6]));
			int time = (int) Math.round(((check.subtract(start)).divide(step)).doubleValue());
			System.out.println(time);
			if ((double) time / (double) fileCount < 0.25) {
				suffix1 = suffix1.add(delta);
				suffix2 = suffix2.subtract(delta);
			} else if (((double) time / (double) fileCount < 0.5)) {
				suffix1 = suffix1.subtract(delta);
				suffix2 = suffix2.subtract(delta);
			} else if (((double) time / (double) fileCount < 0.75)) {
				suffix1 = suffix1.subtract(delta);
				suffix2 = suffix2.add(delta);
			} else {
				suffix1 = suffix1.add(delta);
				suffix2 = suffix2.add(delta);
			}
			System.out.println(suffix);
			suffix = String.format(suffix4format, suffix1.doubleValue(), suffix2.doubleValue());
			
			ps.print(output);
			ps.close();
		}
	}
	
	/**
	 * Create custom particle file
	 * 
	 * @param name The name of particle
	 * @param texture Array contains details about texture
	 * @throws IllegalFileCountException
	 * @throws FileNotFoundException
	 * @see {@link #FILE}
	 * @since alpha 1.0.0
	 * @author 乐观的辣条
	 */
	public void create(String name, String[] texture) throws IllegalFileCountException, FileNotFoundException {
		final int fileCount = 80;
		final String rate = "Math.sqrt(4 - Math.pow(variable.particle_age - 2, 2))";
		final int decimals = 2;
		final Apfloat delta = new Apfloat(0.05);
		
		Apfloat suffix1 = new Apfloat(0);
		Apfloat suffix2 = new Apfloat(1);
		Apfloat step = new Apfloat(0);
		step = end.subtract(start).divide(new Apfloat(fileCount));
		String suffix4format = "_%." + decimals + "f" + "_%." + decimals + "f";
		String suffix = String.format(suffix4format, suffix1.doubleValue(), suffix2.doubleValue());
		
		for(Apfloat check = start; check.compareTo(end) < 0; check = check.add(step)) {
			
			File file = new File(PATH + "\\" + name + suffix + ".json");
			PrintStream ps;
			
			try {
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true);
			} catch (FileNotFoundException e) {
				new File(PATH).mkdirs();
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true);
			}
			
			double x = expressions[0].setVariable("t", check.doubleValue()).evaluate();
			double h = expressions[1].setVariable("t", check.doubleValue()).evaluate();
			double y = expressions[2].setVariable("t", check.doubleValue()).evaluate();
			String output = String.format(FILE, name + suffix, texture[0], x, h, y, rate, 
					Integer.parseInt(texture[1]), Integer.parseInt(texture[2]), texture[3], texture[4], texture[5],
					Integer.parseInt(texture[6]));
			int time = (int) Math.round(((check.subtract(start)).divide(step)).doubleValue());
			System.out.println(time);
			if ((double) time / (double) fileCount < 0.25) {
				suffix1 = suffix1.add(delta);
				suffix2 = suffix2.subtract(delta);
			} else if (((double) time / (double) fileCount < 0.5)) {
				suffix1 = suffix1.subtract(delta);
				suffix2 = suffix2.subtract(delta);
			} else if (((double) time / (double) fileCount < 0.75)) {
				suffix1 = suffix1.subtract(delta);
				suffix2 = suffix2.add(delta);
			} else {
				suffix1 = suffix1.add(delta);
				suffix2 = suffix2.add(delta);
			}
			System.out.println(suffix);
			suffix = String.format(suffix4format, suffix1.doubleValue(), suffix2.doubleValue());
			
			ps.print(output);
			ps.close();
		}
	}
	
	
	public static void main(String[] args) throws IOException, IllegalFileCountException {
//		String[] str = {"sin(t)cos(t)", "-cos(t)", "0"};
//		String[] texture = {"particles2.png", "960", "656", "[944, 0]", "[16, 16]", "[-16, 0]", "60"};
//		Apfloat start = new Apfloat(-2);
//		Apfloat end = new Apfloat(2);
//		
//		ParticleGenerator obj = new ParticleGenerator(str, start, end);
//		obj.setOut("Pec OutPu");
//		obj.create("endRod_Test", texture);
		
	}
}

class IllegalFileCountException extends Exception{
	private static final long serialVersionUID = -6147957199653228238L;
	
	/**
	 * Constructs an {@code IllegalFileCountException} with the specified detail message.
	 * 
	 * @param message
	 * 		The detail message (which is saved for later retrieval
	 * 		by the {@link #getMessage()} method)
	 * 
	 */
	public IllegalFileCountException(String message) {
		super(message);
	}
}