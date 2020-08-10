package bicycleMatsin.utilityTest;

import java.util.Arrays;
import java.util.List;


import bicycleMatsim.utility.MultinomialDistributionSampler;

public class MultinomialDistributionSamplerTest {

	public static void main(String[] args) {
		
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
		
		
		List<Double> weights = Arrays.asList(1.0,1.0,1.0,0.5,0.5,0.5,0.5,0.5,1.0,4.0,1.0,1.0,1.0,1.0);
		MultinomialDistributionSampler sampler = new MultinomialDistributionSampler(weights);
		Integer[] result=sampler.sampleWithoutReplacement(5);
		
		for (int i=0; i<result.length;i++) {
			System.out.println("sample without replacement is: "+ result[i]);
		}
		
//		double[] originalWeight = sampler.getCdfWeights();
//		for (int i=0; i<originalWeight.length;i++) {
//			System.out.println("weight is: " +originalWeight[i]);
//		}
		
		
		Integer[] resultWithReplacement=sampler.sampleWithReplacement(8);
		for (int i=0; i<resultWithReplacement.length;i++) {
			System.out.println("sample with replacement is: "+ resultWithReplacement[i]);
		}
		
	}

}
