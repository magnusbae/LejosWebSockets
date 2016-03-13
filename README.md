# EV3 to EV3 Remote

This [leJOS][lejos] application allows one EV3 to talk to a WebSocket server using the [Java-WebSocket][java-websocket]
library.

Communication can be over USB, Bluetooth or Wi-Fi. The IP address of the server is configured in the file
`RobotState.java`.

[lejos]: http://www.lejos.org/ev3/docs/
[java-websocket]: http://java-websocket.org/

## Requirements

You need to have [Java Development Kit 7][jdk7] (or higher) and [Ant][ant]installed on
your system.

[jdk7]: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
[ant]: http://ant.apache.org/

If you want to use IntelliJ, you may follow the guide in `README-INTELLIJ.md`.

##Building

It is recommended to build using Apache Ant instead of Eclipse, you may follow the guide in `README-ANT.md`.


## Motors and Sensors connections

EV3 Color Sensor connected to `S1`