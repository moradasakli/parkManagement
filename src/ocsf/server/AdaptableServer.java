package ocsf.server;

class AdaptableServer extends AbstractServer {
   private ObservableServer server;

   public AdaptableServer(int port, ObservableServer server) {
      super(port);
      this.server = server;
   }

   protected final void clientConnected(ConnectionToClient client) {
      this.server.clientConnected(client);
   }

   protected final void clientDisconnected(ConnectionToClient client) {
      this.server.clientDisconnected(client);
   }

   protected final void clientException(ConnectionToClient client, Throwable exception) {
      this.server.clientException(client, exception);
   }

   protected final void listeningException(Throwable exception) {
      this.server.listeningException(exception);
   }

   protected final void serverStopped() {
      this.server.serverStopped();
   }

   protected final void serverStarted() {
      this.server.serverStarted();
   }

   protected final void serverClosed() {
      this.server.serverClosed();
   }

   protected final void handleMessageFromClient(Object msg, ConnectionToClient client) {
      this.server.handleMessageFromClient(msg, client);
   }
}
