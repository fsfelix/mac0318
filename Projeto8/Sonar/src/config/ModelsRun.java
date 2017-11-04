import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.robotics.mapping.LineMap;


public class ModelsRun {
    // private LineMap map;

    static Line[] lines2 = {
        /* L-shape polygon */
        new Line(0f, 0f, 155f, 0f),
        new Line(155f, 0f, 110f, 135f),
        new Line(110f, 135f, 0f, 132f),
        new Line(0f, 132f, 0f, 0f)
		};
		static Rectangle bounds = new Rectangle(0, 0, 1189, 841);
		static LineMap map = new LineMap(lines2, bounds);

    // public ModelsRun(LineMap map) {
    //     this.map = map;
    // }

    public static double expectedSonarRead(Pose p, double angle) {
        /**************** MODELO DO SONAR *******************/
        Pose tmppose = new Pose(p.getX(), p.getY(), p.getHeading());
        float mindist = Float.POSITIVE_INFINITY;
        int teta2 = 10;
        int cone = teta2*2;
        for (int angulo=-cone/2; angulo <= cone/2; angulo++) {
            tmppose.setHeading((float) (p.getHeading() - angulo + angle));
            float dist = map.range(tmppose);
            if (dist > 0 && dist < mindist)
                mindist = dist;
        }
        return mindist;
    }


    // robotData(d);
    // double expected = smodel.expectedSonarRead(d.getPose(), d.getSensorAngle()-90);

    public static void main(String[] args) {
        // Pose original = new Pose(25.0f, 25.0f, 0.0f); // primeira medicao
        // Pose original = new Pose(25.0f, 100.0f, 0.0f); // segunda medicao
        Pose original = new Pose(25.0f, 108.0f, 225.0f); // terceira medicao do giu

        for (int i = 90; i > -91; i=i-2) {
            double angle = (double) i;
            double expected = expectedSonarRead(original, -angle - 90);
            System.out.println("" + angle + " " + expected);
            // System.out.println((double)i);
        }
    }
}
