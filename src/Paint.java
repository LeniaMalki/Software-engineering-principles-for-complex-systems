import java.awt.geom.Point2D;
import java.util.ArrayList;


/**
 * TODO description
 */
public class Paint {
	/*
	private ArrayList _enemyWaves;
	public Point2D.Double _myLocation;
	
	public Paint (ArrayList enemyWaves, Point2D.Double myLocation) {
		this._enemyWaves = enemyWaves;
		this._myLocation = myLocation;
	}*/
	
	
	
	  public void onPaint(java.awt.Graphics2D g, ArrayList _enemyWaves, Point2D.Double _myLocation) { 
		  g.setColor(java.awt.Color.red);
		  for (int i = 0; i < _enemyWaves.size(); i++) {
			  EnemyWave w = (EnemyWave)(_enemyWaves.get(i)); 
			  Point2D.Double center = w.fireLocation;
	
			  int radius = (int)w.distanceTraveled;
	  
		 
			  if (radius - 40 <	center.distance(_myLocation)) 
				  g.drawOval((int) (center.x - radius), (int)(center.y - radius), radius * 2, radius * 2); 
			  } 
		  }

}