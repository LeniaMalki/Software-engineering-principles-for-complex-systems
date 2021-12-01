import robocode.AdvancedRobot;

public class ConstantRotation{
	private AdvancedRobot robot;
	
	public void run() {

		robot.setAdjustGunForRobotTurn(true);
	    robot.setAdjustRadarForGunTurn(true);
	
	    do {
	        // basic mini-radar code
	        robot.turnRadarRightRadians(Double.POSITIVE_INFINITY);
	    } while (true);
	}
}