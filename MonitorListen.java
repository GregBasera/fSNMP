import java.io.*;

public class MonitorListen implements Runnable {
  BufferedReader sin;

  public MonitorListen(BufferedReader sin) {
    this.sin = sin;
  }

  public void run() {
    while(true) {
      try {
        // receive from the agent
        String agent = "";
        while((agent = sin.readLine()) == null){}

        // just display the contents of the message
        System.out.println(agent);
        
      } catch(IOException e) {
        System.out.println("IOException in MonitorListen.java...");
      }
    }
  }
}
