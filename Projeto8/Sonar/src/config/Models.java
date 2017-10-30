package config;

import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

public class Models {
	private LineMap map;
	public Models(LineMap map) {
		this.map = map;
	}

	public double expectedSonarRead(Pose p, double angle) {
		/**************** MODELO DO SONAR *******************/
		Pose tmppose = new Pose(p.getX(), p.getY(), p.getHeading());
		float mindist = Float.POSITIVE_INFINITY;
    int teta2 = 24;
		int cone = teta2*2;
		for (int angulo=-cone/2; angulo <= cone/2; angulo++) {
			tmppose.setHeading((float) (p.getHeading() - angulo + angle));
			float dist = map.range(tmppose);
			if (dist > 0 && dist < mindist)
				mindist = dist;
		}
		return mindist;
	}
}
