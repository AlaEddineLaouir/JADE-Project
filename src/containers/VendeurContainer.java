package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class VendeurContainer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			
			Runtime runtime =Runtime.instance();
			ProfileImpl profileImpl =new ProfileImpl(false);
			
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			
			AgentContainer vendeurContainer =runtime.createAgentContainer(profileImpl);
			
			AgentController vendeurControlleur = vendeurContainer.createNewAgent("vendeur", agents.Vendeur.class.getName(),new Object[] {});
			AgentController vendeurControlleur2 = vendeurContainer.createNewAgent("vendeur2", agents.Vendeur.class.getName(),new Object[] {});

			
			vendeurControlleur.start();
			vendeurControlleur2.start();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
