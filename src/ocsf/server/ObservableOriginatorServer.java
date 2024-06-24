package ocsf.server;

public class ObservableOriginatorServer extends ObservableServer {
   public ObservableOriginatorServer(int port) {
      super(port);
   }

   protected synchronized void handleMessageFromClient(Object message, ConnectionToClient client) {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage(client, message));
   }

   protected synchronized void clientConnected(ConnectionToClient client) {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage(client, "#OS:Client connected."));
   }

   protected synchronized void clientDisconnected(ConnectionToClient client) {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage(client, "#OS:Client disconnected."));
   }

   protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage(client, "#OS:Client exception." + exception.getMessage()));
   }

   protected synchronized void listeningException(Throwable exception) {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage((ConnectionToClient)null, "#OS:Listening exception." + exception.getMessage()));
   }

   protected synchronized void serverStarted() {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage((ConnectionToClient)null, "#OS:Server started."));
   }

   protected synchronized void serverStopped() {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage((ConnectionToClient)null, "#OS:Server stopped."));
   }

   protected synchronized void serverClosed() {
      this.setChanged();
      this.notifyObservers(new OriginatorMessage((ConnectionToClient)null, "#OS:Server closed."));
   }
}
