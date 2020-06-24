package bicycleMatsim.RUCY.importNetwork;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.utils.objectattributes.ObjectAttributes;

import com.google.common.collect.Table;
import com.opencsv.exceptions.CsvException;

import bicycleInMatsim.utility.CsvReaderToIteratable;

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
		CsvReaderToIteratable nodeReader = new CsvReaderToIteratable(this.tcNodesFileName,';');
		Table<String, String, String> nodeTable = nodeReader.readTableWithUniqueID("TransCadID");
		
		
		final Network matsimNetwork = NetworkUtils.createNetwork();
		final NetworkFactory matsimNetworkFactory = matsimNetwork.getFactory();
		final ObjectAttributes linkAttributes = new ObjectAttributes();

		final CoordinateTransformation coordinateTransform = StockholmTransformationFactory.getCoordinateTransformation(
				StockholmTransformationFactory.WGS84, StockholmTransformationFactory.WGS84_SWEREF99);
        
		// Save the nodes into Matsim nodes
		//-----------------------------------
		Set<String> TransCadIDSet=nodeTable.rowKeySet();
		for (String TransCadID: TransCadIDSet) {
			Map<String, String> ANode = nodeTable.row(TransCadID); 
			// transform each node into Matsim node object
			double NodeLatitude= Double.parseDouble(ANode.get("latitude"));
			double Nodelongtitude= Double.parseDouble(ANode.get("longtitude"));		
			double NodeAltitude= Double.parseDouble(ANode.get("altitude"));	
			final Coord coord = coordinateTransform.transform(new Coord(1e-6*Nodelongtitude, 1e-6*NodeLatitude, NodeAltitude));
			final Node matsimNode = matsimNetworkFactory.createNode(Id.create(TransCadID, Node.class),coord);
			System.out.println("Node added: "+TransCadID);
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
		
		//----------------------------------
		
		
		
	} // end run()



	public static void main(String[] args) throws IOException, CsvException {
		// final String inputPath = "./ihop2/network-input/";
		final String inputPath = "C:/Users/ChengxiL/OneDrive – VTI, Statens väg-och transportforskningsinstitut/JavaChengxiLiu/src/test/resources/";
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
