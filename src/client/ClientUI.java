package client;

import gui.clientConnectController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {
   public static ClientController chat;

   public static void main(String[] args) throws Exception {
      launch(args);
   }

   public void start(Stage primaryStage) throws Exception {
	   clientConnectController aFrame = new clientConnectController();
	   aFrame.start(primaryStage);
   }
}
