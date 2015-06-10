package fr.tse.fi2.hpp.labs.projet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1v2 extends AbstractQueryProcessor {

	private List<DebsRecord> rec_=null;
	private List<DuoRouteOcc> route_;
	private List<Route> currentBest_;
	private boolean found;
	private boolean changement;
	private final DateFormat df;
	
	public Query1v2(QueryProcessorMeasure measure) {
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
		
		Route r = this.convertRecordToRoute2(record);
		
		
		
		// ***** La route est-elle dans la grille ? *****
		if(r.isValid(300))
		{
			rec_.add(record);
			
			// ***** Suppression des records superflus
			while (record.getDropoff_datetime() > rec_.get(0).getDropoff_datetime()+1800000)
			{
				rec_.remove(0);
			}
			
			// ***** Parcours liste de records *****
			for (int i=0; i<rec_.size();i++)
			{
				
//				// ***** Parcours de la liste de routes *****
//				for (int j=0;j<route_.size();j++)
//				{
//					// ****** Test si les deux routes sont les mêmes *****
//					if (route_.get(j).getRoute().equals(r))
//					{
//						route_.get(j).incrementRouteOcc();
//						found=true;
//						break;
//					}
//				}
//				// ***** Si la route n'existe pas déjà, on l'ajoute *****
//				if (!found)
//				{
//					route_.add(new DuoRouteOcc(r,1));						
//				}
//				else
//					found=false;	
			}

			
			// ***** On trie la liste de route en fonction du nombre d'occurences par ordre décroissant *****
			Collections.sort(route_, new ComparateurDuoRouteOcc());
			
			// ***** Gestion des cas ou on a plus de 10 routes *****
			if (route_.size()>10)
				route_.subList(10, route_.size()).clear();
			
			// ***** Vérification si changement des routes les plus fréquentes *****
			if (currentBest_.size() != route_.size())
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
				
				// ***** Actualisation des meilleurs routes *****
				if (currentBest_.size() <= route_.size())
				{
					for (int i=0; i < currentBest_.size(); i++)
						currentBest_.set(i, route_.get(i).getRoute());
					
					for (int i=currentBest_.size(); i < route_.size(); i++)
						currentBest_.add(route_.get(i).getRoute());
				}
				else
				{
					for (int i = 0; i < route_.size(); i++)
						currentBest_.set(i, route_.get(i).getRoute());
					currentBest_.subList(route_.size(), currentBest_.size()).clear();
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
