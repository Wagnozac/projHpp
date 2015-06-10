package fr.tse.fi2.hpp.labs.projet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadWriter1 implements Runnable {
	
	BufferedWriter outputWriter;
	LinkedBlockingQueue<String> q;
	//public String poison;
	
	public ThreadWriter1(LinkedBlockingQueue<String> q) {
		super();
		this.q=q;
		//this.poison="";
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/projetQuery1.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("file not created");
		}
	}

	@Override
	public void run() {
		
//		while (!poison.equals("poison")){
//			try {	
//				String line =q.take();
//				writeLine(line);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				
//			}
//		}
		while(true)
		{
			try {
				String line = q.take();
				if (line.equals("poison"))
					break;
				else
					writeLine(line);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		finish();
	}
	
	
	public void writeLine(String line){
		try {			
			outputWriter.write(line);
			outputWriter.newLine();
			
		} catch (IOException e) {}		
	}
	
	
	public void finish() {
		// Close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {}

	}
	
	public void add(String s)
	{
		q.add(s);
	}

}
