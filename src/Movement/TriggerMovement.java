package Movement;
import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

/**
 * TODO description
 */
public class TriggerMovement implements IMovement{
	
	int trigger; // Keeps track of when to move

	
	public void onScannedRobotMove(ScannedRobotEvent e, AdvancedRobot robot) {
		// TODO Auto-generated method stub
		
	}

	
	public void run(AdvancedRobot robot) {
		// Initially, we'll move when life hits 80
		trigger = 80;
		// Add a custom event named "trigger hit",
		robot.addCustomEvent(new Condition("triggerhit") {
			public boolean test() {
				return (robot.getEnergy() <= trigger);
			}
		});
		
	}

	
	public void onHitByBullet(HitByBulletEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * onCustomEvent handler
	 */
	public void onCustomEvent(CustomEvent e, AdvancedRobot robot) {
		// If our custom event "triggerhit" went off,
		if (e.getCondition().getName().equals("triggerhit")) {
			// Adjust the trigger value, or
			// else the event will fire again and again and again...
			trigger -= 20;
			//out.println("Ouch, down to " + (int) (getEnergy() + .5) + " energy.");
			// move around a bit.
			robot.turnLeft(65);
			robot.ahead(100);
		}
	}

}