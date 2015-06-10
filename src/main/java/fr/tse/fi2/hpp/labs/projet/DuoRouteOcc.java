package fr.tse.fi2.hpp.labs.projet;

import fr.tse.fi2.hpp.labs.beans.Route;

public class DuoRouteOcc {

	private Route route_;
	private int nbOcc_;
	
	public DuoRouteOcc(Route r, int nb) {
		// TODO Auto-generated constructor stub
		this.route_=r;
		this.nbOcc_=nb;
	}
	
	public Route getRoute() {
		return route_;
	}
	public void setRoute(Route route_) {
		this.route_ = route_;
	}
	public int getNbOcc() {
		return nbOcc_;
	}
	public void setNbOcc(int nbOcc_) {
		this.nbOcc_ = nbOcc_;
	}


	public void incrementRouteOcc(){
		this.nbOcc_++;
	}

}
