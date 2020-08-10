package bicycleMatsim.demandgeneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
		int NumberofOriginZones=transCadOringinZoneIDSet.size();
		LinkedHashMap<String, Double> NumberOfTripsInEachOriginZone= new LinkedHashMap<String, Double>();
		
		// loop each origin zone
		for (String TransCadOriginZoneID: transCadOringinZoneIDSet) {
			Map<String, String> oneOriginToAllDestination = demandMatrixTable.row(TransCadOriginZoneID);
			double totalNumberOfTripInOringinZone = 0;
			for (Map.Entry<String, String> entry : oneOriginToAllDestination.entrySet()) {
				String TripsFromAOriginToADestination=entry.getValue();
				if (TripsFromAOriginToADestination.isEmpty()) {
					TripsFromAOriginToADestination="0";
				}
				totalNumberOfTripInOringinZone=totalNumberOfTripInOringinZone+Double.parseDouble(TripsFromAOriginToADestination);
			}
			NumberOfTripsInEachOriginZone.put(TransCadOriginZoneID,totalNumberOfTripInOringinZone);
			System.out.println("Oringin zone ID: "+TransCadOriginZoneID + " has "+ totalNumberOfTripInOringinZone +  " zones.");
		}
		
		
		// sample number of trips per zone
		for (Map.Entry<String, Double> entry : NumberOfTripsInEachOriginZone.entrySet()) {
			
		}
		
		// loop each origin zone again 
		for (String TransCadOriginZoneID: transCadOringinZoneIDSet) {
			Map<String, String> oneOriginToAllDestination = demandMatrixTable.row(TransCadOriginZoneID); 
			// 3 elements are needed here: 
			// 1.OriginZoneID is TransCadOriginZoneID
			// 2. a string[] of destination zone ID 
			// 3. a double[] of the number of trips 
			int numberOfDestinationZones= oneOriginToAllDestination.size();
			String[] destinationZoneIDList = new String[numberOfDestinationZones];
			double[] numberOfTripsList = new double[numberOfDestinationZones];
			double totalNumberOfTripInOringinZone = 0;
			int counter =0;
			
			// loop each row (each destination zone)
			for (Map.Entry<String, String> entry : oneOriginToAllDestination.entrySet()) {
				String TripsFromAOriginToADestination=entry.getValue();
				if (TripsFromAOriginToADestination.isEmpty()) {
					TripsFromAOriginToADestination="0";
				}
				destinationZoneIDList[counter]=entry.getKey();
				numberOfTripsList[counter]=Double.parseDouble(TripsFromAOriginToADestination);
				
				totalNumberOfTripInOringinZone=totalNumberOfTripInOringinZone+Double.parseDouble(TripsFromAOriginToADestination);
				counter++;
			}
			
			
		}
		
		
		
	}


}
