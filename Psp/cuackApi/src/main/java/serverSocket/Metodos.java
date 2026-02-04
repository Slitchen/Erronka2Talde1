package serverSocket;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import edu.controller.HibernateUtil;
import modelo.Centro;
import modelo.Ciclos;
import modelo.Horarios;
import modelo.Modulos;
import modelo.Reuniones;
import modelo.Tipos;
import modelo.Users;

public class Metodos {

	public void bilkuraGehitu(Reuniones bilkura) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = saioa.beginTransaction();
			saioa.save(bilkura);
			tx.commit();
			System.out.println("igoera zuzena" + " " + bilkura.toString());
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			saioa.close();
		}
	}
	
	public void bilkuraAldatu(Reuniones bilkura) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
	
		try {
			tx = saioa.beginTransaction();
			Reuniones bil = saioa.get(Reuniones.class, (Integer) bilkura.getIdReunion());
			bil.setEstado(bilkura.getEstado());
			saioa.save(bil);
			tx.commit();
			System.out.println("igoera zuzena" + " " + bilkura.toString());
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			saioa.close();
		}
	}


	public ArrayList<Centro> ikastetxeakKargatuJSONetik() {
		ArrayList<Centro> ikastetxeZerrenda = new ArrayList<>();
		Gson gson = new Gson();

		// Fitxategia dagoen bidea
		String fitxategiBidea = "data/centros.json";

		try (FileReader irakurlea = new FileReader(fitxategiBidea)) {
			// 1. Fitxategi osoa objektu gisa analizatu
			JsonObject erroa = JsonParser.parseReader(irakurlea).getAsJsonObject();

			// 2. "CENTROS" barruan dagoen array-a lortu
			JsonArray jsonArray = erroa.getAsJsonArray("CENTROS");

			// 3. GSON array hori Java ArrayList objektuetan bihurtu
			Type mota = new TypeToken<ArrayList<Centro>>() {
			}.getType();
			ikastetxeZerrenda = gson.fromJson(jsonArray, mota);

			System.out.println("Ikastetxeak ongi kargatu dira: " + ikastetxeZerrenda.size());
		} catch (Exception e) {
			System.err.println("Errorea CENTROS prozesatzean: " + e.getMessage());
		}

		return ikastetxeZerrenda;
	}

	public ArrayList<Reuniones> bilkurakGarbiLortu(Integer irakasleId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Reuniones> bilkuraGarbiak = new ArrayList<>();
		String hql = "FROM Reuniones r " + "WHERE r.usersByProfesorId.id = :id ";

		Query<Reuniones> galdeketa = saioa.createQuery(hql, Reuniones.class);
		galdeketa.setParameter("id", irakasleId);

		List<?> errenkadak = galdeketa.list();

		for (Object objektua : errenkadak) {
			Reuniones bilkuraLikitsa = (Reuniones) objektua;

			Reuniones bilkuraGarbia = new Reuniones();

			bilkuraGarbia.setIdReunion(bilkuraLikitsa.getIdReunion());

			if (bilkuraLikitsa.getUsersByAlumnoId() != null) {
				Users ikasleOriginala = bilkuraLikitsa.getUsersByAlumnoId();
				Users ikasleGarbia = new Users();

				ikasleGarbia.setId(ikasleOriginala.getId());
				bilkuraGarbia.setUsersByAlumnoId(ikasleGarbia);
			}

			if (bilkuraLikitsa.getUsersByProfesorId() != null) {
				Users irakasleOriginala = bilkuraLikitsa.getUsersByProfesorId();
				Users irakasleGarbia = new Users();

				irakasleGarbia.setId(irakasleOriginala.getId());
				bilkuraGarbia.setUsersByProfesorId(irakasleGarbia);
			}

			bilkuraGarbia.setEstado(bilkuraLikitsa.getEstado());
			bilkuraGarbia.setIdCentro(bilkuraLikitsa.getIdCentro());
			bilkuraGarbia.setTitulo(bilkuraLikitsa.getTitulo());
			bilkuraGarbia.setAsunto(bilkuraLikitsa.getAsunto());
			bilkuraGarbia.setAula(bilkuraLikitsa.getAula());
			bilkuraGarbia.setFecha(bilkuraLikitsa.getFecha());

			bilkuraGarbiak.add(bilkuraGarbia);
		}

		return bilkuraGarbiak;
	}

	public ArrayList<Horarios> ordutegiakGarbiLortu(Integer irakasleId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Horarios> ordutegiGarbiak = new ArrayList<>();

		String hql = "SELECT h.id, h.dia, h.hora, m.nombre, h.aula, m.curso, ci.nombre, m.id, ci.id, u.nombre "
				+ "FROM Horarios h " + "JOIN Modulos m ON h.modulos.id = m.id "
				+ "JOIN Ciclos ci ON m.ciclos.id = ci.id " + "JOIN Users u ON h.users.id = u.id "
				+ "WHERE h.users.id = :id "
				+ "ORDER BY FIELD(h.dia, 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'), h.hora ASC ";
		try {
			Query<Object[]> galdeketa = saioa.createQuery(hql, Object[].class);
			galdeketa.setParameter("id", irakasleId);

			List<Object[]> errenkadak = galdeketa.list();

			for (Object[] errenkada : errenkadak) {

				Horarios ordutegiGarbia = new Horarios();
				ordutegiGarbia.setId((Integer) errenkada[0]);
				ordutegiGarbia.setDia((String) errenkada[1]);
				ordutegiGarbia.setHora((byte) errenkada[2]);
				ordutegiGarbia.setAula((String) errenkada[4]);

				Modulos moduloGarbia = new Modulos();
				moduloGarbia.setNombre((String) errenkada[3]);
				moduloGarbia.setCurso((byte) errenkada[5]);
				moduloGarbia.setId((Integer) errenkada[7]);

				Ciclos zikloGarbia = new Ciclos();
				zikloGarbia.setNombre((String) errenkada[6]);
				zikloGarbia.setId((Integer) errenkada[8]);

				Users erabiltzaileGarbia = new Users();
				erabiltzaileGarbia.setNombre((String) errenkada[9]);

				moduloGarbia.setCiclos(zikloGarbia);
				ordutegiGarbia.setUsers(erabiltzaileGarbia);
				ordutegiGarbia.setModulos(moduloGarbia);

				ordutegiGarbiak.add(ordutegiGarbia);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}

		return ordutegiGarbiak;
	}

	public ArrayList<Byte> mailakGarbiLortu(Integer irakasleId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		String hql = "SELECT DISTINCT mo.curso " + "FROM Horarios h " + "JOIN h.modulos mo "
				+ "WHERE h.users.id = :id ";
		Query q = saioa.createQuery(hql);
		q.setParameter("id", irakasleId);

		List<Byte> mailak = q.list(); 

		return new ArrayList<>(mailak);
	}

	public ArrayList<Ciclos> zikloakGarbiLortu(Integer irakasleId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Ciclos> zikloGarbiak = new ArrayList<>();

		try {

			String hql = "SELECT DISTINCT ci " + "FROM Users u " + "JOIN Horarios h ON u.id = h.users.id "
					+ "JOIN Modulos mo ON h.modulos.id = mo.id " + "JOIN Ciclos ci ON mo.ciclos.id = ci.id "
					+ "WHERE u.id = :id";
			Query q = saioa.createQuery(hql);
			q.setParameter("id", irakasleId);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Ciclos zikloLikitsa = (Ciclos) objektua;

					Ciclos zikloGarbia = new Ciclos();

					zikloGarbia.setId(zikloLikitsa.getId());
					zikloGarbia.setNombre(zikloLikitsa.getNombre());

					zikloGarbiak.add(zikloGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return zikloGarbiak;
	}

	public ArrayList<Users> irakasleakGarbiLortu(Integer motaId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> irakasleGarbiak = new ArrayList<>();

		try {
			String hql = "FROM Users a " + "WHERE a.tipos.id = :id ";

			Query q = saioa.createQuery(hql);
			q.setParameter("id", motaId);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Users irakasleLikitsa = (Users) objektua;

					Users irakasleGarbia = new Users();

					irakasleGarbia.setId(irakasleLikitsa.getId());

					if (irakasleLikitsa.getTipos() != null) {
						Tipos motaOriginala = irakasleLikitsa.getTipos();
						Tipos motaGarbia = new Tipos();

						motaGarbia.setId(motaOriginala.getId());
						motaGarbia.setName(motaOriginala.getName()); 

						irakasleGarbia.setTipos(motaGarbia);
					}

					irakasleGarbia.setEmail(irakasleLikitsa.getEmail());
					irakasleGarbia.setUsername(irakasleLikitsa.getUsername());
					irakasleGarbia.setPassword(irakasleLikitsa.getPassword());
					irakasleGarbia.setNombre(irakasleLikitsa.getNombre());
					irakasleGarbia.setApellidos(irakasleLikitsa.getApellidos());
					irakasleGarbia.setDni(irakasleLikitsa.getDni());
					irakasleGarbia.setDireccion(irakasleLikitsa.getDireccion());
					irakasleGarbia.setTelefono1(irakasleLikitsa.getTelefono1());
					irakasleGarbia.setTelefono2(irakasleLikitsa.getTelefono2());
					irakasleGarbia.setArgazkiaUrl(irakasleLikitsa.getArgazkiaUrl());
					irakasleGarbia.setCreatedAt(irakasleLikitsa.getCreatedAt());
					irakasleGarbia.setUpdatedAt(irakasleLikitsa.getUpdatedAt());

					irakasleGarbiak.add(irakasleGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return irakasleGarbiak;
	}

	public ArrayList<Users> irakasleakAbizenazLortu(String abizena) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> irakasleGarbiak = new ArrayList<>();

		try {
			String hql = "FROM Users a " + "WHERE a.apellidos = :abizena ";

			Query q = saioa.createQuery(hql);
			q.setParameter("abizena", abizena);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Users irakasleLikitsa = (Users) objektua;

					Users irakasleGarbia = new Users();

					irakasleGarbia.setId(irakasleLikitsa.getId());

					if (irakasleLikitsa.getTipos() != null) {
						Tipos motaOriginala = irakasleLikitsa.getTipos();
						Tipos motaGarbia = new Tipos();

						motaGarbia.setId(motaOriginala.getId());
						motaGarbia.setName(motaOriginala.getName()); 

						irakasleGarbia.setTipos(motaGarbia);
					}

					irakasleGarbia.setEmail(irakasleLikitsa.getEmail());
					irakasleGarbia.setUsername(irakasleLikitsa.getUsername());
					irakasleGarbia.setPassword(irakasleLikitsa.getPassword());
					irakasleGarbia.setNombre(irakasleLikitsa.getNombre());
					irakasleGarbia.setApellidos(irakasleLikitsa.getApellidos());
					irakasleGarbia.setDni(irakasleLikitsa.getDni());
					irakasleGarbia.setDireccion(irakasleLikitsa.getDireccion());
					irakasleGarbia.setTelefono1(irakasleLikitsa.getTelefono1());
					irakasleGarbia.setTelefono2(irakasleLikitsa.getTelefono2());
					irakasleGarbia.setArgazkiaUrl(irakasleLikitsa.getArgazkiaUrl());
					irakasleGarbia.setCreatedAt(irakasleLikitsa.getCreatedAt());
					irakasleGarbia.setUpdatedAt(irakasleLikitsa.getUpdatedAt());

					irakasleGarbiak.add(irakasleGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return irakasleGarbiak;
	}
	
	public ArrayList<Users> irakasleakIzenazLortu(String izena) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> irakasleGarbiak = new ArrayList<>();
		
		try {
			String hql = "FROM Users a " + "WHERE a.nombre = :izena ";

			Query q = saioa.createQuery(hql);
			q.setParameter("izena", izena);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Users irakasleLikitsa = (Users) objektua;

					Users irakasleGarbia = new Users();

					irakasleGarbia.setId(irakasleLikitsa.getId());

					if (irakasleLikitsa.getTipos() != null) {
						Tipos motaOriginala = irakasleLikitsa.getTipos();
						Tipos motaGarbia = new Tipos();

						motaGarbia.setId(motaOriginala.getId());
						motaGarbia.setName(motaOriginala.getName()); 

						irakasleGarbia.setTipos(motaGarbia);
					}

					irakasleGarbia.setEmail(irakasleLikitsa.getEmail());
					irakasleGarbia.setUsername(irakasleLikitsa.getUsername());
					irakasleGarbia.setPassword(irakasleLikitsa.getPassword());
					irakasleGarbia.setNombre(irakasleLikitsa.getNombre());
					irakasleGarbia.setApellidos(irakasleLikitsa.getApellidos());
					irakasleGarbia.setDni(irakasleLikitsa.getDni());
					irakasleGarbia.setDireccion(irakasleLikitsa.getDireccion());
					irakasleGarbia.setTelefono1(irakasleLikitsa.getTelefono1());
					irakasleGarbia.setTelefono2(irakasleLikitsa.getTelefono2());
					irakasleGarbia.setArgazkiaUrl(irakasleLikitsa.getArgazkiaUrl());
					irakasleGarbia.setCreatedAt(irakasleLikitsa.getCreatedAt());
					irakasleGarbia.setUpdatedAt(irakasleLikitsa.getUpdatedAt());

					irakasleGarbiak.add(irakasleGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return irakasleGarbiak;
	}

	public ArrayList<Users> ikasleGuztiakGarbiLortu(Integer motaId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> ikasleGarbiak = new ArrayList<>();
		try {
			String hql = "FROM Users a " + "WHERE a.tipos.id = :id ";

			Query q = saioa.createQuery(hql);
			q.setParameter("id", motaId);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Users ikasleLikitsa = (Users) objektua;

					Users ikasleGarbia = new Users();

					ikasleGarbia.setId(ikasleLikitsa.getId());

					if (ikasleLikitsa.getTipos() != null) {
						Tipos motaOriginala = ikasleLikitsa.getTipos();
						Tipos motaGarbia = new Tipos();

						motaGarbia.setId(motaOriginala.getId());
						motaGarbia.setName(motaOriginala.getName()); 

						ikasleGarbia.setTipos(motaGarbia);
					}

					ikasleGarbia.setEmail(ikasleLikitsa.getEmail());
					ikasleGarbia.setUsername(ikasleLikitsa.getUsername());
					ikasleGarbia.setPassword(ikasleLikitsa.getPassword());
					ikasleGarbia.setNombre(ikasleLikitsa.getNombre());
					ikasleGarbia.setApellidos(ikasleLikitsa.getApellidos());
					ikasleGarbia.setDni(ikasleLikitsa.getDni());
					ikasleGarbia.setDireccion(ikasleLikitsa.getDireccion());
					ikasleGarbia.setTelefono1(ikasleLikitsa.getTelefono1());
					ikasleGarbia.setTelefono2(ikasleLikitsa.getTelefono2());
					ikasleGarbia.setArgazkiaUrl(ikasleLikitsa.getArgazkiaUrl());
					ikasleGarbia.setCreatedAt(ikasleLikitsa.getCreatedAt());
					ikasleGarbia.setUpdatedAt(ikasleLikitsa.getUpdatedAt());

					ikasleGarbiak.add(ikasleGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return ikasleGarbiak;
	}

	public ArrayList<Users> ikasleakGarbiLortuMailaka(byte maila, Integer irakasleId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> ikasleGarbiak = new ArrayList<>();

		try {

			String hql = "SELECT DISTINCT u " + "FROM Horarios h " + "JOIN h.modulos m " + "JOIN Matriculaciones mat "
					+ "ON mat.ciclos.id = m.ciclos.id " + "AND mat.curso = m.curso " + "JOIN mat.users u "
					+ "WHERE h.users.id = :id " + "AND mat.curso = :curso";

			Query q = saioa.createQuery(hql);
			q.setParameter("id", irakasleId);
			q.setParameter("curso", maila);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Users ikasleLikitsa = (Users) objektua;

					Users ikasleGarbia = new Users();

					ikasleGarbia.setId(ikasleLikitsa.getId());

					if (ikasleLikitsa.getTipos() != null) {
						Tipos motaOriginala = ikasleLikitsa.getTipos();
						Tipos motaGarbia = new Tipos();

						motaGarbia.setId(motaOriginala.getId());
						motaGarbia.setName(motaOriginala.getName()); 

						ikasleGarbia.setTipos(motaGarbia);
					}

					ikasleGarbia.setEmail(ikasleLikitsa.getEmail());
					ikasleGarbia.setUsername(ikasleLikitsa.getUsername());
					ikasleGarbia.setPassword(ikasleLikitsa.getPassword());
					ikasleGarbia.setNombre(ikasleLikitsa.getNombre());
					ikasleGarbia.setApellidos(ikasleLikitsa.getApellidos());
					ikasleGarbia.setDni(ikasleLikitsa.getDni());
					ikasleGarbia.setDireccion(ikasleLikitsa.getDireccion());
					ikasleGarbia.setTelefono1(ikasleLikitsa.getTelefono1());
					ikasleGarbia.setTelefono2(ikasleLikitsa.getTelefono2());
					ikasleGarbia.setArgazkiaUrl(ikasleLikitsa.getArgazkiaUrl());
					ikasleGarbia.setCreatedAt(ikasleLikitsa.getCreatedAt());
					ikasleGarbia.setUpdatedAt(ikasleLikitsa.getUpdatedAt());

					ikasleGarbiak.add(ikasleGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return ikasleGarbiak;
	}

	public ArrayList<Users> ikasleakGarbiLortuZikloka(Integer zikloa, Integer irakasleId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> ikasleGarbiak = new ArrayList<>();

		try {

			String hql = "SELECT DISTINCT u " + "FROM Horarios h " + "JOIN h.modulos m " + "JOIN Matriculaciones mat "
					+ "ON mat.ciclos.id = m.ciclos.id " + "JOIN mat.users u " + "WHERE h.users.id = :id "
					+ "AND mat.ciclos.id = :idCiclo";

			Query q = saioa.createQuery(hql);
			q.setParameter("id", irakasleId);
			q.setParameter("idCiclo", zikloa);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Users ikasleLikitsa = (Users) objektua;

					Users ikasleGarbia = new Users();

					ikasleGarbia.setId(ikasleLikitsa.getId());

					if (ikasleLikitsa.getTipos() != null) {
						Tipos motaOriginala = ikasleLikitsa.getTipos();
						Tipos motaGarbia = new Tipos();

						motaGarbia.setId(motaOriginala.getId());
						motaGarbia.setName(motaOriginala.getName()); 

						ikasleGarbia.setTipos(motaGarbia);
					}

					ikasleGarbia.setEmail(ikasleLikitsa.getEmail());
					ikasleGarbia.setUsername(ikasleLikitsa.getUsername());
					ikasleGarbia.setPassword(ikasleLikitsa.getPassword());
					ikasleGarbia.setNombre(ikasleLikitsa.getNombre());
					ikasleGarbia.setApellidos(ikasleLikitsa.getApellidos());
					ikasleGarbia.setDni(ikasleLikitsa.getDni());
					ikasleGarbia.setDireccion(ikasleLikitsa.getDireccion());
					ikasleGarbia.setTelefono1(ikasleLikitsa.getTelefono1());
					ikasleGarbia.setTelefono2(ikasleLikitsa.getTelefono2());
					ikasleGarbia.setArgazkiaUrl(ikasleLikitsa.getArgazkiaUrl());
					ikasleGarbia.setCreatedAt(ikasleLikitsa.getCreatedAt());
					ikasleGarbia.setUpdatedAt(ikasleLikitsa.getUpdatedAt());

					ikasleGarbiak.add(ikasleGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return ikasleGarbiak;
	}

	public ArrayList<Users> ikasleakGarbiLortu(Integer irakasleId) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> ikasleGarbiak = new ArrayList<>();

		try {
			String hql = "SELECT DISTINCT a " + "FROM Users a " + "JOIN Matriculaciones m ON a.id = m.users.id "
					+ "JOIN Ciclos c ON m.ciclos.id = c.id " + "JOIN Modulos mo ON m.ciclos.id = mo.ciclos.id "
					+ "JOIN Horarios h ON mo.id = h.modulos.id " + "WHERE h.users.id = :id";

			Query q = saioa.createQuery(hql);
			q.setParameter("id", irakasleId);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				for (Object objektua : errenkadak) {
					Users ikasleLikitsa = (Users) objektua;

					Users ikasleGarbia = new Users();

					ikasleGarbia.setId(ikasleLikitsa.getId());

					if (ikasleLikitsa.getTipos() != null) {
						Tipos motaOriginala = ikasleLikitsa.getTipos();
						Tipos motaGarbia = new Tipos();

						motaGarbia.setId(motaOriginala.getId());
						motaGarbia.setName(motaOriginala.getName()); 

						ikasleGarbia.setTipos(motaGarbia);
					}

					ikasleGarbia.setEmail(ikasleLikitsa.getEmail());
					ikasleGarbia.setUsername(ikasleLikitsa.getUsername());
					ikasleGarbia.setPassword(ikasleLikitsa.getPassword());
					ikasleGarbia.setNombre(ikasleLikitsa.getNombre());
					ikasleGarbia.setApellidos(ikasleLikitsa.getApellidos());
					ikasleGarbia.setDni(ikasleLikitsa.getDni());
					ikasleGarbia.setDireccion(ikasleLikitsa.getDireccion());
					ikasleGarbia.setTelefono1(ikasleLikitsa.getTelefono1());
					ikasleGarbia.setTelefono2(ikasleLikitsa.getTelefono2());
					ikasleGarbia.setArgazkiaUrl(ikasleLikitsa.getArgazkiaUrl());
					ikasleGarbia.setCreatedAt(ikasleLikitsa.getCreatedAt());
					ikasleGarbia.setUpdatedAt(ikasleLikitsa.getUpdatedAt());

					ikasleGarbiak.add(ikasleGarbia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}
		return ikasleGarbiak;
	}

	public Users erabiltzaileaBilatuDBan(String erabiltzailea, String pasahitza) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		Users erabiltzaileGarbia = null; 

		try {
			String hql = "from Users u where u.username = :erabiltzailea and u.password = :pass ";
			Query q = saioa.createQuery(hql);
			q.setParameter("erabiltzailea", erabiltzailea);
			q.setParameter("pass", pasahitza);

			List<?> errenkadak = q.list();

			if (!errenkadak.isEmpty()) {
				Users erabiltzaileHibernate = (Users) errenkadak.get(0);

				erabiltzaileGarbia = new Users();

				erabiltzaileGarbia.setId(erabiltzaileHibernate.getId());

				if (erabiltzaileHibernate.getTipos() != null) {
					Tipos motaOriginala = erabiltzaileHibernate.getTipos();
					Tipos motaGarbia = new Tipos();

					motaGarbia.setId(motaOriginala.getId());
					motaGarbia.setName(motaOriginala.getName()); 

					erabiltzaileGarbia.setTipos(motaGarbia);
				}

				erabiltzaileGarbia.setEmail(erabiltzaileHibernate.getEmail());
				erabiltzaileGarbia.setUsername(erabiltzaileHibernate.getUsername());
				erabiltzaileGarbia.setPassword(erabiltzaileHibernate.getPassword());
				erabiltzaileGarbia.setNombre(erabiltzaileHibernate.getNombre());
				erabiltzaileGarbia.setApellidos(erabiltzaileHibernate.getApellidos());
				erabiltzaileGarbia.setDni(erabiltzaileHibernate.getDni());
				erabiltzaileGarbia.setDireccion(erabiltzaileHibernate.getDireccion());
				erabiltzaileGarbia.setTelefono1(erabiltzaileHibernate.getTelefono1());
				erabiltzaileGarbia.setTelefono2(erabiltzaileHibernate.getTelefono2());
				erabiltzaileGarbia.setArgazkiaUrl(erabiltzaileHibernate.getArgazkiaUrl());
				erabiltzaileGarbia.setCreatedAt(erabiltzaileHibernate.getCreatedAt());
				erabiltzaileGarbia.setUpdatedAt(erabiltzaileHibernate.getUpdatedAt());

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saioa.close();
		}

		return erabiltzaileGarbia;
	}
}