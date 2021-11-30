import robocode.ScannedRobotEvent;

/**
 * TODO description
 */
public interface IMovement {
	
	void onScannedRobotMove(ScannedRobotEvent e);
	void run();

}