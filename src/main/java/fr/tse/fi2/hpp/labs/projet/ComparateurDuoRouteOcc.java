package fr.tse.fi2.hpp.labs.projet;

import java.util.Comparator;

/**
 * Comparateur personnalisé pour la classe DuoRouteOcc, basant la comparaison sur le nombre
 * d'occurence uniquement
 * @author Arnaud P
 *
 */
public class ComparateurDuoRouteOcc implements Comparator<DuoRouteOcc> {

	public ComparateurDuoRouteOcc() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Méthode effectuant la comparaison. La comparaison est inversée afin de
	 * faire un tri décroissant.
	 */
	@Override
	public int compare(DuoRouteOcc o1, DuoRouteOcc o2) {
		if (o1.getNbOcc() > o2.getNbOcc())
			return -1;
		else if (o1.getNbOcc() < o2.getNbOcc())
			return 1;
		else return 0;
	}
	
	

}
