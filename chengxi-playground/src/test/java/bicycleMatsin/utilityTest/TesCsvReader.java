package bicycleMatsin.utilityTest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Table;
import com.opencsv.exceptions.CsvException;

import bicycleInMatsim.utility.CsvReaderToIteratable;


public class TesCsvReader {
	

	public static void main(String[] args) {
		
		final String SAMPLE_CSV_FILE_PATH = "C:/Users/ChengxiL/OneDrive – VTI, Statens väg-och transportforskningsinstitut/JavaChengxiLiu/src/test/resources/csvReaderTest.csv";

		CsvReaderToIteratable csvToUserClass = new CsvReaderToIteratable(SAMPLE_CSV_FILE_PATH,';');
		
		try {
			Table<String, String, String> result = csvToUserClass.readTableWithUniqueID("Name");
			// Set<String> columnKey=result.columnKeySet();
			
			Map<String, String> specificColumn = result.column("Phone"); 
			 for (Map.Entry<String, String> cell : specificColumn.entrySet()) { 
		            System.out.println("row name : " + cell.getKey() + ", value Name : " + cell.getValue()); 
		        } 
			 
			 System.out.println(result.get("CCC", "Country"));
			
		} catch (IOException | CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    }

}
