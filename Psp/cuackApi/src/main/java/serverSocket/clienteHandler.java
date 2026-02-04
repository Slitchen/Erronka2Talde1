package serverSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import modelo.Centro;
import modelo.Ciclos;
import modelo.Horarios;
import modelo.Reuniones;
import modelo.Users;

public class clienteHandler implements Runnable {

	private Socket socket;
	private Metodos metodos = new Metodos();

	public clienteHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (DataInputStream sarrera = new DataInputStream(socket.getInputStream());
				DataOutputStream irteera = new DataOutputStream(socket.getOutputStream())) {
			irteera.flush();

			String komandoa = sarrera.readUTF();
			Gson gson = new Gson();

			switch (komandoa) {
			case "LOGIN":
				String erabiltzailea = sarrera.readUTF();
				String pasahitza = sarrera.readUTF();

				// 2. Galdeketa egin (Metodo euskaratua erabiliz)
				Users erabiltzaileAurkitua = metodos.erabiltzaileaBilatuDBan(erabiltzailea, pasahitza);

				if (erabiltzaileAurkitua != null) {
					String jsonErantzuna = gson.toJson(erabiltzaileAurkitua);
					irteera.writeUTF(jsonErantzuna); // JSONa bidali
				} else {
					irteera.writeUTF("null"); // "null" testu gisa bidali
				}
				break;

			case "GET_IKASLEAK":
				// 1. Bezeroak bidaltzen duen irakasle IDa irakurri
				String irakasleIdStr = sarrera.readUTF();
				int irakasleId = Integer.parseInt(irakasleIdStr);

				// 2. Kontsultatu eta Garbitu
				ArrayList<Users> ikasleZerrenda = metodos.ikasleakGarbiLortu(irakasleId);

				// 3. JSON bidali
				String jsonIkasleak = gson.toJson(ikasleZerrenda);
				irteera.writeUTF(jsonIkasleak);
				break;
			case "GET_ZIKLOAK":
				String irakasleIdStr2 = sarrera.readUTF();
				int irakasleId2 = Integer.parseInt(irakasleIdStr2);

				ArrayList<Ciclos> zikloak = metodos.zikloakGarbiLortu(irakasleId2);

				String jsonZikloak = gson.toJson(zikloak);
				irteera.writeUTF(jsonZikloak);
				break;
			case "GET_KURTSOAK":
				String irakasleIdStr3 = sarrera.readUTF();
				int irakasleId3 = Integer.parseInt(irakasleIdStr3);

				ArrayList<Byte> mailak = metodos.mailakGarbiLortu(irakasleId3);

				String jsonMailak = gson.toJson(mailak);
				irteera.writeUTF(jsonMailak);
				break;
			case "GET_ORDUTEGIAK":
				String irakasleIdStr4 = sarrera.readUTF();
				int irakasleId4 = Integer.parseInt(irakasleIdStr4);

				ArrayList<Horarios> ordutegia = metodos.ordutegiakGarbiLortu(irakasleId4);

				String jsonOrdutegiak = gson.toJson(ordutegia);
				irteera.writeUTF(jsonOrdutegiak);
				break;
			case "GET_BATZARRAK":
				String irakasleIdStr5 = sarrera.readUTF();
				int irakasleId5 = Integer.parseInt(irakasleIdStr5);

				ArrayList<Reuniones> bilkura = metodos.bilkurakGarbiLortu(irakasleId5);

				String jsonBilkurak = gson.toJson(bilkura);
				irteera.writeUTF(jsonBilkurak);
				break;
			case "GET_IRAKASLEAK":
				String motaId = sarrera.readUTF();
				int motaIdInt = Integer.parseInt(motaId);

				ArrayList<Users> irakasleak = metodos.irakasleakGarbiLortu(motaIdInt);

				String jsonIrakasleak = gson.toJson(irakasleak);
				irteera.writeUTF(jsonIrakasleak);
				break;
			case "GET_ALL_IKASLEAK":
				String motaId2 = sarrera.readUTF();
				int motaIdInt2 = Integer.parseInt(motaId2);

				String jsonIkasleGuztiak = gson.toJson(metodos.ikasleGuztiakGarbiLortu(motaIdInt2));
				irteera.writeUTF(jsonIkasleGuztiak);
				break;
			case "GET_IKASTETXEAK":
				try {
					// 1. Objektuen zerrenda lortu
					ArrayList<Centro> bidaltzekoIkastetxeak = metodos.ikastetxeakKargatuJSONetik();

					// 2. ObjectOutputStream sortu socket gainean
					ObjectOutputStream irteeraObjektua = new ObjectOutputStream(socket.getOutputStream());

					// 3. Zerrenda osoa bidali
					irteeraObjektua.writeObject(bidaltzekoIkastetxeak);
					irteeraObjektua.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "POST_BATZARRAK":
				try {
					// 1. Bezeroaren objektua jaso
					ObjectInputStream sarreraObjektua = new ObjectInputStream(socket.getInputStream());
					Reuniones sarrerakoBilkura = (Reuniones) sarreraObjektua.readObject();

					metodos.bilkuraGehitu(sarrerakoBilkura);

				} catch (ClassNotFoundException e) {
					System.err.println("Errorea: Reuniones klasea ez da zerbitzarian existitzen.");
				} catch (IOException e) {
					System.err.println("Sare errorea: " + e.getMessage());
				}
				break;
			case "PUT_BATZARRAK":
				try {
					// 1. Bezeroaren objektua jaso
					ObjectInputStream sarreraObjektua = new ObjectInputStream(socket.getInputStream());
					Reuniones sarrerakoBilkura = (Reuniones) sarreraObjektua.readObject();

					metodos.bilkuraAldatu(sarrerakoBilkura);

				} catch (ClassNotFoundException e) {
					System.err.println("Errorea: Reuniones klasea ez da zerbitzarian existitzen.");
				} catch (IOException e) {
					System.err.println("Sare errorea: " + e.getMessage());
				}
				break;
			case "GET_IKASLEAK_KURTSOA":
				String mailaStr = sarrera.readUTF();
				String motaId3 = sarrera.readUTF();

				int motaIdInt3 = Integer.parseInt(motaId3);
				int mailaInt = Integer.parseInt(mailaStr);

				// 2. Kontsultatu eta Garbitu
				ArrayList<Users> ikasleZerrenda2 = metodos.ikasleakGarbiLortuMailaka((byte) mailaInt, motaIdInt3);

				// 3. JSON bidali
				String jsonIkasleak2 = gson.toJson(ikasleZerrenda2);
				irteera.writeUTF(jsonIkasleak2);
				break;
			case "GET_IKASLEAK_ZIKLOA":
				String zikloaStr = sarrera.readUTF();
				String motaId4 = sarrera.readUTF();

				int motaIdInt4 = Integer.parseInt(motaId4);
				int zikloaInt = Integer.parseInt(zikloaStr);

				// 2. Kontsultatu eta Garbitu
				ArrayList<Users> ikasleZerrenda3 = metodos.ikasleakGarbiLortuZikloka(zikloaInt, motaIdInt4);

				// 3. JSON bidali
				String jsonIkasleak3 = gson.toJson(ikasleZerrenda3);
				irteera.writeUTF(jsonIkasleak3);
				break;

			case "GET_IRAKASLEAK_ABIZENA":
				String abizena = sarrera.readUTF();

				// 2. Kontsultatu eta Garbitu
				ArrayList<Users> irakasleZerrenda3 = metodos.irakasleakAbizenazLortu(abizena);

				// 3. JSON bidali
				String jsonIrakasleak3 = gson.toJson(irakasleZerrenda3);
				irteera.writeUTF(jsonIrakasleak3);
				break;
			case "GET_IRAKASLEAK_IZENAK":
				String izena = sarrera.readUTF();

				// 2. Kontsultatu eta Garbitu
				ArrayList<Users> irakasleZerrenda4 = metodos.irakasleakIzenazLortu(izena);

				// 3. JSON bidali
				String jsonIrakasleak4 = gson.toJson(irakasleZerrenda4);
				irteera.writeUTF(jsonIrakasleak4);
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}