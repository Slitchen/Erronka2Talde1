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
import modelo.Users;

public class ProfesorController {
	public ArrayList<Users> irakasleIkerketa(Integer idTipo) {

		ArrayList<Users> irakasleak = new ArrayList<>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_IRAKASLEAK");

			// 2. Enviar ID del profesor
			irteera.writeUTF(String.valueOf(idTipo));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type zerrendaMota = new TypeToken<ArrayList<Users>>() {
			}.getType();

			irakasleak = gson.fromJson(jsonJasota, zerrendaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return irakasleak;
	}

	public ArrayList<Users> irakasleIkerketaAbizena(String abizena) {

		ArrayList<Users> irakasleak = new ArrayList<>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_IRAKASLEAK_ABIZENA");

			// 2. Enviar ID del profesor
			irteera.writeUTF(abizena);

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type zerrendaMota = new TypeToken<ArrayList<Users>>() {
			}.getType();

			irakasleak = gson.fromJson(jsonJasota, zerrendaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return irakasleak;
	}

	public ArrayList<Users> irakasleIkerketaIzena(String izena) {

		ArrayList<Users> irakasleak = new ArrayList<>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {
			
			irteera.writeUTF("GET_IRAKASLEAK_IZENAK");

			irteera.writeUTF(izena);

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type zerrendaMota = new TypeToken<ArrayList<Users>>() {
			}.getType();

			irakasleak = gson.fromJson(jsonJasota, zerrendaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return irakasleak;
	}
}
