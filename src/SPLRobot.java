
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

		if (c.getProperty("GFTmove")) {
			this.movement = new GFTMovement();
		}
		if (c.getProperty("WaveSurfing")) {
			this.movement = new WaveSurfing();
		}
		if (c.getProperty("LinearTargeting")) {
			this.targeting = new LinearTargeting();
		}
		if (c.getProperty("GuessFactor")) {
			this.targeting = new GuessFactorTargeting();
		}
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
			/*
			if (movement.getClass() == WaveSurfing.class) {
				WaveSurfing robotMovement = (WaveSurfing) movement;
				this.paint = new Paint(robotMovement._enemyWaves, robotMovement._myLocation);
				}*/
			this.paint = new Paint();
		}

	}

	/*
	 * public SPLRobot(boolean GFTmovement, boolean waveSurfing, boolean
	 * linearTargeting, boolean guessFactorTargeting) { if (GFTmovement){
	 * this.movement= new GFTMovement(); } if (waveSurfing){ this.movement= new
	 * WaveSurfing(); } if (linearTargeting){ this.targeting= new LinearTargeting();
	 * } if (guessFactorTargeting){ this.targeting= new GuessFactorTargeting() ; } }
	 * 
	 */

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
