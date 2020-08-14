package bicycleMatsim.analyzeoutputsTest;

import java.util.HashMap;
import java.util.Map.Entry;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;

import bicycleMatsim.analyzeoutputs.LinkVolumeHandler;

public class RunEventsHandlingTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//path to events file. For this you first need to run a simulation.
		final String inputPath = "C:/Users/ChengxiL/git/MatsimPlaygroundCLI/chengxi-playground/src/test/resources/";   
		final String inputFile = inputPath+"output/output_events.xml.gz";

		//create an event object
		EventsManager events = EventsUtils.createEventsManager();

		//create the handler and add it
		LinkVolumeHandler linkVolumeHandler1 = new LinkVolumeHandler();
		events.addHandler(linkVolumeHandler1);

		//create the reader and read the file
		MatsimEventsReader reader = new MatsimEventsReader(events);
		reader.readFile(inputFile);

		HashMap<Id<Link>, double[]> linkVolume = linkVolumeHandler1.getLinkVolume();
		int counter =1;
		for (Entry<Id<Link>, double[]> entry: linkVolume.entrySet()) {
			String savePath=inputPath+"output/linkVolumePNG/departuresPerHour_linkID_"+counter+".png";
			linkVolumeHandler1.writeChart(entry.getKey(),savePath);
			counter++;
		}



		System.out.println("Events file read!");

	}

}
