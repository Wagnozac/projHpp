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
	private List<DuoRouteOcc> currentBest_;
	private final DateFormat df;
	
	public Query1v2(QueryProcessorMeasure measure) {
		super(measure);
		rec_= new ArrayList<DebsRecord>();
		route_= new ArrayList<DuoRouteOcc>();
		currentBest_ = new ArrayList<DuoRouteOcc>();
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	protected void process(DebsRecord record) {
		
		// ***** Début mesure du delay *****
		long delayStart = System.nanoTime();
		
		Route r = this.convertRecordToRoute2(record);
		DuoRouteOcc currentDuo = new DuoRouteOcc(r,1);
		
		
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
				// ***** Test si la route est présente et à quel indice *****
				int ind = route_.indexOf(currentDuo);
				if(ind != -1)
				{
					// ***** Si route déjà présente on incrémente son nombre d'occurence *****
					route_.get(ind).incrementRouteOcc();
				}
				else
				{
					// ***** Sinon on l'ajoute à la liste *****
					route_.add(currentDuo);
				}	
			}

			
			// ***** On trie la liste de route en fonction du nombre d'occurences par ordre décroissant *****
			Collections.sort(route_, new ComparateurDuoRouteOcc());
			
			// ***** Gestion des cas ou on a plus de 10 routes *****
			if (route_.size()>10)
				route_.subList(10, route_.size()).clear();
			
			
			// ***** Vérification si changement des routes les plus fréquentées *****
			if (!currentBest_.equals(route_))
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
						currentBest_.set(i, route_.get(i));
					
					for (int i=currentBest_.size(); i < route_.size(); i++)
						currentBest_.add(route_.get(i));
				}
				else
				{
					for (int i = 0; i < route_.size(); i++)
						currentBest_.set(i, route_.get(i));
					currentBest_.subList(route_.size(), currentBest_.size()).clear();
				}
			
					
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
				
				// ***** Calcul du temps de traitement *****
				long delay = System.nanoTime() - delayStart;
				
				// ***** Ecriture grâce au thread *****
				writeQuery1(line + "," + delay);
			}
		}

	}

}
