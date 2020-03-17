package bean;

import jade.core.AID;

public class Offer {

	
	private AID fournisseur;
	private int offer;
	private String livre;
	
	public AID getFournisseur() {
		return fournisseur;
	}
	public void setFournisseur(AID fournisseur) {
		this.fournisseur = fournisseur;
	}
	public String getLivre() {
		return livre;
	}
	public void setLivre(String livre) {
		this.livre = livre;
	}
	public int getOffer() {
		return offer;
	}
	public void setOffer(int offer) {
		this.offer = offer;
	}
	
	
}
