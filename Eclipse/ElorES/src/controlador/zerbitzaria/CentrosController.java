package controlador.zerbitzaria;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import controlador.Funtzioak;
import modelo.Centro;

public class CentrosController {

	@SuppressWarnings("unchecked")
	public ArrayList<Centro> getCentrosJson() {

		ArrayList<Centro> ikastetxeak = new ArrayList<Centro>();
		
		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
		         DataOutputStream irteera = new DataOutputStream(socket.getOutputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_IKASTETXEAK");
			irteera.flush();
			//esto hay que ponerlo después del primer try por que si no se queda esperando al objeto y no va a seguir adelante
			//en los data no se queda bloqueado por que son flujos simples no inteligente como el object 
			
			try(ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream())){
				
				ikastetxeak = (ArrayList<Centro>) inObjeto.readObject();				
			}

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return ikastetxeak;
	}
	
}
