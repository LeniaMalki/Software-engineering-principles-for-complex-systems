package Movement;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import robocode.AdvancedRobot;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import concrete.GFTUtils;

/**
 * TODO description
 */
public class GFTMovement implements IMovement{
	
	private static final double BATTLE_FIELD_WIDTH = 800;
	private static final double BATTLE_FIELD_HEIGHT = 600;
	private static final double WALL_MARGIN = 18;
	private static final double MAX_TRIES = 125;
	private static final double REVERSE_TUNER = 0.421075;
	private static final double DEFAULT_EVASION = 1.2;
	private static final double WALL_BOUNCE_TUNER = 0.699484;

	private Rectangle2D fieldRectangle = new Rectangle2D.Double(WALL_MARGIN, WALL_MARGIN,
			BATTLE_FIELD_WIDTH - WALL_MARGIN * 2, BATTLE_FIELD_HEIGHT - WALL_MARGIN * 2);
	private double enemyFirePower = 3;
	private double direction = 0.4;


	public void onScannedRobotMove(ScannedRobotEvent e,AdvancedRobot robot) {
		double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		Point2D robotLocation = new Point2D.Double(robot.getX(), robot.getY());
		Point2D enemyLocation = GFTUtils.project(robotLocation, enemyAbsoluteBearing, enemyDistance);
		Point2D robotDestination;
		double tries = 0;
		while (!fieldRectangle.contains(robotDestination = GFTUtils.project(enemyLocation,
				enemyAbsoluteBearing + Math.PI + direction, enemyDistance * (DEFAULT_EVASION - tries / 100.0)))
				&& tries < MAX_TRIES) {
			tries++;
		}
		if ((Math.random() < (GFTUtils.bulletVelocity(enemyFirePower) / REVERSE_TUNER) / enemyDistance
				|| tries > (enemyDistance / GFTUtils.bulletVelocity(enemyFirePower) / WALL_BOUNCE_TUNER))) {
			direction = -direction;
		}
		// Jamougha's cool way
		double angle = GFTUtils.absoluteBearing(robotLocation, robotDestination) - robot.getHeadingRadians();
		robot.setAhead(Math.cos(angle) * 100);
		robot.setTurnRightRadians(Math.tan(angle));
	}

	
	public void run(AdvancedRobot robot) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCustomEvent(CustomEvent e, AdvancedRobot robot) {
		// TODO Auto-generated method stub
		
	}
	
	
}
