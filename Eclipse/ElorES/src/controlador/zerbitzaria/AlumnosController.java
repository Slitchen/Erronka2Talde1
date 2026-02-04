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
import modelo.Ciclos;
import modelo.Users;

public class AlumnosController {

	public ArrayList<Users> ikasleIkerketa(Integer idIrakaslea) {

		ArrayList<Users> ikasleak = new ArrayList<>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irtera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irtera.writeUTF("GET_IKASLEAK");

			// 2. Enviar ID del profesor
			irtera.writeUTF(String.valueOf(idIrakaslea));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type listaMota = new TypeToken<ArrayList<Users>>() {
			}.getType();

			ikasleak = gson.fromJson(jsonJasota, listaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ikasleak;
	}
	
	public ArrayList<Users> ikasleAllIkerketa(Integer idTipo) {

		ArrayList<Users> irakasleak = new ArrayList<>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irtera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irtera.writeUTF("GET_ALL_IKASLEAK");

			// 2. Enviar ID del profesor
			irtera.writeUTF(String.valueOf(idTipo));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type listaMota = new TypeToken<ArrayList<Users>>() {
			}.getType();

			irakasleak = gson.fromJson(jsonJasota, listaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return irakasleak;
	}

	public ArrayList<Ciclos> irakasleZikloak(Integer idIrakaslea) {
		
		ArrayList<Ciclos> ciclos = new ArrayList<Ciclos>();
		
		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_ZIKLOAK");

			// 2. Enviar ID del profesor
			irteera.writeUTF(String.valueOf(idIrakaslea));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type listaMota = new TypeToken<ArrayList<Ciclos>>() {
			}.getType();

			ciclos = gson.fromJson(jsonJasota, listaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ciclos;

	}
	
	public ArrayList<Byte> irakasleModulos(Integer idIrakaslea){

		ArrayList<Byte> cursos = new ArrayList<Byte>();
		
		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_KURTSOAK");

			// 2. Enviar ID del profesor
			irteera.writeUTF(String.valueOf(idIrakaslea));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type listaMota = new TypeToken<ArrayList<Byte>>() {
			}.getType();

			cursos = gson.fromJson(jsonJasota, listaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    return cursos;
	}

	public ArrayList<Users> ikasleKurtsoak(Integer kurtsoa, Integer idMota ) {

		ArrayList<Users> irakasleak = new ArrayList<>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_IKASLEAK_KURTSOA");
			
			irteera.writeUTF(String.valueOf(kurtsoa));
			irteera.writeUTF(String.valueOf(idMota));

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
	
	public ArrayList<Users> ikasleZikloa(Integer zikloa, Integer idMota ) {

		ArrayList<Users> ikasleak = new ArrayList<>();

		try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {

			// 1. Enviar COMANDO
			irteera.writeUTF("GET_IKASLEAK_ZIKLOA");
			
			irteera.writeUTF(String.valueOf(zikloa));
			irteera.writeUTF(String.valueOf(idMota));

			// 3. Recibir JSON
			String jsonJasota = sarrera.readUTF();

			// 4. Convertir JSON a ArrayList<Users>
			Gson gson = new Gson();

			// "TypeToken" sirve para decirle a Gson: "Oye, esto es un ArrayList de Users"
			Type zerrendaMota = new TypeToken<ArrayList<Users>>() {
			}.getType();

			ikasleak = gson.fromJson(jsonJasota, zerrendaMota);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ikasleak;
	}

}
