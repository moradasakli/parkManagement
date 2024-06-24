package Server;

import gui.ServerPortFrameController;
import java.util.Vector;
import javafx.application.Application;
import javafx.stage.Stage;
import EntityClasses.Order;

public class ServerUI extends Application {
   public static final int DEFAULT_PORT = 5555;
   

   public static void main(String[] args) throws Exception {
      launch(args);
   }

 
   public void start(Stage primaryStage) throws Exception {
      ServerPortFrameController aFrame = new ServerPortFrameController();
      aFrame.start(primaryStage);
      
   }

	public static void runServer(String url, String username, String password, String p) {
      
		int port = 0;
      try {
         port = Integer.parseInt(p);
      } catch (Throwable var5) {
         System.out.println("ERROR - Could not connect!");
      }

      EchoServer sv = new EchoServer(url, username, password, port);

      try {
         sv.listen();
      } catch (Exception var4) {
         System.out.println("ERROR - Could not listen for clients!");
      }

   }
}
