package edu.controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.controller.dto.FotoDTO;

import modelo.Users;

@RestController
public class UsersController {

	@GetMapping("/usersAll")
	public String erabiltzaileGuztiak() {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		Query<Users> galdeketa = saioa.createQuery("SELECT u from Users u JOIN FETCH u.tipos", Users.class);
		List<Users> erabiltzaileak = galdeketa.list();

		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		String erabiltzaileakJson = gson.toJson(erabiltzaileak);

		return erabiltzaileakJson;
	}

	@GetMapping("/login/{username}/{password}")
	public String erabiltzaileenLogin(@PathVariable String username, @PathVariable String password) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "SELECT u FROM Users u JOIN FETCH u.tipos WHERE u.username = :username AND u.password = :password";
			Query<Users> galdeketa = saioa.createQuery(hql, Users.class);
			galdeketa.setParameter("username", username);
			galdeketa.setParameter("password", password);

			Users erabiltzailea = galdeketa.uniqueResult();
			
			Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
			return gson.toJson(erabiltzailea);
			

		} catch (Exception e) {
			e.printStackTrace(); 
			return "Errorea: " + e.getMessage();
		} finally {
			saioa.close();

		}

	}

	@PutMapping("/updateUserFoto/{id}")
	public ResponseEntity<Users> argazkiaEguneratu(
	        @PathVariable("id") Integer id,
	        @RequestBody FotoDTO dto) {

	    Session saioa = HibernateUtil.getSessionFactory().openSession();
	    Transaction tx = null;

	    try {
	        tx = saioa.beginTransaction();

	        // 1️⃣ Erabiltzailea ID bidez bilatu
	        Users erabiltzailea = saioa.get(Users.class, id);
	        if (erabiltzailea == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        // 2️⃣ Argazkiaren URL-a eguneratu
	        erabiltzailea.setArgazkiaUrl(dto.getUrlFoto());

	        // 3️⃣ Aldaketak gorde
	        saioa.update(erabiltzailea);
	        tx.commit();

	        return ResponseEntity.ok(erabiltzailea);

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        saioa.close();
	    }
	}

}