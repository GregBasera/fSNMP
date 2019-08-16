# Project
---
| Name | fSNMP |
|---|---|
| Description | A jerry-rigged implementation of a Simple Network Management Protocol. This is a bonus activity offered in my DataCom course. I was just in awe on the number of possibilities applications like this have. |
| Author | Basera, Greg Emerson A. (gbasera@gbox.adnu.edu.ph) |

# Environment
---

* Linux (Ubuntu)
* Java

#### Note:

The author would advice anyone who plan to use this software to run it in a Linux environment (preferably Ubuntu) with SuperUser(**sudo**) capabilities. Below are the applications/libraries the program requires to properly function.

| Linux Commands | Install |
|---|---|
| whoami | installed by default |
| uptime | installed by default |
| ifconfig | $ sudo apt get install ifconfig |
| iostat | $ sudo apt get install iostat |
| sensors | $ sudo apt get install lm-sensors |

# Let's play
---

1. Before anything else, clone this repo.
```
  $ git clone https://github.com/GregBasera/fSNMP.git fSNMP
```
  * *Note: SNMPs are networking applications used to control and manage networks. If you're not familiar with this kinds of applictions or Networking in general, please do read up on topics first.*


2. Set up a Monitor to a port number.
```
  $ javac Monitor.java
  $ java Monitor [port]
```
  * [port] --> can be any number from 5000 to 1024.


3. Connect an Agent to the Monitor. The application will ask what networking interface would you like to submit to the Monitor (it could be your wired or wireless connection).
```
  $ javac Agent.java
  $ java Agent [ip] [port]
```
  * [ip] --> the IP address of the Monitor.
  * [port] --> the port number the Monitor is initialized in.


4. Repeat item 3 for all agents.
5. The Agents will submit a report every 10 seconds, the Monitor on the other hand will display the reports on the terminal
