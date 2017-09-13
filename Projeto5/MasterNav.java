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
    public static void main(String[] args) {
        byte cmd = 0; float param = 0f; float ret=0f; float addX = 0f; float addY = 0f; boolean boolRet = false;
        MasterNav master = new MasterNav();
        master.connect();
        Scanner scan = new Scanner( System.in );

        int [] trajetoria1 = {1, 2, 3, 4, 10};
        int [] trajetoria2 = {1, 2, 6, 5, 10};
        int [] trajetoria3 = {1, 2, 7, 8, 11, 10};

        // while(true) {
            // System.out.print("Enter command [0:ADD_POINT 1:TRAVEL_PATH 2:STATUS 3:STOP]: ");
            // cmd = (byte) scan.nextFloat();
            // if (cmd == 0){
            //     System.out.println("Enter P-");
            //     int num  = scan.nextInt();
            //     addX = points[num-1].x/10;
            //     addY = points[num-1].y/10;
            //     // System.out.println("Enter coordinate X: ");
            //     // addX = scan.nextFloat();
            //     // System.out.println("Enter coordinate Y: ");
            //     // addY = scan.nextFloat();
            // } else {
            //     addX = -1;
            //     addY = -1;
            // }
            // if (cmd == 2){
            //     boolRet = master.sendCommand(cmd);
            //     System.out.println("cmd: " + " return: " + boolRet);
            // }else{
            //     ret = master.sendCommand(cmd, addX, addY); // return 0 when Slave successfully recieved the dos
            //     System.out.println("X: " + addX + " X: " + "Y: " + addY +" return: " + ret);
            // }

        // }
        for (int i = 0; i < trajetoria3.length; i++)
            {
            ret = master.sendCommand((byte) 0, points[trajetoria3[i] - 1].x/10, points[trajetoria3[i] - 1].y/10);
            Delay.msDelay(100);
            }
        ret = master.sendCommand((byte) 1, -1, -1);
        int num = scan.nextInt();
    }

}
