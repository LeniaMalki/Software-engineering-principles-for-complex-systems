import robocode.AdvancedRobot;

public class ConstantRotation{
 	
	public void run(AdvancedRobot robot) {

		robot.setAdjustGunForRobotTurn(true);
	    robot.setAdjustRadarForGunTurn(true);
	
	    do {
	        // basic mini-radar code
	        robot.turnRadarRightRadians(Double.POSITIVE_INFINITY);
	    } while (true);
	}
}