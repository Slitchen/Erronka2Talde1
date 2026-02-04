package serverSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket {
	 public final int PORTUA = 12345;  // Puerto en el que escuchará el servidor

	    public void hasiera () {
	        try (ServerSocket serverSocket = new ServerSocket(PORTUA)) {
	        	System.out.println("Zerbitzaria entzuten " + PORTUA + " portuan");
	           
	            while (true) {
	                Socket clienteSocket = serverSocket.accept();
	                System.out.println("Bezero konektatuta: " + clienteSocket.getInetAddress());
	               
	                clienteHandler handler = new clienteHandler(clienteSocket);
	                Thread hilo = new Thread(handler);
	                hilo.start(); // Inicia el hilo
	               	               
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
