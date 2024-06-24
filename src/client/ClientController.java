package client;

import common.ChatIF;
import java.io.IOException;

public class ClientController implements ChatIF {
   public static int DEFAULT_PORT;
   public ChatClient client;
   
   
   public ClientController(String host, int port) {
      try {
         this.client = new ChatClient(host, port, this);
      } catch (IOException var4) {
         System.out.println("Error: Can't setup connection! Terminating client.");
         System.exit(1);
      }

   }

   public void accept(Object str) {
      this.client.handleMessageFromClientUI(str);
   }

   public void display(String message) {
      System.out.println("> " + message);
   }
}
