package pec.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import bsh.EvalError;
import bsh.Interpreter;
import pec.core.*;

/**
 * Evaluates codes from Excel sheets
 * 
 * @author 乐观的辣条
 * @since alpha 1.0.0
 */
public class MidiIn {
	
	private String[] addons = {};
	private String path;
	
	/**
	 * Setup the output
	 * 
	 * @param outPath File Output Path
	 * @param customizations Store figure with {@code PEN.save();}, more backslashes required. i.e. \\\" for \"
	 * @since alpha 1.1.0
	 */
	public MidiIn(String outPath, String... customizations) {
		path = outPath;
		int count = customizations.length;
		addons = Arrays.copyOf(addons, addons.length + customizations.length);
		for (String customization: customizations) {
			addons[customizations.length - count] = customization;
			count--;
		}
	}
	
	/**
	 * Read data from sheets and create {@code .mcfunction} files
	 * 
	 * @param ExcelPath File path of .xlsx file 
	 * @param OutPath Path of the generated files
	 * @throws IOException
	 * @throws EvalError
	 * @throws InvalidInputException
	 * @since alpha 1.0.0
	 */
	public void cast(String ExcelPath) throws IOException, EvalError, InvalidInputException {
		//Read data
		String out = "import pec.core.*; \n";
		BufferedInputStream file = new BufferedInputStream(new FileInputStream(new File(ExcelPath)));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		String pen = workbook.getSheetAt(0).getRow(0).getCell(0).toString();
		String service = workbook.getSheetAt(0).getRow(0).getCell(1).toString().equals(".draw(\"") ? "Line" : "Disappear";
		out += "Pen " + pen + " = new " + service + "(\"" + path + "\"); \n";
		
		for (String addon : addons) {
			out += addon + "\n";
		}
		for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {
			Sheet sheet = workbook.getSheetAt(sheetCount);
			for(Row row : sheet) {
				String rowData = "";
				int cellCount = 0;
				for(Cell cell : row) {
					cellCount++;
					if (cellCount == 5) {
						rowData += (int)(Double.parseDouble(cell.toString()));
						continue;
					}
					rowData += cell.toString();		
				}
				out += rowData + '\n';
			}

		}
		workbook.close();
		
		//Parse code
		FileWriter fos = new FileWriter(new File("out.log"));
		fos.write(out);
		Interpreter interpreter = new Interpreter();
		interpreter.eval(out);
	}
	
	/**
	 * Rewrite the customizations from constructor
	 * 
	 * @param customizations replacement
	 * @since alpha 1.1.0
	 */
	public void rewriteAddons(String... customizations) {
		addons = new String[0];
		int count = customizations.length;
		addons = Arrays.copyOf(addons, addons.length + customizations.length);
		for (String customization: customizations) {
			addons[customizations.length - count] = customization;
			count--;
		}
	}
	
	/**
	 * Extends the customizations from constructor
	 * 
	 * @param customizations extension
	 * @since alpha 1.1.0
	 */
	public void addAddons(String... customizations) {
		int oldLength = addons.length;
		int count = customizations.length;
		addons = Arrays.copyOf(addons, addons.length + customizations.length);
		for (String customization: customizations) {
			addons[oldLength + customizations.length - count] = customization;
			count--;
		}
	}
}
