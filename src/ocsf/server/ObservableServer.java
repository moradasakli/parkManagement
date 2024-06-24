package ocsf.server;

import java.io.IOException;
import java.util.Observable;

public class ObservableServer extends Observable {
   public static final String CLIENT_CONNECTED = "#OS:Client connected.";
   public static final String CLIENT_DISCONNECTED = "#OS:Client disconnected.";
   public static final String CLIENT_EXCEPTION = "#OS:Client exception.";
   public static final String LISTENING_EXCEPTION = "#OS:Listening exception.";
   public static final String SERVER_CLOSED = "#OS:Server closed.";
   public static final String SERVER_STARTED = "#OS:Server started.";
   public static final String SERVER_STOPPED = "#OS:Server stopped.";
   private AdaptableServer service;

   public ObservableServer(int port) {
      this.service = new AdaptableServer(port, this);
   }

   public final void listen() throws IOException {
      this.service.listen();
   }

   public final void stopListening() {
      this.service.stopListening();
   }

   public final void close() throws IOException {
      this.service.close();
   }

   public void sendToAllClients(Object msg) {
      this.service.sendToAllClients(msg);
   }

   public final boolean isListening() {
      return this.service.isListening();
   }

   public final Thread[] getClientConnections() {
      return this.service.getClientConnections();
   }

   public final int getNumberOfClients() {
      return this.service.getNumberOfClients();
   }

   public final int getPort() {
      return this.service.getPort();
   }

   public final void setPort(int port) {
      this.service.setPort(port);
   }

   public final void setTimeout(int timeout) {
      this.service.setTimeout(timeout);
   }

   public final void setBacklog(int backlog) {
      this.service.setBacklog(backlog);
   }

   protected synchronized void clientConnected(ConnectionToClient client) {
      this.setChanged();
      this.notifyObservers("#OS:Client connected.");
   }

   protected synchronized void clientDisconnected(ConnectionToClient client) {
      this.setChanged();
      this.notifyObservers("#OS:Client disconnected.");
   }

   protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
      this.setChanged();
      this.notifyObservers("#OS:Client exception.");

      try {
         client.close();
      } catch (Exception var4) {
      }

   }

   protected synchronized void listeningException(Throwable exception) {
      this.setChanged();
      this.notifyObservers("#OS:Listening exception.");
      this.stopListening();
   }

   protected synchronized void serverStopped() {
      this.setChanged();
      this.notifyObservers("#OS:Server stopped.");
   }

   protected synchronized void serverClosed() {
      this.setChanged();
      this.notifyObservers("#OS:Server closed.");
   }

   protected synchronized void serverStarted() {
      this.setChanged();
      this.notifyObservers("#OS:Server started.");
   }

   protected synchronized void handleMessageFromClient(Object message, ConnectionToClient client) {
      this.setChanged();
      this.notifyObservers(message);
   }
}
