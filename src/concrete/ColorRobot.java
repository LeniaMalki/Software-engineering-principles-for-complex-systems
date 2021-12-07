package concrete;
import java.awt.Color;

import robocode.AdvancedRobot;

public class ColorRobot{
	
	public void run(AdvancedRobot robot) {
		robot.setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
	}
}