package ocsf.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer implements Runnable {
   private ServerSocket serverSocket = null;
   private Thread connectionListener;
   private int port;
   private int timeout = 500;
   private int backlog = 10;
   private ThreadGroup clientThreadGroup;
   private boolean readyToStop = false;

   public AbstractServer(int port) {
      this.port = port;
      this.clientThreadGroup = new ThreadGroup("ConnectionToClient threads") {
         public void uncaughtException(Thread thread, Throwable exception) {
            AbstractServer.this.clientException((ConnectionToClient)thread, exception);
         }
      };
   }

   public final void listen() throws IOException {
      if (!this.isListening()) {
         if (this.serverSocket == null) {
            this.serverSocket = new ServerSocket(this.getPort(), this.backlog);
         }

         this.serverSocket.setSoTimeout(this.timeout);
         this.readyToStop = false;
         this.connectionListener = new Thread(this);
         this.connectionListener.start();
      }

   }

   public final void stopListening() {
      this.readyToStop = true;
   }

   public final synchronized void close() throws IOException {
      if (this.serverSocket != null) {
         this.stopListening();

         try {
            this.serverSocket.close();
         } finally {
            Thread[] clientThreadList = this.getClientConnections();

            for(int i = 0; i < clientThreadList.length; ++i) {
               try {
                  ((ConnectionToClient)clientThreadList[i]).close();
               } catch (Exception var8) {
               }
            }

            this.serverSocket = null;
            this.serverClosed();
         }

      }
   }

   public void sendToAllClients(Object msg) {
      Thread[] clientThreadList = this.getClientConnections();

      for(int i = 0; i < clientThreadList.length; ++i) {
         try {
            ((ConnectionToClient)clientThreadList[i]).sendToClient(msg);
         } catch (Exception var5) {
         }
      }

   }

   public final boolean isListening() {
      return this.connectionListener != null;
   }

   public final synchronized Thread[] getClientConnections() {
      Thread[] clientThreadList = new Thread[this.clientThreadGroup.activeCount()];
      this.clientThreadGroup.enumerate(clientThreadList);
      return clientThreadList;
   }

   public final int getNumberOfClients() {
      return this.clientThreadGroup.activeCount();
   }

   public final int getPort() {
      return this.port;
   }

   public final void setPort(int port) {
      this.port = port;
   }

   public final void setTimeout(int timeout) {
      this.timeout = timeout;
   }

   public final void setBacklog(int backlog) {
      this.backlog = backlog;
   }

   public final void run() {
      this.serverStarted();

      try {
         while(!this.readyToStop) {
            try {
               Socket clientSocket = this.serverSocket.accept();
               synchronized(this) {
                  new ConnectionToClient(this.clientThreadGroup, clientSocket, this);
               }
            } catch (InterruptedIOException var10) {
            }
         }

         this.serverStopped();
      } catch (IOException var11) {
         if (!this.readyToStop) {
            this.listeningException(var11);
         } else {
            this.serverStopped();
         }
      } finally {
         this.readyToStop = true;
         this.connectionListener = null;
      }

   }

   protected void clientConnected(ConnectionToClient client) {
   }

   protected synchronized void clientDisconnected(ConnectionToClient client) {
   }

   protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
   }

   protected void listeningException(Throwable exception) {
   }

   protected void serverStarted() {
   }

   protected void serverStopped() {
   }

   protected void serverClosed() {
   }

   protected abstract void handleMessageFromClient(Object var1, ConnectionToClient var2);

   final synchronized void receiveMessageFromClient(Object msg, ConnectionToClient client) {
      this.handleMessageFromClient(msg, client);
   }
}
