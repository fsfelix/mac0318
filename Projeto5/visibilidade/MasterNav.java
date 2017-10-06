import lejos.pc.comm.*;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;
import lejos.nxt.Button;
import lejos.util.Delay;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class MasterNav {
    private static final byte ADD_POINT = 0; //adds waypoint to path
    private static final byte TRAVEL_PATH = 1; // enables slave to execute the path
    private static final byte STATUS = 2; // enquires about slave's position 
    private static final byte STOP = 3; // closes communication
    
    private NXTComm nxtComm;
    private DataOutputStream dos;
    private DataInputStream dis;    
    
    private static final String NXT_ID = "NXT15"; // NXT BRICK ID

    public static Line[] linesMap = {
        new Line(170,437,60,680),
        new Line(60,680,398,800),
        new Line(398,800,450,677),
        new Line(450,677,235,595),
        new Line(235,595,281,472),
        new Line(281,472,170,437),
        new Line(1070,815,770,602),
        new Line(770,602,1060,516),
        new Line(1070,815,1060,516),
        new Line(335,345,502,155),
        new Line(502,155,700,225),
        new Line(700,225, 725,490),
        new Line(725,490,480,525),
        new Line(480,525,335,345),
    };

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


    public static void main(String[] args) {
        byte cmd = 0; float param = 0f; float ret=0f; float addX = 0f; float addY = 0f; boolean boolRet = false;
        MasterNav master = new MasterNav();
        master.connect();
        Scanner scan = new Scanner( System.in );
        /*******************************************************************************************************/
        ArrayList <Point> finalPoints = new ArrayList <Point>();

        Visibility vsl = new Visibility(points[0], points[9], linesMap);
        finalPoints = vsl.findPath();
        Button.waitForAnyPress();

        /* Primeiro ponto*/
        Point initial = finalPoints.get(0);
        master.sendCommand((byte) 3, initial.x / 10, initial.y / 10);

        /* Demais pontos -> dividindo por 10?*/
        for (int i = 1; i < finalPoints.size(); i++) {
            Point p = finalPoints.get(i);
            ret = master.sendCommand((byte) 0, p.x / 10, p.y / 10);
            Delay.msDelay(100);
        }
   
        /*******************************************************************************************************/

        ret = master.sendCommand((byte) 1, -1, -1);
        int num = scan.nextInt();
    }

}
