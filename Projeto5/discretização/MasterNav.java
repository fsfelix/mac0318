import java.util.ArrayList;
import lejos.pc.comm.*;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;
import java.util.Scanner;
import java.io.*;
import lejos.util.Delay;

public class MasterNav {
    private static final byte ADD_POINT = 0; //adds waypoint to path
    private static final byte TRAVEL_PATH = 1; // enables slave to execute the path
    private static final byte STATUS = 2; // enquires about slave's position 
    private static final byte STOP = 3; // closes communication
	
    private NXTComm nxtComm;
    private DataOutputStream dos;
    private DataInputStream dis;	
	
    private static final String NXT_ID = "NXT15"; // NXT BRICK ID

    private static Point[] points = {
        new Point(100,813),    /* P1 */
        new Point(428,873),   /* P2 */
        new Point(1140,885),  /* P3 */
        new Point(1117,432),  /* P4 */
        new Point(830,507),   /* P5 */
        new Point(690,571),   /* P6 */
        new Point(450,593),   /* P7 */
        new Point(263,350),   /* P8 */
        new Point(531,382),   /* P9 */
        new Point(986,166),    /* P10 */
        new Point(490,100)     /* P11 */
    };

    private float sendCommand(byte command, float paramX, float paramY) {
        try {
            dos.writeByte(command);
            dos.writeFloat(paramX);
            dos.writeFloat(paramY);
            dos.flush();
            return dis.readFloat();
        } catch (IOException ioe) {
            System.err.println("IO Exception");
            System.exit(1);
            return -1f;
        }
    }
    private boolean sendCommand(byte command) {
        try {
            dos.writeByte(command);
            dos.flush();
            return dis.readBoolean();
        } catch (IOException ioe) {
            System.err.println("IO Exception");
            System.exit(1);
            return false;
        }
    }

    private void connect() {
        try {
            NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
            /* Uncomment next line for Bluetooth communication */
            // NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);			
            NXTInfo[] nxtInfo = nxtComm.search(MasterNav.NXT_ID);
			
            if (nxtInfo.length == 0) {
                System.err.println("NO NXT found");
                System.exit(1);
            }
			
            if (!nxtComm.open(nxtInfo[0])) {
                System.err.println("Failed to open NXT");
                System.exit(1);
            }
			
            dis = new DataInputStream(nxtComm.getInputStream());
            dos = new DataOutputStream(nxtComm.getOutputStream());
			
        } catch (NXTCommException e) {
            System.err.println("NXTComm Exception: "  + e.getMessage());
            System.exit(1);
        }
    }		

    private void close() {
        try {
            dos.writeByte(STOP);
            dos.writeFloat(0f);
            dos.writeFloat(0f);
            dos.flush();
            Thread.sleep(200);
            System.exit(0);
        } catch (Exception ioe) {
            System.err.println("IO Exception");
        }
    }

    public static ArrayList <Point> xyToMap(ArrayList <coord> path, int w, int h) {
        ArrayList <Point> points = new ArrayList <Point> ();
        for (coord p : path) {
            Point tmp = new Point((int) Math.round(p.x() * w), (int) Math.round(p.y() * h));
            points.add(tmp);
        }

        return points;
    }

    public static void main(String[] args) {
        byte cmd = 0; float param = 0f; float ret=0f; float addX = 0f; float addY = 0f; boolean boolRet = false;
        MasterNav master = new MasterNav();
        master.connect();
        Scanner scan = new Scanner( System.in );

        ArrayList <coord> path = new ArrayList <coord> ();
        ArrayList <Point> pointsPath = new ArrayList <Point> ();

        /***********************************************/

        // OLD WAY

        // coord init = new coord(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        // coord goal = new coord(Integer.parseInt(args[2]), Integer.parseInt(args[3]));

        // Discrete dsc = new Discrete (40, 40);
        // widthSearch wS = new widthSearch();

        // wS.search(dsc.map, init, goal, 4);
        // path = wS.getPath(dsc.map, init, goal, 4);
        // // path = wS.getPath(dsc.map, goal, init, 4);

        // for (coord p : path)
        //     System.out.println(p.x() + " " + p.y());

        // points = dsc.xyToMap(path, 40, 40);

        // for (Point p : points)
        //     System.out.println(p.x + " " + p.y);

        /***********************************************/

        // NEW WAY
        widthSearch wS = new widthSearch();
        boolean linearize = false;

        path = wS.frenteDeOnda(40, points[0], points[10], linearize);
        pointsPath = xyToMap(path, 40, 40);


        for (Point p : pointsPath) {
            ret = master.sendCommand((byte) 0, (int) p.x/10, (int) p.y/10);
            Delay.msDelay(100);

        }
        ret = master.sendCommand((byte) 1, -1, -1);
        int num = scan.nextInt();
    }

}
