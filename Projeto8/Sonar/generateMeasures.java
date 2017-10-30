package config;

import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;
import lejos.geom.Line;
import lejos.geom.Rectangle;

public class generateMeasures {
    Line[] lines2 = {
        /* L-shape polygon */
        new Line(0f, 0f, 155f, 0f),
        new Line(155f, 0f, 110f, 135f),
        new Line(110f, 135f, 0f, 132f),
        new Line(0f, 132f, 0f, 0f)
		};

		Rectangle bounds = new Rectangle(0, 0, 1189, 841);
    LineMap map = new LineMap(lines2, bounds);

    public double expectedSonarRead(Pose p, double angle, int cone) {
        /**************** MODELO DO SONAR *******************/
        Pose tmppose = new Pose(p.getX(), p.getY(), p.getHeading());
        float mindist = Float.POSITIVE_INFINITY;
        // int cone = 0;
        for (int angulo=-cone/2; angulo <= cone/2; angulo++) {
            tmppose.setHeading((float) (p.getHeading() - angulo + angle));
            float dist = map.range(tmppose);
            if (dist > 0 && dist < mindist)
                mindist = dist;
        }
        return mindist;
    }

    public void main(String[] args) {
        Pose p = new Pose(50.0f, 50.0f, 0.0f);
        int cone = 15;

        for (int i = 0; i < 181; i++) {
            float dist = expectedSonarRead(p, (double) i, cone);
            System.out.println(dist);
        }
    }
}
