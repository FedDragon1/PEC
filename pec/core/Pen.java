package pec.core;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Draw Effects
 * 
 * @author 乐观的辣条
 * @since alpha 1.0.0
 */
public abstract class Pen {
	protected final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public String PATH;
	public PrintStream ps;
	public int DEFAULT_STATIC_PARTICLE_COUNT = 20;
	public int DEFAULT_DYNAMIC_PARTICLE_COUNT = 40;
	public boolean FLIP = true;
	
	/**
	 * Print the data saved
	 * 
	 *  @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public abstract void printData();
	
	/**
	 * Draw float disappear effects
	 * 
	 * @param x Minecraft Coordinate x
	 * @param y Minecraft Coordinate y
	 * @param z Minecraft Coordinate z
	 * @param key Name of Effect
	 * @param perSideParticle particle per side / parametric function
	 * @param particle particle want to use ({@code minecraft:} is not included)
	 * @throws InvalidInputException when perSideParticle is 0 or negative
	 * @throws IOException 
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public void draw(int x, int y, int z, String key, int perSideParticle, String particle) throws InvalidInputException, IOException {}
	
	/**
	 * Saves parametric functions
	 * 
	 * @param key Name of figure
	 * @param paramFuncs Parametric Functions, last array in 2d array indicates the end of t value (2PI if not given)
	 * 
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public void save(String key, String[][] paramFuncs) {}
	
	/**
	 * Saves edges for disappear effects
	 * 
	 * @param key Name of figure
	 * @param edges {{x1, y1, z1, x2, y2, z2}, ..., {xn, yn, zn}} in minecraft coordinate (realative)
	 * 		last array indicates the extra particle needed for a complete figure.
	 * @throws InvalidInputException when length of {@code edges} is lower than 2 or last array's length is not 3
	 * 
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public void save(String key, double[][] edges) throws InvalidInputException {}
	
	/**
	 * Saves particle for ripple disappear effects
	 * 
	 * @param key Name of figure
	 * @param particle Has to start with {@code %s_} and end with {@code _}, in between can be anything except an empty {@code String}
	 * @throws InvalidInputException
	 * 
	 * @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public void save(String key, String particle) throws InvalidInputException {}
	
	/**
	 * Saves string pattern with defined particle set and particle set's decimal count
	 * 
	 * @param dynamic True for dynamic particles, false for static
	 * @param paramx Parametric Function (x axis)
	 * @param paramy Parametric Function (y axis)
	 * @param paramz Parametric Function (z axis)
	 * @param particle Particle set
	 * @param decimal Decimals after file of particle set. e.g., 0.00 = 2, 0.000 = 3
	 * @throws InvalidInputException 
	 * 
	 *  @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public void save(String key, boolean dynamic, String paramx, String paramy, String paramz, String particle, int decimal) throws InvalidInputException {}
	
	/**
	 * Saves string pattern which uses static particle
	 * 
	 * @param paramx Parametric Function (x axis)
	 * @param paramy Parametric Function (y axis)
	 * @param paramz Parametric Function (z axis)
	 * @throws InvalidInputException 
	 * 
	 *  @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public void save(String key, String paramx, String paramy, String paramz) throws InvalidInputException {}
	
	/**
	 * Draw the string with given 2 coordinates and particle use
	 * 
	 * @param key String type
	 * @param x1 x of first coordinate
	 * @param y1 y of first coordinate
	 * @param z1 z of first coordinate
	 * @param x2 x of second coordinate
	 * @param y2 y of second coordinate
	 * @param z2 z of second coordinate
	 * @param particle particle going to use
	 * @throws InvalidInputException
	 * @throws IOException 
	 * 
	 *  @author 乐观的辣条
	 * @since alpha 1.0.0
	 */
	public void draw(String key, int x1, int y1, int z1, int x2, int y2, int z2, String particle) throws InvalidInputException, IOException {}
}
