package bicycleMatsim.configgeneration;

import java.util.Arrays;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ActivityParams;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;

public class WriteConfigFromNetworkAndPopulation {
	private final String matsimPlanFileName;
	private final String matsimNetworkFileName;
	private final String matsimConfigFileName;
	
	public WriteConfigFromNetworkAndPopulation(String networkFile, String planFile, String configFile) {
		this.matsimNetworkFileName = networkFile;
		this.matsimPlanFileName = planFile;
		this.matsimConfigFileName=configFile;
		// TODO Auto-generated constructor stub
	}
	
	public void WriteConfig() {
		Config config = ConfigUtils.createConfig();
		Scenario scenario = ScenarioUtils.createScenario(config);
		(new MatsimNetworkReader(scenario.getNetwork())).readFile(this.matsimNetworkFileName);
		
		PopulationReader populationReader = new PopulationReader(scenario);
		populationReader.readFile(this.matsimPlanFileName);
		
		
		// setup config file variables
		config.controler().setLastIteration(1);
		ActivityParams home = new ActivityParams("home");
		home.setTypicalDuration(16 * 60 * 60);
		config.planCalcScore().addActivityParams(home);
		ActivityParams work = new ActivityParams("work");
		work.setTypicalDuration(8 * 60 * 60);
		config.planCalcScore().addActivityParams(work);
		
		config.controler().setOutputDirectory("...");
		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		
		
		config.plansCalcRoute().setNetworkModes( Arrays.asList( TransportMode.car, TransportMode.bike ) );
		config.plansCalcRoute().removeModeRoutingParams( TransportMode.bike );
		
		ConfigUtils.writeConfig(config, this.matsimConfigFileName);
	}

}
