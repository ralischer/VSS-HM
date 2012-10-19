package edu.hm.vss.prak3;

public class Main {

	public static final int PROUCER_COUNT = 10;
	public static final int CONSUMER_COUNT = 10;
	
	public static void main(String... args) {
		
		ProducerThread[] producers = new ProducerThread[PROUCER_COUNT];
		ConsumerThread[] consumers = new ConsumerThread[CONSUMER_COUNT];
		for(int i = 0; i < CONSUMER_COUNT; i++) {
			consumers[i] = new ConsumerThread();
			consumers[i].start();
		}
		for(int i = 0; i < PROUCER_COUNT; i++) {
			producers[i] = new ProducerThread();
			producers[i].start();
		}
	}
}
