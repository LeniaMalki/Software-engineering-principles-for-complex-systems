import properties.ConfigurationManager;

/**
 * TODO description
 */
public class MovementFactory {
	
	ConfigurationManager c = ConfigurationManager.getInstance();
	
	public IMovement getMovement(){
		
		if (c.getProperty("GFTmove")) {
			return new GFTMovement();
		}
		if (c.getProperty("WaveSurfing")) {
			return new WaveSurfing();
		}
		return null;
		
	}

}