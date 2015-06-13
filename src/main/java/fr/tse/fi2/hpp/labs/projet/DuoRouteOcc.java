package fr.tse.fi2.hpp.labs.projet;

import fr.tse.fi2.hpp.labs.beans.Route;

/**
 * Classe regroupant une route et le nombre de fois qu'elle apparait.
 * @author Arnaud P
 *
 */
public class DuoRouteOcc {

	private Route route_;
	private int nbOcc_;
	
	/**
	 * Constructeur de la classe
	 * @param r Une route
	 * @param nb Le nombre d'occurence de cette route
	 */
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

	/**
	 * Méthode pour incrementer le nombre d'occurence de la route
	 */
	public void incrementRouteOcc(){
		this.nbOcc_++;
	}
	
	public void decrementRouteOcc()
	{
		this.nbOcc_--;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((route_ == null) ? 0 : route_.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DuoRouteOcc other = (DuoRouteOcc) obj;
		if (route_ == null) {
			if (other.route_ != null)
				return false;
		} else if (!route_.equals(other.route_))
			return false;
		return true;
	}
}
