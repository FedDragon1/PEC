package pec.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
	
	/**
	 * Read data from sheets and create {@code .mcfunction} files
	 * 
	 * @param ExcelPath File path of .xlsx file 
	 * @param OutPath Path of the generated files
	 * @throws IOException
	 * @throws EvalError
	 * @throws InvalidInputException
	 */
	public static void cast(String ExcelPath, String OutPath) throws IOException, EvalError, InvalidInputException {
		//Read data
		String out = "import pec.core.*; \n";
		BufferedInputStream file = new BufferedInputStream(new FileInputStream(new File(ExcelPath)));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		String pen = workbook.getSheetAt(0).getRow(0).getCell(0).toString();
		String service = workbook.getSheetAt(0).getRow(0).getCell(2).toString().equals("KEY") ? "Line" : "Disappear";
		out += "Pen " + pen + " = new " + service + "(\"" + OutPath + "\"); \n";
		for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {
			Sheet sheet = workbook.getSheetAt(sheetCount);
			for(Row row : sheet) {
				String rowData = "";
				for(Cell cell : row) {
					rowData += cell.toString();
				}
				out += rowData + '\n';
			}

		}
		workbook.close();
		
		//Parse code
		Interpreter interpreter = new Interpreter();
		interpreter.eval(out);
	}
}
