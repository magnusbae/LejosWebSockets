package no.itera.lego.websocket;

import static no.itera.lego.util.EV3Helper.getColorName;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

import no.itera.lego.StateReceiver;
import no.itera.lego.util.RobotState;

public class WebSocketThread implements Runnable {

    private RobotState robotState;
    private BrickSocket socket;
    private List<StateReceiver> eventListeners = new ArrayList<>();

    public WebSocketThread(RobotState robotState) {
        this.robotState = robotState;
    }

    @Override
    public void run() {
        String url = String.format("ws://%s:%s", RobotState.HOST, RobotState.PORT);
        socket = new BrickSocket(url, robotState);
        socket.connect();

        EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S1);

        while (robotState.shouldRun){
            while (robotState.shouldRun && robotState.webSocketOpen) {
                //FIXME Examplecode, robot should send more info than this:
                //FIXME (And preferably in a better format)
                String colorName = getColorName(cs.getColorID());
                receiveColor(colorName);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //TODO Implement behaviour for what to do with the message received:
                //TODO NOTE: This does not have to be in this class, RobotState can easily be shared
                //TODO between threads
                if(robotState.lastMessage != null){
                    String message = robotState.lastMessage;
                    robotState.lastMessage = null;
                    System.out.println("Last message was: " + message);
                }
            }
            if(!robotState.webSocketOpen && !robotState.webSocketConnecting){
                System.out.println("Lost connection, reconnecting");
                socket.connect();
            }
        }
        socket.close();
        robotState.latch.countDown();
    }

    public void addEventListener(StateReceiver eventListener) {
        eventListeners.add(eventListener);
    }

    public void removeEventListener(StateReceiver eventListener) {
        eventListeners.remove(eventListener);
    }

    private void receiveColor(String color) {
        if (color.equals(robotState.lastColor)) {
          return;
        }
        socket.send(color);
        callEventListeners(color);
        robotState.lastColor = color;
    }

    private void callEventListeners(String color) {
        System.out.println("Got color: " + color);

        if ("BLACK".equals(color)) {
            for (StateReceiver eventListener : eventListeners) {
                eventListener.avoidEdge();
            }
        }
    }
}
