
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.*; // for Point2D's
import java.lang.*; // for Double and Integer objects
import java.util.ArrayList; // for collection of waves

public class SPLRobot extends AdvancedRobot{
	
	private IMovement movement;
	private ITargeting targeting;
	private ConstantRotation radar;
	private ColorRobot color;


	

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

		
		color.run();
		targeting.run();
		movement.run();
		radar.run();

	}

	public void onScannedRobot(ScannedRobotEvent e) {
		
		movement.onScannedRobotMove(e);
		targeting.onScannedRobotTarget(e);
		

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




