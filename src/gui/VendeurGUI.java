package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import agents.Vendeur;
import jade.gui.GuiEvent;

public class VendeurGUI extends JFrame {
	
	
	private JLabel labelTaux =new JLabel("Taux de benefice actuele");
	private JTextField fieldTaux =new JTextField(12);
	private JButton btnEnvoye = new JButton("Modifier");
	
	private JTextArea textAreaMessage =new JTextArea();
	
	private Vendeur myVendeur;
	
	
	public Vendeur getMyVendeur() {
		return myVendeur;
	}

	public void setMyVendeur(Vendeur myVendeur) {
		this.myVendeur = myVendeur;
	}

	public VendeurGUI()
	{
		btnEnvoye.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String newTauxInput= fieldTaux.getText();
				try {
					int newTaux =Integer.parseInt(newTauxInput);
					
					GuiEvent event =new GuiEvent(this , 1);
					
					event.addParameter(newTaux);
					
					myVendeur.postGuiEvent(event);
					
					modifierTaux(newTaux);
					
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		
		
		
		JPanel topPanel =new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		topPanel.add(labelTaux);
		topPanel.add(fieldTaux);
		topPanel.add(btnEnvoye);
		
		JPanel agentPanel =new JPanel();
		agentPanel.setLayout(new BorderLayout());
		
		agentPanel.add(topPanel, BorderLayout.NORTH);
		agentPanel.add(new JScrollPane(textAreaMessage),BorderLayout.CENTER);
		
this.add(agentPanel);
		
		this.setSize(850,600);
		this.setVisible(true);
		
	}
	
	public void modifierTaux (int taux)
	{
		labelTaux.setText("Taux de benefice actuele "+taux);
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
