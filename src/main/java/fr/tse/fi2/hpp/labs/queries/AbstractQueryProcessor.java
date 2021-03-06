package fr.tse.fi2.hpp.labs.queries;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.projet.ThreadWriter1;
import fr.tse.fi2.hpp.labs.projet.ThreadWriter2;
import fr.tse.fi2.hpp.labs.queries.impl.Lab3.ThreadWriter;

/**
 * Every query must extend this class that provides basic functionalities such
 * as :
 * <ul>
 * <li>Receives event from {@link StreamingDispatcher}</li>
 * <li>Notify start/end time</li>
 * <li>Manages thread synchronization</li>
 * <li>Grid mapping: maps lat/long to x,y in a discrete grid of given size</li>
 * </ul>
 * 
 * @author Julien
 * 
 */
public abstract class AbstractQueryProcessor implements Runnable {

	public final LinkedBlockingQueue<String> sumList;
	
	final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);

	/**
	 * Counter to uniquely identify the query processors
	 */
	private final static AtomicInteger COUNTER = new AtomicInteger();
	/**
	 * Unique ID of the query processor
	 */
	private final int id = COUNTER.incrementAndGet();
	/**
	 * Internal queue of events
	 */
	public final BlockingQueue<DebsRecord> eventqueue;
	
		
	
	public ThreadWriter Thread1;
	public Thread Thread2;
	
	public final LinkedBlockingQueue<String> query1;
	public ThreadWriter1 tw1;
	public Thread threadQ1;
	
	public final LinkedBlockingQueue<String> query2;
	public ThreadWriter2 tw2;
	public Thread threadQ2;
	
	/**
	 * Global measurement
	 */
	
	
	
	private final QueryProcessorMeasure measure;
	/**
	 * For synchronisation purpose
	 */
	private CountDownLatch latch;

	

	/**
	 * Default constructor. Initialize event queue and writer
	 */
	public AbstractQueryProcessor(QueryProcessorMeasure measure) {
		// Set the global measurement instance
		this.measure = measure;
		// Initialize queue
		this.eventqueue = new LinkedBlockingQueue<>();
		this.sumList=new LinkedBlockingQueue<String>();
		
		Thread1=new ThreadWriter(sumList,id);
		Thread2=new Thread(Thread1);
		Thread2.start();
		
		this.query1=new LinkedBlockingQueue<String>();
		tw1= new ThreadWriter1(query1);
		threadQ1 = new Thread(tw1);
		threadQ1.start();
		
		this.query2=new LinkedBlockingQueue<String>();
		tw2= new ThreadWriter2(query2);
		threadQ2 = new Thread(tw2);
		threadQ2.start();
		
		
		// Initialize writer

	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		logger.info("Starting query processor " + id);
		// Notify beginning of processing
		measure.notifyStart(this.id);
		while (true) {
			try {
				
				DebsRecord record = eventqueue.take();
				if (record.isPoisonPill()) {
					break;
				} else {
					process(record);
				}
			} catch (InterruptedException e) {
				logger.error(
						"Error taking element from internal queue, processor "
								+ id, e);
				break;
			}
		}
		// Finish, close the writer and notify the measurement
		finish();
		logger.info("Closing query processor " + id);
	}

	/**
	 * 
	 * @param record
	 *            record to be processed
	 */
	protected abstract void process(DebsRecord record);

	/**
	 * 
	 * @param record
	 *            the record to process
	 * @return the route in a 600*600 grid
	 */
	protected Route convertRecordToRoute(DebsRecord record) {
		// Convert pickup coordinates into cell
		float lat1 = record.getPickup_latitude();
		float long1 = record.getPickup_longitude();
		GridPoint pickup = convert(lat1, long1);
		// Convert dropoff coordinates into cell
		float lat2 = record.getDropoff_latitude();
		float long2 = record.getDropoff_longitude();
		GridPoint dropoff = convert(lat2, long2);
		return new Route(pickup, dropoff);
	}
	
	protected Route convertRecordToRoute2(DebsRecord record) {
		// Convert pickup coordinates into cell
		float lat1 = record.getPickup_latitude();
		float long1 = record.getPickup_longitude();
		GridPoint pickup = convert2(lat1, long1);
		// Convert dropoff coordinates into cell
		float lat2 = record.getDropoff_latitude();
		float long2 = record.getDropoff_longitude();
		GridPoint dropoff = convert2(lat2, long2);
		return new Route(pickup, dropoff);
	}

	/**
	 * 
	 * @param lat1
	 * @param long1
	 * @return The lat/long converted into grid coordinates
	 */
	protected static GridPoint convert(float lat1, float long1) {
		return new GridPoint(cellX(long1), cellY(lat1));
	}
	
	private GridPoint convert2(float lat1, float long1) {
		return new GridPoint(cellX2(long1), cellY2(lat1));
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param x
	 * @return
	 */
	private static int cellX(float x) {

		// double x=0;
		double x_0 = -74.913585;
		double delta_x = 0.005986 / 2;

		// double cell_x;
		Double cell_x = 1 + Math.floor(((x - x_0) / delta_x) + 0.5);

		return cell_x.intValue();
	}
	
	private int cellX2(float x) {

		// double x=0;
		double x_0 = -74.913585;
		double delta_x = 0.005986;

		// double cell_x;
		Double cell_x = 1 + Math.floor(((x - x_0) / delta_x) + 0.5);

		return cell_x.intValue();
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param y
	 * @return
	 */
	private static int cellY(double y) {

		double y_0 = 41.474937;
		double delta_y = 0.004491556 / 2;

		Double cell_y = 1 + Math.floor(((y_0 - y) / delta_y) + 0.5);

		return cell_y.intValue();

	}
	
	private int cellY2(double y) {

		double y_0 = 41.474937;
		double delta_y = 0.004491556;

		Double cell_y = 1 + Math.floor(((y_0 - y) / delta_y) + 0.5);

		return cell_y.intValue();

	}


	/**
	 * @return the id of the query processor
	 */
	public final int getId() {
		return id;
	}

	/**
	 * 
	 * @param line
	 *            the line to write as an answer
	 */
	protected void writeLine(String line) {
			
		sumList.add(line);
		

	}

	protected void writeQuery1(String line){
		query1.add(line);
	}
	
	protected void writeQuery2(String line){
		query2.add(line);
	}
	
	protected void finish(){
		measure.notifyFinish(this.id);
		latch.countDown();
		Thread1.poison=true;
		tw1.add("poison");
		tw2.add("poison");
	}

}
