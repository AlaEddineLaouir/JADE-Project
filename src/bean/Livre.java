package bean;

public class Livre {

	private String nom;
	private int quantity;
	private int price;
	
	
	
	
	
	
	public Livre(String nom, int quantity, int price) {
		super();
		this.nom = nom;
		this.quantity = quantity;
		this.price = price;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
