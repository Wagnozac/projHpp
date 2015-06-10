package fr.tse.fi2.hpp.labs.projet;

import java.util.Comparator;

public class ComparateurDuoRouteOcc implements Comparator<DuoRouteOcc> {

	public ComparateurDuoRouteOcc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(DuoRouteOcc o1, DuoRouteOcc o2) {
		if (o1.getNbOcc() > o2.getNbOcc())
			return -1;
		else if (o1.getNbOcc() < o2.getNbOcc())
			return 1;
		else return 0;
	}

}
