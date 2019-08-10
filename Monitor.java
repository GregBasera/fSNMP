import java.io.*;
import java.net.*;
import java.util.*;

public class Monitor {
  public static void main(String[] fudge) throws Exception {
    if(fudge.length != 1) {
			System.out.println("Usage: java Monitor [port]");
			System.exit(1);
		}

    // Create a server socket
    ServerSocket server = new ServerSocket(Integer.parseInt(fudge[0]));

    while(true){
      // accepts clients (agents)
      Socket socket = server.accept();

      // create input and output streams for the accepted agent
      BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);

      // spawn a thread that will listen to the accepted agent
      // 1 agent : 1 thread
      MonitorListen ml = new MonitorListen(sin);
      Thread listen = new Thread(ml);
      listen.start();
    }
  }
}

// muliple Agents : one Monitor
// [thread.sleep] Agents must remit info per 10 seconds
// add cpu temp, realtime ul/dl, username, IP address functionalities
