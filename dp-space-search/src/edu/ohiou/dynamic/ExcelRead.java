package edu.ohiou.dynamic;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.cellwalk.CellWalk;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ExcelRead {

	public ExcelRead() {
		// TODO Auto-generated constructor stub
	}
//Capacity Data
	public static List<Object> getData ( String range, Workbook wb )
	{ 
		List <Object> capacity= new ArrayList <Object>();
		CellRangeAddress cellRange = CellRangeAddress.valueOf(range);
		Sheet sheet= wb.getSheetAt(0);
		//CellRangeAddress range2= CellRangeAddress.valueOf(range);
	//	CellWalk cellwalk= new CellWalk (sheet, range2);
		for (int i = cellRange.getFirstRow(); i<=cellRange.getLastRow();i++)
		{
		
			Row row= sheet.getRow(i);
			 for (int colNum = cellRange.getFirstColumn(); colNum <= cellRange.getLastColumn(); colNum++) 
			 {
	                Cell cell = row.getCell(colNum);
	                Object cellValue = getValues(cell);
	                capacity.add(cellValue);
	            }
		}
		
			
	
		for (Object i: capacity )
		{
			System.out.print(i);
			System.out.println("\n");
		}
		return capacity;
	}
	
	public static Object getValues (Cell cell)
	
	{
		if (cell==null)
		{
			return 0;
		}
		switch (cell.getCellType())
		{
		case  Cell.CELL_TYPE_NUMERIC:			
			return cell.getNumericCellValue();
	
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case  Cell.CELL_TYPE_BLANK:		
			return 0;
		default:
			return 0;
	
		}
		
		
	}
	public static void main(String[] args) throws IOException {
		FileInputStream fis=new FileInputStream(new File("C:\\Users\\HP\\Documents\\DataFile2.xlsx"));  
		//creating workbook instance that refers to .xls file  
		Workbook wb=new XSSFWorkbook(fis);   
		getData( "B27:K27",wb);
	
//		//creating a Sheet object to retrieve the object
//		Sheet sheet=wb.getSheetAt(0);  
//		//evaluating cell type   
//		FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();  
//		for(Row row: sheet)     //iteration over row using for each loop  
//		{  
//		for(Cell cell: row)    //iteration over cell using for each loop  
//		{  
//		switch(formulaEvaluator.evaluateInCell(cell).getCellType())  
//		{  
//		case Cell.CELL_TYPE_NUMERIC:   //field that represents numeric cell type  
//		//getting the value of the cell as a number  
//		System.out.print(cell.getNumericCellValue()+ "\t\t");   
//		break;  
//		case Cell.CELL_TYPE_STRING:    //field that represents string cell type  
//		//getting the value of the cell as a string  
//		System.out.print(cell.getStringCellValue()+ "\t\t");  
//		break;  
//		}  
//		}  
//		System.out.println();  
//		}  
//		}  
	}	 
	}


