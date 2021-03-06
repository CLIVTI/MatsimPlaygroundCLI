package bicycleMatsin.utilityTest;

import java.util.PriorityQueue;

public class DummyTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DummyTest test = new DummyTest();
		test.testFunction();
	}
	
	public void testFunction(){
        // Creating empty priority queue 
        PriorityQueue<Integer> pQueue = new PriorityQueue<Integer>(); 
  
        // Adding items to the pQueue using add() 
        pQueue.add(10); 
        pQueue.add(20); 
        pQueue.add(15); 
  
        // Printing the top element of PriorityQueue 
        System.out.println(pQueue.poll()); 
  
        // Printing the top element and removing it 
        // from the PriorityQueue container 
        System.out.println(pQueue.poll()); 
  
	}

}
