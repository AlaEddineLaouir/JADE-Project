package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gui.ClientGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Client extends GuiAgent {

	
	private ClientGUI clientGUI;
	private Agent myAgent;
	private ArrayList<AID> listeVendeurs;
	private Map<String , AID> tableCorrespondence;
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		myAgent=this;
		
		listeVendeurs =new ArrayList<AID>();
		tableCorrespondence=new HashMap<String, AID>();

		clientGUI = new ClientGUI();
		clientGUI.setClientAgent(this);
		clientGUI.showMessage("Agent Demmarer", true);
		
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				
				
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("vendeur-livre"); template.addServices(sd);
				try
				{
					clientGUI.showMessage("Start search \n", false);
					DFAgentDescription[] result = DFService.search(myAgent, template);
					clientGUI.showMessage("Nomber vendeur trouver :"+result.length+" \n", false);
					for(int i = 0; i < result.length; ++i) {
						listeVendeurs.add(result[i].getName());
						tableCorrespondence.put(result[i].getName().getName(), result[i].getName());
					}
					
				}
				catch (Exception e) { e.printStackTrace(); }

			}
				
		});
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				MessageTemplate template =MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL), 
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REFUSE),
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
						MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM))))
						);
				
				ACLMessage msg =receive(template);
				
				if(msg != null)
				{
					System.out.println("Message Arriver");
					switch (msg.getPerformative()) {
					case ACLMessage.PROPOSE:
						
						clientGUI.showMessage("Le vendeur : "+msg.getSender().getName()+" a proposer :"+msg.getContent(), false);
						
						break;
					case ACLMessage.REFUSE :

						clientGUI.showMessage("Le vendeur : "+msg.getSender().getName()+" a refuse votre offer", false);
						
						break;
					case ACLMessage.ACCEPT_PROPOSAL:

						clientGUI.showMessage("Le vendeur : "+msg.getSender().getName()+" a accepter votre offer et le livre sera livrer a vous", false);
						break;
					case ACLMessage.CONFIRM :
							clientGUI.showMessage(msg.getContent(), false);
						break;
					case ACLMessage.DISCONFIRM :
						clientGUI.showMessage(msg.getContent(), false);
						break;

					default:
						break;
					}
				}
				
			}
		});

		
		
		
		
	}

	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		super.takeDown();
	}

	@Override
	protected void onGuiEvent(GuiEvent event) {
		
		Map<String, Object> params = (Map<String, Object>) event.getParameter(0);
		
		switch (event.getType()) {
		case 1:
			clientGUI.showMessage("Envoi A un agent", false);
			String type =(String) params.get("type");
			String agent =(String)params.get("agent");
			String message=(String)params.get("message");
			
			sendRequest(type,agent,message);
			
			break;
		case 2:
			
			
			String livre=(String) params.get("livre");
			try {
				sendRequest(livre);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			clientGUI.showMessage("Cherche un livre", true);
			break;

		default:
			break;
		}
		
	}

	private void sendRequest(String type, String agent, String message) {
		// TODO Auto-generated method stub
		ACLMessage msg;
		if(type.equals("propose"))
		{
			msg=new ACLMessage(ACLMessage.PROPOSE);
			msg.setContent(message);
		}
		else
			msg=new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			
		System.out.println(tableCorrespondence.get(agent));
		msg.addReceiver(tableCorrespondence.get(agent));
		myAgent.send(msg);
	}

	private void sendRequest(String livre) throws Exception {
		
		// TODO Auto-generated method stub
		
		for(AID vendeur : listeVendeurs)
		{
			ACLMessage msg =new ACLMessage(ACLMessage.CFP);
			msg.addReceiver(vendeur);
			msg.setContent(livre);
			
			myAgent.send(msg);
		}		
		
	}

}
