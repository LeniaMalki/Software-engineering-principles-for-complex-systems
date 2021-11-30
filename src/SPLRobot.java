
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.*; // for Point2D's
import java.lang.*; // for Double and Integer objects
import java.util.ArrayList; // for collection of waves

public class SPLRobot extends AdvancedRobot{
	
	private IMovement movement;
	private ITargeting targeting;

	

	public void run() {

		// #if Coloring
//@	    	setColors(Color.BLUE, Color.BLACK, Color.YELLOW); 
		// #endif

		targeting.run();
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




