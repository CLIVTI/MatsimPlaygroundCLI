package bicycleMatsin.utilityTest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import bicycleMatsim.utility.MultinomialDistributionSampler;

public class MultinomialDistributionSamplerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		double[] test = {0.32, 0.68};
//		double[] test2 = Arrays.copyOfRange(test, 0, test.length);
//		test2[1]=2;
//		System.out.println(test.length);
//		System.out.println(test[0]+" , "+test[1]);
//		System.out.println(test2[0]+" , "+test2[1]);
//
//		Random random=new Random();
//		double[] test3 = {0.32, 0.32, 0.32};
//		double nextRandom=random.nextDouble();
//		int index = Arrays.binarySearch(test3, nextRandom);
//		System.out.println("next random is: "+ nextRandom);
//		System.out.println("index is: "+index);
		
		
		List<Double> weights = Arrays.asList(10.0,4.5,0.8,1.7,9.1);
		MultinomialDistributionSampler sampler = new MultinomialDistributionSampler(weights);
		Integer[] result=sampler.sampleWitoutReplacement(8);
		
		for (int i=0; i<result.length;i++) {
			System.out.println(result[i]);
		}
	}

}
