import lejos.pc.comm.*;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;
import lejos.nxt.Button;
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
            // NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
            /* Uncomment next line for Bluetooth communication */
            NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);           
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

    public static int[] arrayListToArray(List<Integer> integers) {
        
        int[] v = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        
        for (int i = 0; i < v.length; i++) 
            v[i] = iterator.next().intValue();
        
        return v;
    }


    public static void main(String[] args) {
        byte cmd = 0; float param = 0f; float ret=0f; float addX = 0f; float addY = 0f; boolean boolRet = false;
        MasterNav master = new MasterNav();
        master.connect();
        Scanner scan = new Scanner( System.in );

        ArrayList <Integer> path = new ArrayList <Integer> ();

        Graph graph = new Graph (11);
        graph.addBothEdges(0, 1, distance(points[0].x, points[0].y, points[1].x, points[1].y));
        graph.addBothEdges(1, 2, distance(points[1].x, points[1].y, points[2].x, points[2].y));
        graph.addBothEdges(2, 3, distance(points[2].x, points[2].y, points[3].x, points[3].y));
        graph.addBothEdges(1, 5, distance(points[1].x, points[1].y, points[5].x, points[5].y));
        graph.addBothEdges(5, 6, distance(points[5].x, points[5].y, points[6].x, points[6].y));
        graph.addBothEdges(4, 5, distance(points[4].x, points[4].y, points[5].x, points[5].y));
        graph.addBothEdges(3, 4, distance(points[3].x, points[3].y, points[4].x, points[4].y));
        graph.addBothEdges(3, 9, distance(points[3].x, points[3].y, points[9].x, points[9].y));
        graph.addBothEdges(4, 9, distance(points[4].x, points[4].y, points[9].x, points[9].y));
        graph.addBothEdges(7, 10, distance(points[7].x, points[7].y, points[10].x, points[10].y));
        graph.addBothEdges(10, 9, distance(points[10].x, points[10].y, points[9].x, points[9].y));
        
        path = graph.Dijkstra(0, 10);

        int[] trajetoria = new int[path.size()];
    
        for (int i = 0; i < trajetoria.length; i++) {
            trajetoria[i] = path.get(i) + 1;
            System.out.println(trajetoria[i]);      
        }        


        Button.waitForAnyPress();
        master.sendCommand((byte) 3, points[trajetoria[0]-1].x/10, points[trajetoria[0]-1].y/10);
        
        for (int i = 0; i < trajetoria.length; i++)
            {
                ret = master.sendCommand((byte) 0, points[trajetoria[i] - 1].x/10, points[trajetoria[i] - 1].y/10);
                Delay.msDelay(100);
            }
        
        ret = master.sendCommand((byte) 1, -1, -1);
        int num = scan.nextInt();
    }

}
