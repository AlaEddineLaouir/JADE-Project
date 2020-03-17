package containers;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;

public class MainContainer {

	public static void main(String[] args) {

		try {

			Runtime runtime=Runtime.instance();

			Properties properties=new ExtendedProperties();
			properties.setProperty(Profile.GUI,"true");
			ProfileImpl profileImplX=new ProfileImpl(properties);
			AgentContainer mainContainer=runtime.createMainContainer(profileImplX);
			mainContainer.start();
			
		}catch(Exception e)
		{

		}

	}

}
