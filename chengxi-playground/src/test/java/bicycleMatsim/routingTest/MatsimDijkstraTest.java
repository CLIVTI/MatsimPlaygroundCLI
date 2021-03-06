package bicycleMatsim.routingTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.scenario.ScenarioUtils;

import bicycleMatsim.routing.MatsimDijkstra;
import bicycleMatsim.routing.TravelDisutilityBicycle;

public class MatsimDijkstraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final String inputPath = "C:/Users/ChengxiL/git/MatsimPlaygroundCLI/chengxi-playground/src/test/resources/";
		final String matsimPlainFile = inputPath + "network_test.xml";
		Config config = ConfigUtils.createConfig();
		Scenario scenario = ScenarioUtils.createScenario(config);
		(new MatsimNetworkReader(scenario.getNetwork())).readFile(matsimPlainFile);
		TravelDisutility travelDisutility = new TravelDisutilityBicycle("generalizedCost");
		Network testNetwork = scenario.getNetwork();
		MatsimDijkstra dijkstraRouter = new MatsimDijkstra(testNetwork, travelDisutility, null);
		Map<Id<Node>, ? extends Node> allNodes = testNetwork.getNodes();
		Node node_6000 = allNodes.get(Id.create("6000", Node.class));
		Node node_6005 = allNodes.get(Id.create("6005", Node.class));
		Path testPath = dijkstraRouter.calcLeastCostPath(node_6000, node_6005, 0, null, null);
		System.out.println(testPath.travelCost);
		List<Link> linksInPath = testPath.links;
		for (Link link : linksInPath) {
			System.out.println("link: "+link.getId());
		}
		List<Node> nodeInPath = testPath.nodes;
		for (Node node :nodeInPath) {
			System.out.println("node: "+node.getId());
		}
		
		Node node_6003 = allNodes.get(Id.create("6003", Node.class));
		Node node_6004 = allNodes.get(Id.create("6004", Node.class));
		List<Node> toNodes = new ArrayList<Node>();
		toNodes.add(node_6005);
		toNodes.add(node_6003);
		toNodes.add(node_6004);
		Map<Id<Node>, Path> pathMap = dijkstraRouter.calcOnetoManyLeastCostPath(node_6000, toNodes, 0, null, null);
		for (Entry<Id<Node>, Path> entry:pathMap.entrySet()) {
			System.out.println("end node is: "+entry.getKey());
			Path path = entry.getValue();
			List<Link> pathLinks = path.links;
			for (Link link : pathLinks) {
				System.out.println("end node is: "+entry.getKey()+" has the link: "+link.getId());
			}
		}
		
		
	}

}
