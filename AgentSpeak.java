import java.io.*;

public class AgentSpeak implements Runnable {
  PrintWriter sout;
  Agent a;

  public AgentSpeak(PrintWriter sout, Agent a) {
    this.sout = sout;
    this.a = a;
  }

  public void run(){
    while(true) {
      try {
        // build the string that will be sent to the monitor
        String send = String.format("%-30s %s@%s\n", "Agent (Username@IP-address):", a.getUsername(), a.getPublicIP()) +
          String.format("%-20s %s%%\n", "CPU Utilization:", String.format("%.2f", a.getCPUutil())) +
          String.format("%-20s %s\n", "System up-time:", a.getUptime()) +
          String.format("%-20s %s | %d bytes/sec\n", "Uploaded Data:", a.getULVolume(), a.getULRate()) +
          String.format("%-20s %s | %d bytes/sec\n", "Downloaded Data:", a.getDLVolume(), a.getDLRate()) +
          String.format("Core Temperatures:\n %s\n", a.getCoreTemps()) +
          String.format("---------------------------------------");

        // send the string to the monitor
        sout.println(send);

        // sleep for 10 seconds
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        // inability to sleep exceptions goes here
        System.out.println("Error in thread sleep..");
        System.exit(1);
      }
      catch (IOException w) {
        // errors on any of the agent functions goes here
        System.out.println("IOException.. Check to see if:\n\tyou're running this on a Linux system.\n\tyou have the nessesary apps this app is using.\n\tifconfigInterface is set correctly.");
        System.exit(1);
      }
    }
  }
}
