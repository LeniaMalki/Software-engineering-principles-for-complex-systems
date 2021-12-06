import robocode.AdvancedRobot;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

/**
 * TODO description
 */
public interface IMovement {
	
	void onScannedRobotMove(ScannedRobotEvent e, AdvancedRobot robot);
	void run(AdvancedRobot robot);
	void onHitByBullet(HitByBulletEvent e);
	void onCustomEvent(CustomEvent e, AdvancedRobot robot);

}