package bicycleMatsim.importnetwork;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.utils.objectattributes.ObjectAttributes;

import com.google.common.collect.Table;
import com.opencsv.exceptions.CsvException;

import bicycleInMatsim.utility.CsvReaderToIteratable;
// import floetteroed.utilities.Units;

public class TransCad2MATSimNetwork {
	private final String tcNodesFileName;

	private final String tcLinksFileName;

	private final String tcConnectorsFileName;

	private final String matsimPlainNetworkFileName;

	private final String matsimFullFileName;

	private final String linkAttributesFileName;
	
	
	



	public TransCad2MATSimNetwork(String tcNodesFileName, String tcLinksFileName, String tcConnectorsFileName,
			String matsimPlainNetworkFileName, String matsimFullFileName, String linkAttributesFileName) {
		super();
		this.tcNodesFileName = tcNodesFileName;
		this.tcLinksFileName = tcLinksFileName;
		this.tcConnectorsFileName = tcConnectorsFileName;
		this.matsimPlainNetworkFileName = matsimPlainNetworkFileName;
		this.matsimFullFileName = matsimFullFileName;
		this.linkAttributesFileName = linkAttributesFileName;
	} // end constructor


	private void run() throws IOException, CsvException {
		// TODO Auto-generated method stub
        final Network matsimNetwork = NetworkUtils.createNetwork();
		final NetworkFactory matsimNetworkFactory = matsimNetwork.getFactory();
		final ObjectAttributes linkAttributes = new ObjectAttributes();
		
		CsvReaderToIteratable nodeReader = new CsvReaderToIteratable(this.tcNodesFileName,';');
		Table<String, String, String> nodeTable = nodeReader.readTableWithUniqueID("TransCadID");

		final CoordinateTransformation coordinateTransform = StockholmTransformationFactory.getCoordinateTransformation(
				StockholmTransformationFactory.WGS84, StockholmTransformationFactory.WGS84_SWEREF99);
        
		// Save the nodes into Matsim nodes
		//-----------------------------------
		Set<String> TransCadNodeIDSet=nodeTable.rowKeySet();
		for (String TransCadNodeID: TransCadNodeIDSet) {
			Map<String, String> ANode = nodeTable.row(TransCadNodeID); 
			// transform each node into Matsim node object
			double NodeLatitude= Double.parseDouble(ANode.get("latitude"));
			double Nodelongtitude= Double.parseDouble(ANode.get("longtitude"));		
			double NodeAltitude= Double.parseDouble(ANode.get("altitude"));	
			final Coord coord = coordinateTransform.transform(new Coord(1e-6*Nodelongtitude, 1e-6*NodeLatitude, NodeAltitude));
			final Node matsimNode = matsimNetworkFactory.createNode(Id.create(TransCadNodeID, Node.class),coord);
			System.out.println("Node added: "+TransCadNodeID);
			matsimNetwork.addNode(matsimNode);
			
		}
//		 Map<Id<Node>, ? extends Node> Nodemap = matsimNetwork.getNodes();
//		 for (Entry<Id<Node>, ? extends Node> node:Nodemap.entrySet()) {
//			 Id<Node> nodeId= node.getKey();
//			 System.out.println("Node added: "+nodeId);
//		 }
		nodeTable=null;
		//----------------------------------
		
		// Save the links into Matsim links
		//----------------------------------
		// to create matsim links you need 3 elements, ID (string) fromNode (Node) and toNode(Node)
		CsvReaderToIteratable linkReader = new CsvReaderToIteratable(this.tcLinksFileName,';');
		Table<String, String, String> linkTable = linkReader.readTableWithUniqueID("TransCadID");
		Set<String> TransCadLinkIDSet=linkTable.rowKeySet();
		for (String TransCadLinkID: TransCadLinkIDSet) {
			Map<String, String> ALink = linkTable.row(TransCadLinkID); 
			String FromNode= ALink.get("fromNode");
			String ToNode= ALink.get("toNode");	
			
			// create a link
			final Node matsimFromNode = matsimNetwork.getNodes().get(Id.create(FromNode, Node.class));
			final Node matsimToNode = matsimNetwork.getNodes().get(Id.create(ToNode, Node.class));		
			final Link matsimLink = matsimNetworkFactory.createLink(Id.create(TransCadLinkID, Link.class),
					matsimFromNode, matsimToNode);
			// set link length and speed as default attribute to links
			double LinkLengthKM= Double.parseDouble(ALink.get("length"));
			double LinkFreeSpeedKM_H= Double.parseDouble(ALink.get("bicycleTravelTime"));
			matsimLink.setLength(LinkLengthKM*1000); // change back to: matsimLink.setLength(LinkLengthKM * Units.M_PER_KM);
			matsimLink.setFreespeed(LinkFreeSpeedKM_H/3.6);   // change back to: matsimLink.setFreespeed(LinkFreeSpeedKM_H * Units.M_S_PER_KM_H);  when GunnarRepo is updated.
			matsimNetwork.addLink(matsimLink);
			
			// specify which other attributes you want to save as link attributes
			double bicycleSpeedM_S= Double.parseDouble(ALink.get("bicycleSpeed"))/3.6;// change back to: double bicycleSpeedM_S= Double.parseDouble(ALink.get("bicycleSpeed")) * Units.M_S_PER_KM_H;
			double bicycleGeneralizedCost= Double.parseDouble(ALink.get("generalizedCost"));
			
			
			linkAttributes.putAttribute(TransCadLinkID, "bicycleSpeed_M_S",bicycleSpeedM_S);
			linkAttributes.putAttribute(TransCadLinkID, "generalizedCost",bicycleGeneralizedCost);
			
			
			System.out.println("Link added: "+TransCadLinkID);
		}
		//----------------------------------
		
		
		
	} // end run()



	public static void main(String[] args) throws IOException, CsvException {
		// final String inputPath = "./ihop2/network-input/";
		final String inputPath = "C:/Users/ChengxiL/git/MatsimPlaygroundCLI/chengxi-playground/src/test/resources/";
		final String nodesFile = inputPath + "Node.csv";
		final String ConnectorsFile = inputPath + "Lane Connectors.csv";
		final String linksFile = inputPath + "Links.csv";
		
		// final String outputPath = "./ihop2/network-output/";
		final String outputPath = "/.../";
		final String matsimPlainFile = outputPath + "network.xml";
		final String matsimFullFile = outputPath + "network-raw.xml";
		final String linkAttributesFile = outputPath + "link-attributes.xml";
		
		TransCad2MATSimNetwork networktransformer = new TransCad2MATSimNetwork(nodesFile, linksFile, ConnectorsFile,
				matsimPlainFile, matsimFullFile, linkAttributesFile);
		
		networktransformer.run();
	} // end main





}
