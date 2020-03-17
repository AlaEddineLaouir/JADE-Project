package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.Livre;
import bean.Offer;
import gui.FournisseurGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Fournisseur extends Agent {

	
	private ArrayList<Livre> livres;
	private Agent myAgent =null;
	private FournisseurGUI myGui=null;
	
	private final static int MARGE_NEGOCIATION=100;
	
	private Map<AID, Offer> clientLastProposal ;
	
	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		super.takeDown();
		
		try
		{
		DFService.deregister(this);
		}
		catch (FIPAException fe) {
		fe.printStackTrace();
		}
		
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		myAgent=this;
		
		clientLastProposal =new HashMap<AID, Offer>();
		
		livres =new ArrayList<Livre>();
		
		Object[] args =getArguments();
		
		for(int i = 0 ;i< args.length ;i++)
		{
			livres.add((Livre)args[i]);
			
			publierService(((Livre)args[i]).getNom());
			
		}
		
		
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				
				MessageTemplate mt =MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), 
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
								MessageTemplate.MatchPerformative(ACLMessage.CFP)));
				ACLMessage msg = myAgent.receive(mt);
				if (msg != null) {
					switch (msg.getPerformative()) {
					case ACLMessage.PROPOSE:
							handlePropose(msg);
						break;
					case ACLMessage.CFP:
							handleCFP(msg);
						break;
					case ACLMessage.ACCEPT_PROPOSAL :
							handleACCEPTPROPOSAL(msg);
						break;

					default:
						break;
					}

				}
				else
				{
					block();
				}
				
			}

			private void handleACCEPTPROPOSAL(ACLMessage msg) {
				// TODO Auto-generated method stub
				
				String livreAvendre = msg.getContent();
				System.out.println("Livre vendus");
				ACLMessage reply= msg.createReply();
				reply.setPerformative(ACLMessage.CONFIRM);
				
				for(Livre livre : livres)
				{
					if(livre.getNom().equals(livreAvendre) && livre.getQuantity() > 0 )
						reply.setPerformative(ACLMessage.CONFIRM);
				}
				
				myAgent.send(reply);
				
				
			}

			private void handleCFP(ACLMessage msg) {
				// TODO Auto-generated method stub
				String nomLivre =msg.getContent();
				System.out.println("CFP livre "+nomLivre);
				Livre liverRechercher = null ;
				int prix=0;
				for (Livre livre : livres) {
					if(livre.getNom().equals(nomLivre))
					{
						liverRechercher=livre;
						break;
					}
				}
				
				
				ACLMessage reply= msg.createReply();
				if(liverRechercher != null  &&   liverRechercher.getQuantity() > 0 )
				{
					prix=liverRechercher.getPrice();
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(prix+"");
					System.out.println("PROPOSAL to client "+prix);
				}else {
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent("Non Disponible");
					System.out.println("404 client");
				}
				
				myAgent.send(reply);

				
			}

			private void handlePropose(ACLMessage msg) {
				// TODO Auto-generated method stub
				String proposale =msg.getContent();
				
				String[] livreAbout= proposale.split(" ");
				
				String nomLivre =livreAbout[0];
				int proposedPrice = Integer.parseInt(livreAbout[1]);
				
				System.out.println("Client Propose "+nomLivre+" prix "+proposedPrice);
				int prix=0;
				
				int indexLivre =0;
				
				for(indexLivre=0;indexLivre < livres.size()-1;indexLivre++ )
				{
					Livre livre = livres.get(indexLivre);
					if(livre.getNom().equals(nomLivre) && livre.getQuantity() > 0)
					{
						prix=livre.getPrice();
						break;
					}
				}
				
				
				
				ACLMessage reply= msg.createReply();
				
				AID sender = msg.getSender();
				
				int lastProposed = 0;
				if(clientLastProposal.containsKey(sender))
					lastProposed=clientLastProposal.get(sender).getOffer();
				
				if(prix>0)
				{
					if(prix <= proposedPrice)
					{
						Livre l= livres.get(indexLivre);
						l.setQuantity(l.getQuantity()-1);
						livres.add(l);
						
						reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
						
					}else {
						if(prix-MARGE_NEGOCIATION > proposedPrice)
						{
							clientLastProposal.remove(sender);
							
							reply.setPerformative(ACLMessage.REFUSE);
						}else
						{
							if(lastProposed == 0)
							{
								int difference = (int)(prix-proposedPrice)/2 ;
								int proposal=prix-difference;
								
								Offer offre =new Offer();
								offre.setOffer(lastProposed);
								offre.setLivre(nomLivre);
								clientLastProposal.put(sender, offre);
								
								reply.setPerformative(ACLMessage.PROPOSE);
								reply.setContent(proposal+"");
								
							}else
							{
								if(lastProposed > proposedPrice)
								{
									
									clientLastProposal.remove(sender);
									
									reply.setPerformative(ACLMessage.REFUSE);
								}else
								{
								
									Livre l= livres.get(indexLivre);
									l.setQuantity(l.getQuantity()-1);
									livres.add(l);
									
									clientLastProposal.remove(sender);

									reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
								}
							}
						}
						
					}
					
					
				}else {
					reply.setContent("Non Disponible");
				}
				
				myAgent.send(reply);
				
			}
		
		
		});
		
		
		
		
	}
	
	private void publierService(String nomLivre)
	{
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName(nomLivre);
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public void ajouterModiferLivre(Livre livre) {
		boolean existeNot =true;
		
		for(Livre l :livres)
		{
			if(livre.getNom().equals(l.getNom()))
			{
				existeNot =false;
				
				l.setPrice(livre.getPrice());
				l.setQuantity(livre.getQuantity());
				break;
			}
		}
		if(existeNot)
		{
			livres.add(livre);
		}
		
		myGui.afficherLivre(livres);
		
	}
	

}
