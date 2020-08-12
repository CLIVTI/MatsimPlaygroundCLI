package bicycleMatsim.configgenerationTest;

import bicycleMatsim.configgeneration.WriteConfigFromNetworkAndPopulation;

public class WriteConfigTest {


	public static void main(String[] args) {
		// requires to run importNetwork and demandGeneration to generate network and population xml files.
		final String inputPath = "C:/Users/ChengxiL/git/MatsimPlaygroundCLI/chengxi-playground/src/test/resources/";
		final String NetworkFile = inputPath + "network_test.xml";
		final String planFile = inputPath + "population.xml";
		final String configFile= inputPath+ "config_test.xml";
		WriteConfigFromNetworkAndPopulation configWriter = new WriteConfigFromNetworkAndPopulation(NetworkFile, planFile, configFile);
		configWriter.WriteConfig();
	}

}
