import java.awt.Color;

import robocode.AdvancedRobot;

public class ColorRobot{
	AdvancedRobot robot;
	
	public void run() {
		robot.setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
	}
}