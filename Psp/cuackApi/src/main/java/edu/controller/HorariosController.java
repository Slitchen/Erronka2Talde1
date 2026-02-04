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

import edu.controller.dto.HorarioDTO;

@RestController
public class HorariosController {

	@GetMapping("/horarioAlumno/{id}")
	public String ikasleOrdutegiaLortuJoin(@PathVariable Long id) { 
		Session saioa = HibernateUtil.getSessionFactory().openSession();

		String hql = "SELECT h.dia, h.hora, MAX(m.nombre), MAX(uProfe.nombre) " + "FROM Users uAlumn "
				+ "JOIN Matriculaciones mat " + "JOIN Modulos m on mat.ciclos.id = m.ciclos.id AND mat.curso = m.curso "
				+ "JOIN Horarios h ON m.id = h.modulos.id " + "JOIN Users uProfe ON h.users.id = uProfe.id "
				+ "WHERE uAlumn.id = :id " + "GROUP BY h.dia, h.hora " + "ORDER BY " + "  CASE h.dia "
				+ "    WHEN 'LUNES' THEN 1 " + "    WHEN 'MARTES' THEN 2 " + "    WHEN 'MIERCOLES' THEN 3 "
				+ "    WHEN 'JUEVES' THEN 4 " + "    WHEN 'VIERNES' THEN 5 " + "    ELSE 6 END, " + "  h.hora ASC";

		Query galdeketa = saioa.createQuery(hql);
		galdeketa.setParameter("id", id);

		List<Object[]> errenkadak = galdeketa.list();

		List<HorarioDTO> ordutegiak = new ArrayList<>();
		for (Object[] errenkada : errenkadak) {
			String eguna = errenkada[0] != null ? errenkada[0].toString() : null;
			Number orduaZenbakia = (Number) errenkada[1];
			String modulua = errenkada[2] != null ? errenkada[2].toString() : null;
			String irakaslea = errenkada[3] != null ? errenkada[3].toString() : null;
			int ordua = orduaZenbakia != null ? orduaZenbakia.intValue() : 0;
			ordutegiak.add(new HorarioDTO(irakaslea, modulua, eguna, ordua));
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(ordutegiak);
	}

	@GetMapping("/horarioProfesor/{id}")
	public String irakasleOrdutegiaLortuJoin(@PathVariable Long id) { 
		Session saioa = HibernateUtil.getSessionFactory().openSession();
		String hql = "SELECT h.dia, h.hora, m.nombre, h.aula " + "FROM Horarios h "
				+ "JOIN Modulos m ON h.modulos.id = m.id " + "WHERE h.users.id = :id "
				+ "ORDER BY FIELD(h.dia, 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'), h.hora ASC ";
		
		Query galdeketa = saioa.createQuery(hql);
		galdeketa.setParameter("id", id);

		List<Object[]> errenkadak = galdeketa.list();

		List<HorarioDTO> ordutegiak = new ArrayList<>();
		for (Object[] errenkada : errenkadak) {
			String eguna = errenkada[0] != null ? errenkada[0].toString() : null;
			Number orduaZenbakia = (Number) errenkada[1];
			String modulua = errenkada[2] != null ? errenkada[2].toString() : null;
			String gela = errenkada[3] != null ? errenkada[3].toString() : null;
			int ordua = orduaZenbakia != null ? orduaZenbakia.intValue() : 0;
			ordutegiak.add(new HorarioDTO(gela, modulua, eguna, ordua));
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(ordutegiak);
	}

}