package controlador.zerbitzaria;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import controlador.Funtzioak;
import modelo.Horarios;

public class HorariosController {
	 
	public ArrayList<Horarios> irakasleOrdutegiak(Integer idIrakaslea) {

		ArrayList<Horarios> ordutegiak = new ArrayList<Horarios>();
		
		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_ORDUTEGIAK");

			// 2. Enviar ID del profesor
			irteera.writeUTF(String.valueOf(idIrakaslea));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type zerrendaMota = new TypeToken<ArrayList<Horarios>>() {
			}.getType();

			ordutegiak = gson.fromJson(jsonJasota, zerrendaMota);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ordutegiak;

	}
	
	
	
	
}
