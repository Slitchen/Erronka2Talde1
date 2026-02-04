package controlador.zerbitzaria;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import controlador.Funtzioak;
import modelo.Reuniones;

public class ReunionesController {
	public ArrayList<Reuniones> irakasleReuniones(Integer idIrakaslea) {

		ArrayList<Reuniones> batzarrak = new ArrayList<Reuniones>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_BATZARRAK");

			// 2. Enviar ID del profesor
			irteera.writeUTF(String.valueOf(idIrakaslea));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type zerrendaMota = new TypeToken<ArrayList<Reuniones>>() {
			}.getType();

			batzarrak = gson.fromJson(jsonJasota, zerrendaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return batzarrak;

	}

	public void SortuBatzarrak(Reuniones reunion) {

	    // Solo metemos el Socket y el DataOutputStream en el try inicial
	    try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
	         DataOutputStream irteera = new DataOutputStream(socket.getOutputStream())) {

	        // 1. Enviar COMANDO y asegurar que llegue con flush
	        irteera.writeUTF("POST_BATZARRAK");
	        irteera.flush(); 

	       
	        ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
	        outObjeto.writeObject(reunion);
	        outObjeto.flush();

	       
	       

	    } catch (IOException e) {
	        System.err.println("Error en el cliente: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public void ModifikatuBatzarra(Reuniones reunion) {

	    // Solo metemos el Socket y el DataOutputStream en el try inicial
	    try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
	         DataOutputStream irteera = new DataOutputStream(socket.getOutputStream())) {

	        // 1. Enviar COMANDO y asegurar que llegue con flush
	        irteera.writeUTF("PUT_BATZARRAK");
	        irteera.flush(); 

	       
	        ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
	        outObjeto.writeObject(reunion);
	        outObjeto.flush();

	       
	       

	    } catch (IOException e) {
	        System.err.println("Error en el cliente: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

}
