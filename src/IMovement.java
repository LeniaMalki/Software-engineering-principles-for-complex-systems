import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

/**
 * TODO description
 */
public interface IMovement {
	
	void onScannedRobotMove(ScannedRobotEvent e, AdvancedRobot robot);
	void run();
	void onHitByBullet(HitByBulletEvent e);

}