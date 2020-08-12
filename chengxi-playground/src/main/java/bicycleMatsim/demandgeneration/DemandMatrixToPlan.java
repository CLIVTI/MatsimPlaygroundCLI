package bicycleMatsim.demandgeneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ActivityParams;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;

import com.google.common.collect.Table;
import com.opencsv.exceptions.CsvException;

import bicycleMatsim.utility.CsvReaderToIteratable;
import bicycleMatsim.utility.MultinomialDistributionSamplerMap;

public class DemandMatrixToPlan {
	private final String demandMatrixFileName;
	private final String matsimPlanFileName;
	private final Scenario scenario;


	public DemandMatrixToPlan(String demandMatrixFileName, String networkFileName, String matsimPlanFileName) {
		super();
		this.demandMatrixFileName = demandMatrixFileName;
		this.matsimPlanFileName = matsimPlanFileName;
		
		// create a config file with 2 activities home and work
		Config config = ConfigUtils.createConfig();

		config.controler().setLastIteration(10);
		ActivityParams home = new ActivityParams("home");
		home.setTypicalDuration(16 * 60 * 60);
		config.planCalcScore().addActivityParams(home);
		ActivityParams work = new ActivityParams("work");
		work.setTypicalDuration(8 * 60 * 60);
		config.planCalcScore().addActivityParams(work);
		
		this.scenario = ScenarioUtils.createScenario(config);
		(new MatsimNetworkReader(this.scenario.getNetwork())).readFile(networkFileName);
		
	}


	public void generatePlan() throws IOException, CsvException {
		CsvReaderToIteratable demandMatrixReader = new CsvReaderToIteratable(this.demandMatrixFileName,';');
		Table<String, String, String> demandMatrixTable = demandMatrixReader.readTableWithUniqueID(0);
		
		// calculate total number of trips per zone (row)
		Set<String> transCadOringinZoneIDSet=demandMatrixTable.rowKeySet();
		double totalNumberOfTrips =0;
		HashMap<String, String> numberOfTripsInEachOriginZone= new HashMap<>();
		
		// loop each origin zone
		for (String TransCadOriginZoneID: transCadOringinZoneIDSet) {
			Map<String, String> oneOriginToAllDestination = demandMatrixTable.row(TransCadOriginZoneID);
			double totalNumberOfTripInOringinZone = 0;
			for (Map.Entry<String, String> entry : oneOriginToAllDestination.entrySet()) {
				String tripsFromAOriginToADestination=entry.getValue();
				if (tripsFromAOriginToADestination.isEmpty()) {
					tripsFromAOriginToADestination="0";
				}
				totalNumberOfTripInOringinZone=totalNumberOfTripInOringinZone+Double.parseDouble(tripsFromAOriginToADestination);
			}
			numberOfTripsInEachOriginZone.put(TransCadOriginZoneID,Double.toString(totalNumberOfTripInOringinZone));
			totalNumberOfTrips=totalNumberOfTrips+totalNumberOfTripInOringinZone;
			System.out.println("Oringin zone ID: "+TransCadOriginZoneID + " has "+ totalNumberOfTripInOringinZone +  " zones.");
		}
		
		
		// sample number of trips per zone
		MultinomialDistributionSamplerMap originZoneTripSampler= new MultinomialDistributionSamplerMap(numberOfTripsInEachOriginZone);
        int totalNumberOfTripsToBeSampled = (int) (totalNumberOfTrips/5);  // assuming we sample 20% of total number of out-of-home trips.
        String[] sampledTripsFromOrigin=originZoneTripSampler.sampleMapWithoutReplacement(totalNumberOfTripsToBeSampled);
        for (int i=0; i<sampledTripsFromOrigin.length;i++) {
			System.out.println("Map sampler's sample without replacement is: "+ sampledTripsFromOrigin[i]);
		}
        
        // for each sampled trip, we sample a destination and randomly generate a trip timing
        // create a histogram of trip timing, 
        HashMap<String,String> departureTimeDistribution=new HashMap<>();
        departureTimeDistribution.put("1", "1");
        departureTimeDistribution.put("2", "1");
        departureTimeDistribution.put("3", "2");
        departureTimeDistribution.put("4", "3");
        departureTimeDistribution.put("5", "8");
        departureTimeDistribution.put("6", "10");
        departureTimeDistribution.put("7", "15");
        departureTimeDistribution.put("8", "15");
        departureTimeDistribution.put("9", "10");
        departureTimeDistribution.put("10", "5");
        departureTimeDistribution.put("11", "5");
        departureTimeDistribution.put("12", "3");
        departureTimeDistribution.put("13", "3");
        departureTimeDistribution.put("14", "3");
        departureTimeDistribution.put("15", "3");
        departureTimeDistribution.put("16", "2");
        departureTimeDistribution.put("17", "2");
        departureTimeDistribution.put("18", "2");
        departureTimeDistribution.put("19", "1");
        departureTimeDistribution.put("20", "1");
        departureTimeDistribution.put("21", "1");
        departureTimeDistribution.put("22", "1");
        departureTimeDistribution.put("23", "1");
        departureTimeDistribution.put("24", "1");
        
        for (int i=0; i<sampledTripsFromOrigin.length;i++) {
        	String personID="person_"+(i+1);
        	String tripOrigin=sampledTripsFromOrigin[i];
        	// sample a destination
        	Map<String, String> oneOriginToAllDestination = demandMatrixTable.row(tripOrigin);
        	MultinomialDistributionSamplerMap destinationZoneTripSampler= new MultinomialDistributionSamplerMap(oneOriginToAllDestination);
        	String tripDestination=destinationZoneTripSampler.sampleMap();
        	
        	// randomly generate a departure time
        	MultinomialDistributionSamplerMap departureTimeSampler= new MultinomialDistributionSamplerMap(departureTimeDistribution);
        	String departureTimeInHour=departureTimeSampler.sampleMap();
        	
        	System.out.println("trip " + i + " starts from zone: " + tripOrigin +", departs at: "+ departureTimeInHour + ", ends at zone: " + tripDestination);
        	
        	createOnePersonPlan(this.scenario,personID,tripOrigin,tripDestination,departureTimeInHour);
        }
        
        PopulationWriter popwriter = new PopulationWriter(scenario.getPopulation(), this.scenario.getNetwork());
		popwriter.write(this.matsimPlanFileName);
		
//		Controler controler = new Controler(scenario);
//		controler.getConfig().controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);
//		controler.run();
		
		
	}


	private static void createOnePersonPlan(Scenario scenario, String personID, String tripOrigin, String tripDestination,
			String departureTimeInHour) {
		// a method to create one person plan with home-work-home pattern 
		Population population = scenario.getPopulation();
		final Person person = population.getFactory().createPerson(Id.createPersonId(personID));
		Plan plan = population.getFactory().createPlan();
		Network network=scenario.getNetwork();
		Map<Id<Node>, ? extends Node> NodeMap = network.getNodes();
		
		
		Node originNode=NodeMap.get(Id.createNodeId(tripOrigin));
		Coord originCoord = originNode.getCoord();
		
		Node destinationNode=NodeMap.get(Id.createNodeId(tripDestination));
		Coord destinationCoord = destinationNode.getCoord();
		
		Activity home = population.getFactory().createActivityFromCoord("home", originCoord);
		home.setEndTime(Double.parseDouble(departureTimeInHour)*60*60);
		plan.addActivity(home);
		
		Leg hinweg = population.getFactory().createLeg("bike");
		plan.addLeg(hinweg);
		
		Activity work = population.getFactory().createActivityFromCoord("work", destinationCoord);
		if (Double.parseDouble(departureTimeInHour)+8.0<=21) {
			work.setEndTime((Double.parseDouble(departureTimeInHour)+8.0)*60*60);
		}else {
			work.setEndTime(21*60*60);
		}
		plan.addActivity(work);
		
		
		Leg rueckweg = population.getFactory().createLeg("bike");
		plan.addLeg(rueckweg);

		Activity home2 = population.getFactory().createActivityFromCoord("home", originCoord);
		plan.addActivity(home2);

		person.addPlan(plan);
		population.addPerson(person);

		
	}





}
