package edu.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.controller.dto.CicloDTO;

@RestController
public class MatriculacionController {

	@GetMapping("ikasleCiclo/{id}")
	public String ikasleZikloa(@PathVariable Integer id) {
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "SELECT ma.ciclos.id, ma.ciclos.nombre, ma.curso " +
		             "FROM Matriculaciones ma " +
		             "WHERE ma.users.id = :id ";

		Query<Object[]> galdeketa = saioa.createQuery(hql, Object[].class);
		galdeketa.setParameter("id", id);

		List<Object[]> errenkadak = galdeketa.list();
			
			List<CicloDTO> zikloak = new ArrayList<>();

			for (Object[] errenkada : errenkadak) {

			    Integer zikloId = errenkada[0] != null ? (Integer) errenkada[0] : null;
			    String zikloIzena = (String) errenkada[1];
			    byte maila = errenkada[2] != null ? ((Number) errenkada[2]).byteValue() : 0; 

			    zikloak.add(new CicloDTO(zikloId, zikloIzena, maila));
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
			return gson.toJson(zikloak);

		} catch (Exception e) {
			e.printStackTrace();
			return "Errorea: " + e.getMessage();
		} finally {
			saioa.close();
		}
	}

}