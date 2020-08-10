package bicycleMatsim.demandgeneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Table;
import com.opencsv.exceptions.CsvException;

import bicycleMatsim.utility.CsvReaderToIteratable;

public class DemandMatrixToPlan {
	private Random random = new Random();
	private final String demandMatrixFileName;
	private final String matsimPlanFileName;
	


	public DemandMatrixToPlan(String demandMatrixFileName, String matsimPlanFileName) {
		super();
		this.demandMatrixFileName = demandMatrixFileName;
		this.matsimPlanFileName = matsimPlanFileName;
	}


	public void generatePlan() throws IOException, CsvException {
		CsvReaderToIteratable demandMatrixReader = new CsvReaderToIteratable(this.demandMatrixFileName,';');
		Table<String, String, String> demandMatrixTable = demandMatrixReader.readTableWithUniqueID(0);
		
		// calculate total number of trips per zone (row)
		Set<String> transCadOringinZoneIDSet=demandMatrixTable.rowKeySet();
		
		// calculate number of trips per origin zones
		for (String TransCadOriginZoneID: transCadOringinZoneIDSet) {
			Map<String, String> oneOriginToAllDestination = demandMatrixTable.row(TransCadOriginZoneID); 
			// 3 elements are needed here: 
			// 1.OriginZoneID is TransCadOriginZoneID
			// 2. a string[] of destination zone ID 
			// 3. a double[] of the number of trips 
			int numberOfDestinationZones= oneOriginToAllDestination.size();
			double[] destinationZoneIDList = new double[numberOfDestinationZones];
			double[] numberOfTripsList = new double[numberOfDestinationZones];
			double totalNumberOfTripInOringinZone = 0;
			int counter =0;
			
			for (Map.Entry<String, String> entry : oneOriginToAllDestination.entrySet()) {
				String TripsFromAOriginToADestination=entry.getValue();
				if (TripsFromAOriginToADestination.isEmpty()) {
					TripsFromAOriginToADestination="0";
				}
				
				if (TripsFromAOriginToADestination.equals(TransCadOriginZoneID)) {
					TripsFromAOriginToADestination="0";
				}
				totalNumberOfTripInOringinZone=totalNumberOfTripInOringinZone+Double.parseDouble(TripsFromAOriginToADestination);
			}
			
			System.out.println("Oringin zone ID: "+TransCadOriginZoneID + " has "+ totalNumberOfTripInOringinZone +  " zones.");
		}
		
		
		
	}


}
