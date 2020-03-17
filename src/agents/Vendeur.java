package agents;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.Livre;
import bean.Offer;
import gui.VendeurGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Vendeur extends GuiAgent{

	private Map<AID, AID> clientFournisseur ;
	private Map<AID, AID> fournisseurClient ;
	
	private ArrayList< Offer> offerDesFournisseur;
	private ArrayList<AID> listFournisseur;
	private Offer bestOffer =null;
	private int count=0;
	private Agent myAgent;
	private int benefice =17;
	private AID client;
	private String livreDemander;
	private VendeurGUI myGui;
	
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		
		myAgent=this;
		publierService();
		listFournisseur=new ArrayList<AID>();
		
		VendeurGUI vendeurGui= new VendeurGUI();
		vendeurGui.setMyVendeur(this);
		vendeurGui.showMessage("Agent a demmarer", true);
		myGui =vendeurGui;
		
		addBehaviour(new TickerBehaviour(this ,6000) {
		
			private boolean update =false;
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("book-selling"); template.addServices(sd);
				try
				{
				DFAgentDescription[] result = DFService.search(myAgent, template);
				myGui.showMessage("Nomber fournisseur trouver :"+result.length+" \n", false);
				for(int i = 0; i < result.length; i++) {
					if( ! listFournisseur.contains(result[i].getName()))
						listFournisseur.add(result[i].getName());
				
				}
				}
				catch (Exception fe) { fe.printStackTrace(); }
				
				if(listFournisseur.size()==0)
					supprimerService();
				
					
				
			}
		} );
		
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				try {
					
					MessageTemplate template =MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
							MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL), 
											   MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CFP),
											   MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REFUSE),
											   MessageTemplate.MatchPerformative(ACLMessage.INFORM))))
							);
					
					ACLMessage msg =receive(template);
					if(msg!=null)
					{
						switch(msg.getPerformative())
						{
						
						case ACLMessage.CFP :
								handleCFP(msg);
							break;
						case ACLMessage.ACCEPT_PROPOSAL :
							handleACCEPT_PROPOSAL(msg);
							break;
						case ACLMessage.REFUSE :
							handleREFUSE(msg);
							break;
						case ACLMessage.INFORM :
							handleINFORM(msg);
							break;
						case ACLMessage.PROPOSE :
							
							if(listFournisseur.contains(msg.getSender()))
								handlePROPOSEFournisseur(msg);
							else
								handlePROPOSEClient(msg);
							
							break;
						case ACLMessage.DISCONFIRM :
								handleDISCONFIRME();
							break;
						case ACLMessage.CONFIRM :
								handleCONFIRME();
							break;
							
						
							default :
								break;
						}
					}else
					{
						block();
					}
					
					
				}catch(Exception e)
				{
					
				}
			}
			
			private void handleCONFIRME() {
				// TODO Auto-generated method stub
				ACLMessage msg =new ACLMessage(ACLMessage.CONFIRM);
				msg.setSender(client);
				System.out.println("Votre livre seras livrer le plus ot possible");
				msg.setContent("Votre livre seras livrer le plus ot possible");
				myGui.showMessage("Livre vendus au client", false);
				Vendeur.this.myAgent.send(msg);
			}

			private void handleDISCONFIRME() {
				// TODO Auto-generated method stub
				ACLMessage msg =new ACLMessage(ACLMessage.DISCONFIRM);
				msg.setSender(client);
				msg.setContent("Desole, le livre n'est plus disponible");
				myGui.showMessage("Livre n'est plus disponible pour client", false);
				Vendeur.this.myAgent.send(msg);
				
			}

			private void handleINFORM(ACLMessage msg) {
				// TODO Auto-generated method stub
				
				System.out.println("Fournissuer Inform");
				Offer offer =new Offer();
				offer.setFournisseur(msg.getSender());
				offer.setLivre(livreDemander);
				offer.setOffer(0);
				
				offerDesFournisseur.add(offer);
				count++;
				
				myGui.showMessage("Un fournisseur propose un offre", false);

				
				handlePROPOSEFournisseur(msg);
				
			
			}

			private void handlePROPOSEClient(ACLMessage msg) {
				// TODO Auto-generated method stub
				
				
				System.out.println("Client Propose");
				
				myGui.showMessage("Le client propose", false);
				
				ACLMessage forward = new ACLMessage(ACLMessage.PROPOSE);
				forward.addReceiver(bestOffer.getFournisseur());
				
				int proposal =Integer.parseInt(msg.getContent());
				proposal =(int)(proposal - (proposal*benefice)/100); //Marge Benefice
				
				forward.setContent(bestOffer.getLivre()+" "+proposal);
				
				myAgent.send(forward);
				
			}

			private void handlePROPOSEFournisseur(ACLMessage msg) {
				// TODO Auto-generated method stub
				
				System.out.println("Fournisseur Propose");
				
				myGui.showMessage("Un fournisseur propose un offre", false);
				
				if(bestOffer == null)
				{
					count++;
					System.out.println("count : "+count+" listFourSize : "+listFournisseur.size()+" bestOffer :"+bestOffer);
					System.out.println("Offer  : "+msg.getContent());
					Offer offer =new Offer();
					offer.setFournisseur(msg.getSender());
					offer.setLivre(livreDemander);
					offer.setOffer(Integer.parseInt(msg.getContent()));
					
					offerDesFournisseur.add(offer);
					if(count == listFournisseur.size())
					{
						bestOffer =offerDesFournisseur.get(0);
						for(Offer o :offerDesFournisseur)
						{
							if(o.getOffer()>0)
							{
								if(o.getOffer()< bestOffer.getOffer())
									bestOffer=o;
							}
						}
						
						ACLMessage forward = new ACLMessage(ACLMessage.PROPOSE);
						forward.addReceiver(client);
						
						int proposal =bestOffer.getOffer();
						proposal =(int)(proposal + (proposal*benefice)/100); //MArge Benefice
						
						forward.setContent(bestOffer.getLivre()+" "+proposal);
						
						myAgent.send(forward);
						
					}
					
				}else
				{
					ACLMessage forward = new ACLMessage(ACLMessage.PROPOSE);
					forward.addReceiver(client);
					
					int proposal =Integer.parseInt(msg.getContent());
					proposal =(int)(proposal + (proposal*benefice)/100); //MArge Benefice
					
					bestOffer.setOffer(Integer.parseInt(msg.getContent()));
					
					forward.setContent(bestOffer.getLivre()+" "+proposal);
					
					myAgent.send(forward);
				}
				
			}

			private void handleREFUSE(ACLMessage msg) {
				// TODO Auto-generated method stub
				System.out.println("Fournisseur refuse");
				ACLMessage forward =new ACLMessage(ACLMessage.REFUSE);
				forward.addReceiver(client);
				
				myGui.showMessage("Un fournisseur a refuse l'offre", false);
				
				count=0;
				bestOffer=null;
				
				myAgent.send(forward);
				
			}

			private void handleACCEPT_PROPOSAL(ACLMessage msg) {
				// TODO Auto-generated method stub
				
				ACLMessage forward =new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				
				myGui.showMessage("accepte l'offre", false);
				if(msg.getSender().equals(client))
				{
					forward.setContent(bestOffer.getLivre());
					forward.addReceiver(bestOffer.getFournisseur());
					myGui.showMessage("Le client accepte l'offre", false);
				}else
				{
					int propsal=(int)(bestOffer.getOffer() + (bestOffer.getOffer()*benefice)/100);
					
					forward.setContent(bestOffer.getLivre()+" "+propsal);
					forward.addReceiver(client);
					myGui.showMessage("Un fournisseur accepte l'offre", false);
				}
				
				myAgent.send(forward);
				
			}

			private void handleCFP(ACLMessage msg)
			{
				
				client=msg.getSender();
				livreDemander=msg.getContent();
				
				count =0;
				bestOffer=null;
				
				System.out.println("Client CFP "+livreDemander);
				
				myGui.showMessage("Le client fait un appelle d'offre", false);
				
				offerDesFournisseur =new ArrayList<Offer>();
				
				for(AID fournisseur :listFournisseur)
				{
					ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
					cfp.addReceiver(fournisseur);
					cfp.setContent(livreDemander);
					
					myAgent.send(cfp);
				}
			}
		}
		);
		
		
		/*
		 * Cyclic behaivor to listen to CFP PROPOSAL(CLIENT , FOURNISSEUR) ACCEPT-PORPOSAL (Client , fournisseur)
		 * to choose the best proposel ,must add count to make sure all offers are received
		 * then save fournissuer and his offer and continue between them the offer 
		 * */
		
		
	}
	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		supprimerService();
		super.takeDown();
	}
	
	private void publierService()
	{
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("vendeur-livre");
		sd.setName("vendeur-livre-"+myAgent.getName());
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			
		}
	}
	private void supprimerService()
	{
		try
		{
		DFService.deregister(this);
		}
		catch (FIPAException fe) {
		
		}
	}
	@Override
	protected void onGuiEvent(GuiEvent event) {
		// TODO Auto-generated method stub
		
		int taux =(int)event.getParameter(0);
		 benefice=taux;
		 
		
	}
	
	
	
	
}
