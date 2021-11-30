
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.*; // for Point2D's
import java.lang.*; // for Double and Integer objects
import java.util.ArrayList; // for collection of waves

public class SPLRobot extends AdvancedRobot{
	
	private IMovement movement;
    private ITargeting targeting;
	

	// #if GuessFactor && GFTargetingData
	private static final double BULLET_POWER = 1.9;

	private static double lateralDirection;
	private static double lastEnemyVelocity;
	// #endif
	
	/*
	public SPLRobot(boolean GFTmovement, boolean waveSurfing, boolean linearTargeting, boolean guessFactorTargeting) {
		if (GFTmovement){
				this.movement= new GFTmovement();
		}
		if (waveSurfing){
			this.movement= new WaveSurfing();
		}
		if (linearTargeting){
			this.targeting= new LinearTargeting();
		}
		if (guessFactorTargeting){
			this.targeting= new GuessFactorTargeting() ;
		}
	}
	*/


	public void run() {

		// #if Coloring
//@	    	setColors(Color.BLUE, Color.BLACK, Color.YELLOW); 
		// #endif

		// #if GuessFactor && GFTargetingData
		lateralDirection = 1;
		lastEnemyVelocity = 0;
		// #endif

		movement.run();

		// #if ConstantRotation
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		do {
			// basic mini-radar code
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
		} while (true);
		// #endif
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		
		movement.onScannedRobotMove(e);


		// #if GuessFactor && GFTargetingData
		double enemyAbsoluteBearing = getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();
		if (enemyVelocity != 0) {
			lateralDirection = GFTUtils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
		}
		GFTWave wave = new GFTWave(this);
		wave.gunLocation = new Point2D.Double(getX(), getY());
		GFTWave.targetLocation = GFTUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;

		setTurnGunRightRadians(Utils
				.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians() + wave.mostVisitedBearingOffset()));

		setFire(wave.bulletPower);
		if (getEnergy() >= BULLET_POWER) {
			addCustomEvent(wave);
		}
		setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getRadarHeadingRadians()) * 2);

		// #endif

		
		movement.onScannedRobotMove(e);
		

	}

	

	// #if PaintWaves && WaveSurfing
	/*
	public void onPaint(java.awt.Graphics2D g) {
		g.setColor(java.awt.Color.red);
		for (int i = 0; i < _enemyWaves.size(); i++) {
			EnemyWave w = (EnemyWave) (_enemyWaves.get(i));
			Point2D.Double center = w.fireLocation;

			// int radius = (int)(w.distanceTraveled + w.bulletVelocity);
			// hack to make waves line up visually, due to execution sequence in robocode
			// engine
			// use this only if you advance waves in the event handlers (eg. in
			// onScannedRobot())
			// NB! above hack is now only necessary for robocode versions before 1.4.2
			// otherwise use:
			int radius = (int) w.distanceTraveled;

			// Point2D.Double center = w.fireLocation;
			if (radius - 40 < center.distance(_myLocation))
				g.drawOval((int) (center.x - radius), (int) (center.y - radius), radius * 2, radius * 2);
		}
	}
	*/
	// #endif

}

// #if GuessFactor && GFTargetingData 
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
	private AdvancedRobot robot;
	private double distanceTraveled;

	GFTWave(AdvancedRobot _robot) {
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
// #endif

// #if GFTmove || GuessFactor
class GFTUtils {
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
//#endif


