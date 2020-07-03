package bicycleMatsim.demandgeneration;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DemandMatrixToPlan {
	private Random random = new Random();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[] probabilities = {0.32,1.0};
		double nextrandom = Math.random();
		int index = Arrays.binarySearch(probabilities, nextrandom);
		System.out.println("random number: "+nextrandom+" index:"+index);

	}

}
