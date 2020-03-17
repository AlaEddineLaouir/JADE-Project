package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jade.gui.GuiEvent;

public class AgentPanel extends JPanel {
	
	
	private JLabel labelType =new JLabel("Type de message :");
	private JTextField fieldType =new JTextField(12);
	private JLabel labelAgent =new JLabel("Nome Agent :");
	private JTextField fieldNomeAgent =new JTextField(12);
	private JLabel labelMessage =new JLabel("message :");
	private JTextField fieldMessage =new JTextField(12);
	private JButton btnEnvoye = new JButton("Envoyer");
	
	private JLabel labelLivre =new JLabel("Livre :");
	private JTextField fieldLivre =new JTextField(12);
	private JButton btnSearch = new JButton("Chercher");
	
	private JTextArea textAreaMessage =new JTextArea();
	
	
	public AgentPanel()
	{
		
/*btnEnvoye.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String typeMessage = fieldType.getText();
				String agent =fieldNomeAgent.getText();
				String message =fieldMessage.getText();
				
				GuiEvent event = new GuiEvent(this, 1);
				
				Map<String, Object> params =new HashMap<String, Object>();
				
				params.put("type",typeMessage);
				params.put("agent",	 agent);
				params.put("message",message);
				
				event.addParameter(params);
				clientAgent.postGuiEvent(event);
				
			}
		});*/
		
		
		/*btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String liver = fieldLivre.getText();
				
				GuiEvent event = new GuiEvent(this, 2);
				
				Map<String, Object> params =new HashMap<String, Object>();
				
				params.put("livre",liver);
				
				event.addParameter(params);
				clientAgent.postGuiEvent(event);
			}
		});*/
		
		
		JPanel searchPanel =new JPanel();
		searchPanel.setLayout(new FlowLayout());
		
		searchPanel.add(labelLivre);
		searchPanel.add(fieldLivre);
		searchPanel.add(btnSearch);
		
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		topPanel.add(labelType);
		topPanel.add(fieldType);
		topPanel.add(labelAgent);
		topPanel.add(fieldNomeAgent);
		topPanel.add(labelMessage);
		topPanel.add(fieldMessage);
		topPanel.add(btnEnvoye);
		
		JPanel head =new JPanel();
		head.setLayout(new BorderLayout());
		
		head.add(searchPanel,BorderLayout.NORTH);
		head.add(topPanel,BorderLayout.SOUTH);
		
		JPanel agentPanel =new JPanel();
		agentPanel.setLayout(new BorderLayout());
		
		
		agentPanel.add(head, BorderLayout.NORTH);
		agentPanel.add(new JScrollPane(textAreaMessage),BorderLayout.CENTER);

		
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
