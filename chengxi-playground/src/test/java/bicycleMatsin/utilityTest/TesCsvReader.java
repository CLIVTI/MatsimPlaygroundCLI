package bicycleMatsin.utilityTest;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Table;
import com.opencsv.exceptions.CsvException;

import bicycleMatsim.utility.CsvReaderToIteratable;


public class TesCsvReader {
	

	public static void main(String[] args) {
		
		final String inputPath = "C:/Users/ChengxiL/git/MatsimPlaygroundCLI/chengxi-playground/src/test/resources/";
		final String SAMPLE_CSV_FILE_PATH = inputPath+"csvReaderTest.csv";

		CsvReaderToIteratable csvToUserClass = new CsvReaderToIteratable(SAMPLE_CSV_FILE_PATH,';');
		
		try {
			Table<String, String, String> result = csvToUserClass.readTableWithUniqueID("Name");
			// Set<String> columnKey=result.columnKeySet();
			
			Map<String, String> specificColumn = result.column("Phone"); 
			 for (Map.Entry<String, String> cell : specificColumn.entrySet()) { 
		            System.out.println("row name : " + cell.getKey() + ", column name : " + "Phone" +", value Name : " + cell.getValue()); 
		        } 
			
		} catch (IOException | CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    }

}
