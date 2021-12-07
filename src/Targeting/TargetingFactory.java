package Targeting;
import properties.ConfigurationManager;

/**
 * TODO description
 */
public class TargetingFactory {
	
	ConfigurationManager c = ConfigurationManager.getInstance();
	
	public ITargeting getTargeting() {
		if (c.getProperty("LinearTargeting")) {
			return new LinearTargeting();
		}
		if (c.getProperty("GuessFactor")) {
			return new GuessFactorTargeting();
		}
		return null;
	}
	
	

}