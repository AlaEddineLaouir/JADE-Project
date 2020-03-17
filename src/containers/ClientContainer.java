package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class ClientContainer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			Runtime runtime =Runtime.instance();
			ProfileImpl profileImpl =new ProfileImpl(false);
			
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			AgentContainer clientContainer =runtime.createAgentContainer(profileImpl);
			
			AgentController clientController =clientContainer.createNewAgent("client", agents.Client.class.getName(), new Object [] {});
		
			clientController.start();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
