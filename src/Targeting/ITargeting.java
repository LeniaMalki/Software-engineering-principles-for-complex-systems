package Targeting;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

/**
 * TODO description
 */
public interface ITargeting {
	
	void onScannedRobotTarget(ScannedRobotEvent e, AdvancedRobot robot);
	void run();

}
