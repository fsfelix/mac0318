import lejos.geom.*;
import lejos.robotics.mapping.LineMap;

Line[] lines = {
  /* L-shape polygon */
  new Line(164,356,58,600),
  new Line(58,600,396,721),
  new Line(396,721,455,600),
  new Line(455,600,227,515),
  new Line(227,515,280,399),
  new Line(280,399,164,356),
  /* Triangle */
  new Line(778,526,1079,748),
  new Line(1079,748,1063,436),
  new Line(1063,436,778,526),
  /* Pentagon */
  new Line(503,76,333,267),
  new Line(333,267,481,452),
  new Line(481,452,730,409),
  new Line(730,409,704,150),
  new Line(704,150,503,76)
};
Rectangle bounds = new Rectangle(0, 0, 1189, 841);
LineMap mymap = new LineMap(lines, bounds);

Point[] points = {
  new Point(94,737),    /* P1 */
  new Point(428,800),   /* P2 */
  new Point(1147,811),  /* P3 */
  new Point(1127,355),  /* P4 */
  new Point(831,430),   /* P5 */
  new Point(693,494),   /* P6 */
  new Point(449,515),   /* P7 */
  new Point(260,271),   /* P8 */
  new Point(534,303),   /* P9 */
  new Point(994,88),    /* P10 */
  new Point(489,18)     /* P11 */
};
