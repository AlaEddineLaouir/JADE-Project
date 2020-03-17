package containers;

import bean.Livre;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class FournisseurContainer {

	public static void main(String[] args) {
		
		Livre l1 =new Livre("XML", 2, 350);
		Livre l2 =new Livre("JAVA", 3, 550);
		Livre l3 =new Livre("JavaScript", 2, 950);
		Livre l4 =new Livre("JAVA", 1, 750);
		Livre l5 =new Livre("PHP", 2, 150);
		Livre l6 =new Livre("SQL", 2, 650);
		
		
		try {
			
			Runtime runtime =Runtime.instance();
			ProfileImpl profileImpl =new ProfileImpl(false);
			
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			AgentContainer fournisseurContainer =runtime.createAgentContainer(profileImpl);
			
			AgentController fournisseurControlleur = fournisseurContainer.createNewAgent("fournisseur", agents.Fournisseur.class.getName(),new Object[] {l1,l2});
			AgentController fournisseurControlleur2 = fournisseurContainer.createNewAgent("fournisseur2", agents.Fournisseur.class.getName(),new Object[] {l1,l2,l3});

			
			fournisseurControlleur.start();
			fournisseurControlleur2.start();
			
		}catch(Exception e)
		{
			
		}
	}

}
