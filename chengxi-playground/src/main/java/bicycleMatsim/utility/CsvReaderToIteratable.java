package bicycleMatsim.utility;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.collect.HashBasedTable; 
import com.google.common.collect.Table;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvException;



public class CsvReaderToIteratable {
	private String CSV_FILE_PATH;
	private char saparater;
	
	
	public CsvReaderToIteratable(String sampleCsvFilePath, char saparater) {
		this.CSV_FILE_PATH=sampleCsvFilePath;
		this.saparater=saparater;
	}

	
	public Table<String, String, String> readTableWithUniqueID(String id) throws IOException, CsvException {
		
		List<String[]> records= readTable();
//		Reader reader = Files.newBufferedReader(Paths.get(this.CSV_FILE_PATH));
//	    CSVParser parser = new CSVParserBuilder()
//	        .withSeparator(this.saparater)
//	        .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
//	        .withIgnoreLeadingWhiteSpace(true)
//	        .build();
//	    CSVReader csvReader = new CSVReaderBuilder(reader)
//	            .withCSVParser(parser)
//	            .build();
//	    
//        // first row header
//	    List<String[]> records = csvReader.readAll();
	    String[] header= records.get(0);
	    
	    Table<String, String, String> output = HashBasedTable.create(); 
	    int headerIDLocation=0;
	    for (String headerID:header) {
	    	if(headerID.equals(id)) {
	    		break;
	    	}
	    	headerIDLocation++;
	    }
	    
	    records.remove(0);
	    for (String[] record : records) {
	        // System.out.println("Id : " + record[headerIDLocation]);
	        
	        for (int counter=0; counter<record.length;counter++) {
	        	if (counter!=headerIDLocation) {
	        		output.put(record[headerIDLocation], header[counter], record[counter]);
	        	}
	        }

	    }
	    

	    return output;

	}
	
	
	
	
	public Table<String, String, String> readTableWithUniqueID(int headerIDLocation) throws IOException, CsvException {
		// int headerIDLocation gives which column is the ID column 
		List<String[]> records= readTable();
	    String[] header= records.get(0);    
	    Table<String, String, String> output = HashBasedTable.create();     
	    records.remove(0);
	    for (String[] record : records) {
//	        int headercounter = 0;
//	        for (String recordelement:record) {
//	        	 // System.out.println(header[headercounter] + ": "+ recordelement);  
//	        	 output.put(record[headerIDLocation], header[headercounter], recordelement);
//	        	 headercounter++;
//	        }
	        
	        for (int counter=0; counter<record.length;counter++) {
	        	if (counter!=headerIDLocation) {
	        		output.put(record[headerIDLocation], header[counter], record[counter]);
	        	}
	        }

	    }
	    

	    return output;

	}
	
	
	private List<String[]> readTable() throws IOException, CsvException{
		Reader reader = Files.newBufferedReader(Paths.get(this.CSV_FILE_PATH));
	    CSVParser parser = new CSVParserBuilder()
	        .withSeparator(this.saparater)
	        .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
	        .withIgnoreLeadingWhiteSpace(true)
	        .build();
	    CSVReader csvReader = new CSVReaderBuilder(reader)
	            .withCSVParser(parser)
	            .build();
	    List<String[]> records= csvReader.readAll();
	    csvReader.close();
	    reader.close();
		return records;
	    
	}
	
	
	

	public String getCSV_FILE_PATH() {
		return CSV_FILE_PATH;
	}

	public char getSaparater() {
		return saparater;
	}





	

}
