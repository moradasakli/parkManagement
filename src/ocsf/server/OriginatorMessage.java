package ocsf.server;

public class OriginatorMessage {
   private ConnectionToClient originator;
   private Object message;

   public OriginatorMessage(ConnectionToClient originator, Object message) {
      this.originator = originator;
      this.message = message;
   }

   public ConnectionToClient getOriginator() {
      return this.originator;
   }

   public Object getMessage() {
      return this.message;
   }
}
