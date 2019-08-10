import java.io.*;
import java.net.*;
import java.util.*;

// sensors, whoami, ifconfig, iostat, uptime

public class Agent {
  public String ifconfigInterface;

  public String getUptime() throws IOException {
    Process p = Runtime.getRuntime().exec("uptime -p");

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";
    while((commandOutput = pout.readLine()) != null){break;}

    return commandOutput;
  }

  public double getCPUutil() throws IOException {
    Process p = Runtime.getRuntime().exec("iostat -c");

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      if(q == 3) break;
      else q++;
    }

    String[] ex = commandOutput.split(" ");
    return 100.0 - Float.valueOf(ex[ex.length -1]);
  }

  public String getDLVolume() throws IOException {
    Process p = Runtime.getRuntime().exec("ifconfig -v " + ifconfigInterface);

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      if(q == 4) break;
      else q++;
    }

    String[] ex = commandOutput.split(" ");
    return ex[14] + " " + ex[15];
  }

  public String getULVolume() throws IOException {
    Process p = Runtime.getRuntime().exec("ifconfig -v " + ifconfigInterface);

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      // System.out.println(q + " " + commandOutput);
      if(q == 6) break;
      else q++;
    }

    String[] ex = commandOutput.split(" ");
    return ex[14] + " " + ex[15];
    // return Integer.valueOf(ex[14]);
    // return 1;
  }

  public String getPublicIP() throws IOException {
    Process p = Runtime.getRuntime().exec("ifconfig -v " + ifconfigInterface);

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      // System.out.println(q + " " + commandOutput);
      if(q == 1) break;
      else q++;
    }

    String[] ex = commandOutput.split(" ");
    return ex[9];
    // return 1;
  }

  public String getUsername() throws IOException {
    Process p = Runtime.getRuntime().exec("whoami");

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      // System.out.println(q + " " + commandOutput);
      if(q == 0) break;
      else q++;
    }

    String[] ex = commandOutput.split(" ");
    return ex[0];
    // return 1;
  }

  public Long getDLRate() throws IOException {
    Long prev, next;

    Process p = Runtime.getRuntime().exec("ifconfig -v " + ifconfigInterface);

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      if(q == 4) break;
      else q++;
    }

    String[] ex = commandOutput.split(" ");
    prev = Long.parseUnsignedLong(ex[13]);
    // prev = Integer.valueOf(ex[13]);

    try {Thread.sleep(1000);} catch(InterruptedException e){}

    p = Runtime.getRuntime().exec("ifconfig -v " + ifconfigInterface);
    pout = new BufferedReader(new InputStreamReader(p.getInputStream()));

    q = 0;
    while((commandOutput = pout.readLine()) != null){
      if(q == 4) break;
      else q++;
    }

    ex = commandOutput.split(" ");
    next = Long.parseUnsignedLong(ex[13]);
    // next = Integer.valueOf(ex[13]);

    return (next - prev);
  }

  public Long getULRate() throws IOException {
    Long prev, next;

    Process p = Runtime.getRuntime().exec("ifconfig -v " + ifconfigInterface);
    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      if(q == 6) break;
      else q++;
    }

    String[] ex = commandOutput.split(" ");
    prev = Long.parseUnsignedLong(ex[13]);
    // prev = Integer.valueOf(ex[13]);

    try {Thread.sleep(1000);} catch(InterruptedException e){}

    p = Runtime.getRuntime().exec("ifconfig -v " + ifconfigInterface);
    pout = new BufferedReader(new InputStreamReader(p.getInputStream()));

    q = 0;
    while((commandOutput = pout.readLine()) != null){
      if(q == 6) break;
      else q++;
    }

    ex = commandOutput.split(" ");
    next = Long.parseUnsignedLong(ex[13]);
    // next = Integer.valueOf(ex[13]);

    return (next - prev);
  }

  public String getCoreTemps() throws IOException {
    Process p = Runtime.getRuntime().exec("sensors *-isa-*");

    BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String commandOutput = "";
    String ret = "";

    int q = 0;
    while((commandOutput = pout.readLine()) != null){
      String[] ex = commandOutput.split(" ");
      if(ex[0].equals("Core")) {
        ret = ret + "Core " + ex[1] + " " + ex[9] + "\n ";
      } else {
        q++;
      }
    }

    return ret;
  }


  public static void main(String[] fudge) throws Exception {
    if(fudge.length != 2) {
			System.out.println("Usage: java Agent [ip] [port]");
			System.exit(1);
		}

    Agent a = new Agent();

    // the agent should be configured to only one overlook one interface from ifconfig
    Scanner cin = new Scanner(System.in);
    System.out.print("Enter the ifconfig interface you want to overlook: ");
    a.ifconfigInterface = cin.nextLine();

    // connect to the server
    Socket socket = new Socket(fudge[0], Integer.parseInt(fudge[1]));

    // create input and output streams
    BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);

    // create a thread that will talk to the monitor directly
    AgentSpeak as = new AgentSpeak(sout, a);
    Thread speak = new Thread(as);
    speak.start();
  }
}
