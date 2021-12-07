
import robocode.*;

import robocode.util.Utils;
import java.awt.geom.*; // for Point2D's
import java.lang.*; // for Double and Integer objects
import java.util.ArrayList; // for collection of waves

import properties.ConfigurationManager;

public class SPLRobot extends AdvancedRobot {

	private IMovement movement;
	private ITargeting targeting;
	private ConstantRotation radar;
	private ColorRobot color;
	private Paint paint;

	public SPLRobot() {
		ConfigurationManager c = ConfigurationManager.getInstance();
		MovementFactory movementFactory = new MovementFactory();
		TargetingFactory targetingFactory = new TargetingFactory();
		
		this.movement = movementFactory.getMovement();
		this.targeting = targetingFactory.getTargeting();

		
	
		if (c.getProperty("Coloring")) {
			this.color = new ColorRobot();
		}
		if (c.getProperty("ConstantRotation")) {
			this.radar = new ConstantRotation();
		}
		if (c.getProperty("Trigger")) {
			this.movement = new TriggerMovement();
		}
		if (c.getProperty("PaintWaves")) {
			this.paint = new Paint();
		}

	}


	public void run() {

		if (color != null) {
			color.run(this);
		}
		
		if (targeting != null) {
			targeting.run();
		}
		movement.run(this);
		radar.run(this);

	}

	public void onScannedRobot(ScannedRobotEvent e) {

		movement.onScannedRobotMove(e, this);
		if (targeting != null) {
			targeting.onScannedRobotTarget(e, this);
		}

	}
	
	public void onHitByBulletEvent(HitByBulletEvent e) {
		movement.onHitByBullet(e);
	}
	
	public void onCustomEvent(CustomEvent e) {
		movement.onCustomEvent(e,this);
	}
	
	public void onPaint(java.awt.Graphics2D g) {
		if (paint != null) {
			WaveSurfing robotMovement = (WaveSurfing) movement;
			paint.onPaint(g, robotMovement._enemyWaves, robotMovement._myLocation);
			}
		
	}

	


}
