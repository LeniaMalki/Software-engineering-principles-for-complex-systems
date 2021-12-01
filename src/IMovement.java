import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

/**
 * TODO description
 */
public interface IMovement {
	
	void onScannedRobotMove(ScannedRobotEvent e, AdvancedRobot robot);
	void run();

}