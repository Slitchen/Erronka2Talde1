package controlador.zerbitzaria;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JLabel;

import com.google.gson.Gson;

import controlador.Funtzioak;
import modelo.Users;
import modelo.enumerados.TiposEnum;

import java.security.MessageDigest;
import java.util.Base64;

public class UsersController {

	public Users loginIkerketa(String erabiltzailea, String pasahitza, JLabel label) {
	  
	    Users usr = null;

	    try (Socket socket = new Socket(Funtzioak.host, Funtzioak.puerto);
	    		DataOutputStream irteera = new DataOutputStream(socket.getOutputStream());
				DataInputStream sarrera = new DataInputStream(socket.getInputStream())) {
	    	
	    	irteera.writeUTF("LOGIN");

	        // 1. Enviar credenciales al servidor
	    	
	    	
	    	String pasahitzaEnkriptatuta = enkriptatzailea(pasahitza);
	    	String erabiltzaileEnkriptatuta = enkriptatzailea(erabiltzailea);
	    	
	    	System.err.println(pasahitzaEnkriptatuta);
	    	
	    	System.err.println(erabiltzaileEnkriptatuta);
	    	
	    	
	    	irteera.writeUTF(erabiltzaileEnkriptatuta);
	    	irteera.writeUTF(pasahitzaEnkriptatuta);

	        // 2. Recibir la respuesta (JSON)
	        String jsonJasota = sarrera.readUTF();

	        // 3. Procesar respuesta
	        if (!"null".equals(jsonJasota)) {
	            // Convertir JSON string de vuelta a objeto Java
	            Gson gson = new Gson();
	            usr = gson.fromJson(jsonJasota, Users.class);

	            // 4. Lógica de validación de tipo (Igual que tenías antes)
	            // Nota: Asegúrate de que usr.getTipos() vino lleno desde el servidor
	            TiposEnum tipo = TiposEnum.fromCodigo(usr.getTipos().getId());

	            if (tipo == TiposEnum.PROFESOR) {
	                return usr;
	            } else {
	                label.setText("Irakasle bat izan behar zara (Debes ser profesor)");
	                return null;
	            }
	        } else {
	            label.setText("Erabiltzailea edo pasahitza txarto daude!!");
	            return null;
	        }

	    } catch (IOException e) {
	        label.setText("Errorea zerbitzariarekin konektatzean (Error de conexión)");
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public String enkriptatzailea(String testua) {
	    try {
	        // 1. Usamos SHA-256 para mayor seguridad
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        
	        // 2. Convertimos la contraseña en bytes y generamos el hash
	        byte[] hashBytes = md.digest(testua.getBytes());
	        
	        // 3. Convertimos a Base64 para que sea un String seguro de guardar en BD
	        return Base64.getEncoder().encodeToString(hashBytes);
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error al encriptar", e);
	    }
	}

}
