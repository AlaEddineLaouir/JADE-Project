package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import agents.Fournisseur;
import bean.Livre;

public class FournisseurGUI extends JFrame {
	
	
	private Fournisseur myFournisseur;
	
	private JLabel labelNom =new JLabel("Nom livre :");
	private JTextField fieldNom =new JTextField(12);
	private JLabel labelQt =new JLabel("Quantite livre :");
	private JTextField fieldQt =new JTextField(12);
	private JLabel labelPrix =new JLabel("prix :");
	private JTextField fieldPrix =new JTextField(12);
	private JButton btnEnvoye = new JButton("Modifier/Ajouter");
	
	
	
	private JTextArea textAreaMessage =new JTextArea();
	private JTextArea textAreaProduit =new JTextArea();
	
	public FournisseurGUI()
	{
		btnEnvoye.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					int qt =Integer.parseInt(fieldQt.getText());
					int prix= Integer.parseInt(fieldPrix.getText());
					String nom =fieldNom.getText();
					
					Livre livre =new Livre(nom, qt, prix);
					
					myFournisseur.ajouterModiferLivre(livre);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		
	}
	public void afficherLivre(ArrayList<Livre> listLivre)
	{
		textAreaProduit.setText("");

		String separateur="\n---------------------------------------------------------------------------------------\n";
		
		
		for(Livre livre : listLivre)
		{
			textAreaProduit.append("Nom Livre : "+livre.getNom()+" Qt : "+livre.getQuantity()+" Prix : "+livre.getPrice());
			textAreaProduit.append(separateur);
			
		}
	}
	
	public void showMessage(String Message ,Boolean neww)
	{
		
		String separateur="\n---------------------------------------------------------------------------------------\n";
		
		if(!neww)
		{
			textAreaMessage.append(separateur+Message+separateur);
		}else
		{
			textAreaMessage.setText(separateur+Message+separateur);
		}
	}

}
