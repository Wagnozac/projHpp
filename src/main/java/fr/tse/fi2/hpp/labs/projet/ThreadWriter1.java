package fr.tse.fi2.hpp.labs.projet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * Classe permettant d'écrire dans dans un thread séparé le résultat de la Query 1
 * @author Arnaud P
 *
 */
public class ThreadWriter1 implements Runnable {
	
	BufferedWriter outputWriter;
	LinkedBlockingQueue<String> q;
	
	/**
	 * Constructeur de la classe.
	 * @param q file d'attente pour l'écriture
	 */
	public ThreadWriter1(LinkedBlockingQueue<String> q) {
		super();
		this.q=q;
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/projetQuery1.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("file not created");
		}
	}

	/**
	 * Méthode utilisée lorsque le thread est lancé
	 */
	@Override
	public void run() {
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
	
	/**
	 * Méthode écrivant une ligne dans le fichier
	 * @param line ligne à écrire dans le fichier
	 */
	public void writeLine(String line){
		try {			
			outputWriter.write(line);
			outputWriter.newLine();
			
		} catch (IOException e) {}		
	}
	
	/**
	 * Méthode appellée à la fermeture du thread
	 */
	public void finish() {
		// Close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {}

	}
	
	/**
	 * Méthode pour ajouter une ligne à la file d'attente
	 * @param s ligne à ajouter à la file d'attente
	 */
	public void add(String s)
	{
		q.add(s);
	}

}
