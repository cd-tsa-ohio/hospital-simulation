package edu.ohiou.dynamic;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.cellwalk.CellWalk;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ohiou.mfgresearch.labimp.basis.ViewObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ExcelRead {
static String XLSX_FOLDER;
	public ExcelRead() {
		// TODO Auto-generated constructor stub
	}
public static void  getData ( ) throws IOException
	
	{ 
		File excelFile = ViewObject.chooseFile("XLSX", "Excel", "Choose the file with the example data ");
		FileInputStream fis=new FileInputStream(excelFile);  
		Workbook wb=new XSSFWorkbook(fis); 
		Name patientdatarangename= wb.getName("PatientData");	
		Name capacity2Range=wb.getName("Capacities");
		Name resourcesRange=wb.getName("Resources");		
		CellRangeAddress capacitycellRange = CellRangeAddress.valueOf(capacity2Range.getRefersToFormula());
		CellRangeAddress patientcellRange2 = CellRangeAddress.valueOf(patientdatarangename.getRefersToFormula());		
		CellRangeAddress resourceCellRabgeName = CellRangeAddress.valueOf(resourcesRange.getRefersToFormula());
		Sheet sheet= wb.getSheetAt(0);		
		for (int i = capacitycellRange.getFirstRow(); i<=capacitycellRange.getLastRow();i++) {
			
			 Row row= sheet.getRow(i);
			 List<Integer> cap = new ArrayList<>();
		 for (int colNum = capacitycellRange.getFirstColumn(); colNum <= capacitycellRange.getLastColumn(); colNum++) 	 
		 {		
			 Cell cell = row.getCell(colNum);
           int cellValue = getValues(cell);
           cap.add(cellValue);          
		 }
		 PeriodicProblemDay.capacitylist.add(cap);
		 }
		// filling resources
		for (int i = resourceCellRabgeName.getFirstRow(); i<=resourceCellRabgeName.getLastRow();i++) {
			
			 Row row= sheet.getRow(i);
			 List<Integer> res = new ArrayList<>();
		 for (int colNum = resourceCellRabgeName.getFirstColumn(); colNum <= resourceCellRabgeName.getLastColumn(); colNum++) 	 
		 {		
			 Cell cell = row.getCell(colNum);
            int cellValue = getValues(cell);
            res.add(cellValue);          
		 }
		 int[] resArray = res.stream().mapToInt(Integer::intValue).toArray();
		 PeriodicProblemDay.resourcedata.add(resArray);
		 }
		for (int i = patientcellRange2.getFirstRow(); i<=patientcellRange2.getLastRow();i++)
		{
			Row row= sheet.getRow(i);
			int los=0;		
			int FirstDay=0;			
			 for (int colNum = patientcellRange2.getFirstColumn(); colNum <= patientcellRange2.getLastColumn(); colNum++) 
			 {			 	
	                Cell cell = row.getCell(colNum);
	                Object cellValue = getValues(cell);	                
	                if (cellValue != null && ((Integer)cellValue) != 0) 
	                {	                	
	                	if (los-((Integer)cellValue)==-1)
	                	{
	                		FirstDay=colNum;
	                		los++;                		
	                	}
	                	else 
	                	{
	                		los++;
	                	}              
	                }                
	          }
			 PeriodicProblemDay.data.add(new int [] {FirstDay,los});
		}			
		wb.close();			
		
	}
	public static Integer getValues (Cell cell)
	
	{
		if (cell==null)
		{
			return 0;
		}
		switch (cell.getCellType())
		{
		case  Cell.CELL_TYPE_NUMERIC:			
			return (int) cell.getNumericCellValue();
	
		case Cell.CELL_TYPE_STRING:
			return Integer.parseInt(cell.getStringCellValue());
		case  Cell.CELL_TYPE_BLANK:		
			return 0;
		default:
			return 0;
	
		}		
	}
	public static void main(String[] args) 
	{
		try {
            ExcelRead.getData();
           
        } catch (IOException e) {
            System.out.println("There is an error while reading data  ");
	}
	}

}
