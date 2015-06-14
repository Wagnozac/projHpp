package fr.tse.fi2.hpp.labs.projet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.main.MainNonStreaming;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;



@State(Scope.Thread)
public class BenchmarkQuery1 {

	private final static Logger logger = LoggerFactory
			.getLogger(MainNonStreaming.class);
	private Query1v2 q;
	private StreamingDispatcher dispatch;

	
	
	@Setup
	public void prepare(){
		
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher
		dispatch = new StreamingDispatcher("src/main/resources/data/test_01.csv");

		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		// Add you query processor here

		q = new Query1v2(measure);
		processors.add(q);
		
		dispatch.registerQueryProcessor(q);		
	}
	
	
	@Benchmark
    @Fork(value = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
	public boolean testMethod(){
		
		CountDownLatch latch = new CountDownLatch(1);
		q.setLatch(latch);
		Thread t = new Thread(q);
		t.setName("QP" + q.getId());
		t.start();
		Thread t1 = new Thread(dispatch);
		t1.setName("Dispatcher");
		t1.start();
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
		
		return true;		
	}
	

	
}

