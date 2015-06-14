package fr.tse.fi2.hpp.labs.projet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1 extends AbstractQueryProcessor {

	private List<DebsRecord> rec_=null;
	private List<TripleRouteDropOcc> route_;
	private List<Route> currentBest_;
	private boolean found;
	private boolean changement;
	private DateFormat df;
	
	
	public Query1(QueryProcessorMeasure measure) {
		super(measure);
		rec_= new ArrayList<>();
		route_= new ArrayList<>();
		found = false;
		currentBest_ = new ArrayList<>();
		changement = false;
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	protected void process(DebsRecord record) {
		// ***** Début mesure du delay *****
		long delayStart = System.nanoTime();
		
		
		rec_.add(record);
		List<Integer> temp = new ArrayList<Integer>();
		Route r = this.convertRecordToRoute2(record);
		
		// ***** La route est-elle dans la grille ? *****
		if(!r.isValid(300))
		{
			rec_.remove(rec_.size()-1);
		}
		else
		{
			// ***** Parcours liste de records *****
			for (int i=0; i<rec_.size();i++)
			{
				// ***** Test si on est dans la bonne fenêtre temporelle *****
				if (record.getDropoff_datetime() <= rec_.get(i).getDropoff_datetime()+1800000)
				{				
					// ***** Parcours de la liste de routes *****
					for (int j=0;j<route_.size();j++)
					{
						// ****** Test si les deux routes sont les mêmes *****
						if (route_.get(j).getRoute().equals(r))
						{
							route_.get(j).incrementRouteOcc();
							found=true;
						}
					}
					// ***** Si la route n'existe pas déjà, on l'ajoute *****
					if (!found)
					{
						route_.add(new TripleRouteDropOcc(r,1,1));						
					}
					else
						found=false;					
				}
				else
				{
					// ***** Stockage des indices des records superflus *****
					temp.add(i);
				}
	
			}
			// ***** Suppression des records superflus *****
			for (int i=temp.size()-1;i>=0;i--)
			{
				rec_.remove(temp.get(i));
			}
			
			// ***** On trie la liste de route en fonction du nombre d'occurences par ordre décroissant *****
			Collections.sort(route_, new Comparator<TripleRouteDropOcc>()
					{
						public int compare(TripleRouteDropOcc o1, TripleRouteDropOcc o2) 
						{
							if (o1.getNbOcc() > o2.getNbOcc())
								return -1;
							else if (o1.getNbOcc() < o2.getNbOcc())
								return 1;
							else return 0;
						}			
					});
			
			// ***** Vérification si changement des routes les plus fréquentes *****
			if (currentBest_.size()< route_.size())
				changement = true;
			else
			{
				for (int i=0; i < currentBest_.size(); i++)
				{
					if (!currentBest_.get(i).equals(route_.get(i).getRoute()))
					{
						changement = true;
						break;
					}
				}
			}
			
			// ****** Traitement en cas de changement *****
			if (changement)
			{
				// ***** Gestion cas moins de 10 routes *****
				String finLigne="";
				if (route_.size()<10)
				{
					for (int i = route_.size();i<10;i++)
						finLigne=finLigne+",NULL";
				}
				// ***** Gestion cas plus de 10 routes *****
				else if(route_.size()>10)
				{
					route_.subList(10, route_.size()).clear();
				}
				
				// ***** Actualisation des meilleurs routes *****
				for (int i=0; i < currentBest_.size(); i++)
				{
					currentBest_.set(i, route_.get(i).getRoute());
				}
				for (int i=currentBest_.size(); i < route_.size(); i++)
				{
					currentBest_.add(route_.get(i).getRoute());
				}
				// ***** Réinitialisation du booléen *****
				changement = false;
				
				// ***** Formatage de la date pour écriture dans le fichier *****
				Date pickup = new Date(record.getPickup_datetime());
				Date dropoff = new Date(record.getDropoff_datetime());				
				
				// ***** Début de la ligne *****
				String line = ""+df.format(pickup).toString()+","+ df.format(dropoff).toString();
				
				// ***** Remplissage des données *****
				for (int i=0;i<route_.size();i++)
				{
					line = line+","+route_.get(i).getRoute().getPickup().getX()+"."+route_.get(i).getRoute().getPickup().getY()+","
							+ route_.get(i).getRoute().getDropoff().getX() + "."+ route_.get(i).getRoute().getDropoff().getY();
				}
				
				// ***** Ajout de la fin de la ligne en cas de taille <10 *****
				line = line+finLigne;
				
				long delay = System.nanoTime() - delayStart;
				// ***** Ecriture grâce au thread *****
				writeQuery1(line + "," + delay);
			}
		}

	}

}
