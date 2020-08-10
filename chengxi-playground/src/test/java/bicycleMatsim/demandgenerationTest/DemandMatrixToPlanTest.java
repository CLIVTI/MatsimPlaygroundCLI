package bicycleMatsim.demandgenerationTest;

import java.io.IOException;

import com.opencsv.exceptions.CsvException;

import bicycleMatsim.demandgeneration.DemandMatrixToPlan;

public class DemandMatrixToPlanTest {

	public static void main(String[] args) throws IOException, CsvException {
		// TODO Auto-generated method stub
		final String inputPath = "C:/Users/ChengxiL/git/MatsimPlaygroundCLI/chengxi-playground/src/test/resources/";
		final String DemandFile = inputPath + "ODDemand.csv";
		final String planFile = inputPath + "population.xml";
		DemandMatrixToPlan planreader = new DemandMatrixToPlan(DemandFile, planFile);
		planreader.generatePlan();
	}

}
