import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 * TODO description
 */
public class GuessFactorTargeting implements ITargeting {
	
	private static final double BULLET_POWER = 1.9;

	private static double lateralDirection;
	private static double lastEnemyVelocity;

	
	public void onScannedRobotTarget(ScannedRobotEvent e,AdvancedRobot robot) {
		double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();
		if (enemyVelocity != 0) {
			lateralDirection = GFTUtils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
		}
		GFTWave wave = new GFTWave(robot);
		wave.gunLocation = new Point2D.Double(robot.getX(), robot.getY());
		GFTWave.targetLocation = GFTUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;

		robot.setTurnGunRightRadians(Utils
				.normalRelativeAngle(enemyAbsoluteBearing - robot.getGunHeadingRadians() + wave.mostVisitedBearingOffset()));

		robot.setFire(wave.bulletPower);
		if (robot.getEnergy() >= BULLET_POWER) {
			robot.addCustomEvent(wave);
		}
		robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - robot.getRadarHeadingRadians()) * 2);
		
	}
	
	public void run() {
		lateralDirection = 1;
		lastEnemyVelocity = 0;
		
	}

}

class GFTWave extends Condition {
	static Point2D targetLocation;

	double bulletPower;
	Point2D gunLocation;
	double bearing;
	double lateralDirection;

	private static final double MAX_DISTANCE = 900;
	private static final int DISTANCE_INDEXES = 5;
	private static final int VELOCITY_INDEXES = 5;
	private static final int BINS = 25;
	private static final int MIDDLE_BIN = (BINS - 1) / 2;
	private static final double MAX_ESCAPE_ANGLE = 0.7;
	private static final double BIN_WIDTH = MAX_ESCAPE_ANGLE / (double) MIDDLE_BIN;

	private static int[][][][] statBuffers = new int[DISTANCE_INDEXES][VELOCITY_INDEXES][VELOCITY_INDEXES][BINS];

	private int[] buffer;
	private AdvancedRobot robot;//TODO ta bort
	private double distanceTraveled;

	GFTWave(AdvancedRobot _robot) { //TODO ta bort
		this.robot = _robot;
	}

	public boolean test() {
		advance();
		if (hasArrived()) {
			buffer[currentBin()]++;
			robot.removeCustomEvent(this);
		}
		return false;
	}

	double mostVisitedBearingOffset() {
		return (lateralDirection * BIN_WIDTH) * (mostVisitedBin() - MIDDLE_BIN);
	}

	void setSegmentations(double distance, double velocity, double lastVelocity) {
		int distanceIndex = Math.min(DISTANCE_INDEXES - 1, (int) (distance / (MAX_DISTANCE / DISTANCE_INDEXES)));
		int velocityIndex = (int) Math.abs(velocity / 2);
		int lastVelocityIndex = (int) Math.abs(lastVelocity / 2);
		buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
	}

	private void advance() {
		distanceTraveled += GFTUtils.bulletVelocity(bulletPower);
	}

	private boolean hasArrived() {
		return distanceTraveled > gunLocation.distance(targetLocation) - 18;
	}

	private int currentBin() {
		int bin = (int) Math
				.round(((Utils.normalRelativeAngle(GFTUtils.absoluteBearing(gunLocation, targetLocation) - bearing))
						/ (lateralDirection * BIN_WIDTH)) + MIDDLE_BIN);
		return GFTUtils.minMax(bin, 0, BINS - 1);
	}

	private int mostVisitedBin() {
		int mostVisited = MIDDLE_BIN;
		for (int i = 0; i < BINS; i++) {
			if (buffer[i] > buffer[mostVisited]) {
				mostVisited = i;
			}
		}
		return mostVisited;
	}

}

class GFTUtils2 {
	static double bulletVelocity(double power) {
		return 20 - 3 * power;
	}

	static Point2D project(Point2D sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
				sourceLocation.getY() + Math.cos(angle) * length);
	}

	static double absoluteBearing(Point2D source, Point2D target) {
		return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
	}

	static int sign(double v) {
		return v < 0 ? -1 : 1;
	}

	static int minMax(int v, int min, int max) {
		return Math.max(min, Math.min(max, v));
	}
}