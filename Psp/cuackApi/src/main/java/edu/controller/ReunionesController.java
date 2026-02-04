package edu.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.controller.dto.EstadoDTO;
import edu.controller.dto.ReunionDTO;
import modelo.Reuniones;
import modelo.Users;

@RestController
public class ReunionesController {

	@GetMapping("/reunionAlumno/{id}/{data}")
	public String ikasleBatzarrak(@PathVariable Integer id, @PathVariable String data) {

		Session saioa = HibernateUtil.getSessionFactory().openSession();
		try {
			// SQL bidezko LIKE simulatzeko '%' ikurrak gehitzen ditugu
			String hql = "FROM Reuniones r WHERE r.usersByAlumnoId.id = :id AND str(r.fecha) LIKE :fecha";
			Query<Reuniones> galdeketa = saioa.createQuery(hql, Reuniones.class);
			galdeketa.setParameter("id", id);
			galdeketa.setParameter("fecha", "%" + data + "%");

			List<Reuniones> batzarrak = galdeketa.list();

			List<ReunionDTO> dtoZerrenda = new ArrayList<>();
			for (Reuniones r : batzarrak) {
				ReunionDTO dto = new ReunionDTO();
				dto.idReunion = r.getIdReunion();
				dto.idAlumno = r.getUsersByAlumnoId().getId();
				dto.idProfesor = r.getUsersByProfesorId().getId();
				dto.estado = r.getEstado();
				dto.fecha = r.getFecha().toLocalDateTime().toString();
				dto.titulo = r.getTitulo();
				dto.asunto = r.getAsunto();
				dto.aula = r.getAula();
				dto.idCentro = r.getIdCentro();
				dtoZerrenda.add(dto);
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			return gson.toJson(dtoZerrenda);

		} catch (Exception e) {
			e.printStackTrace();
			return "Errorea: " + e.getMessage();
		} finally {
			saioa.close();
		}
	}

	@GetMapping("/reunionProfesor/{id}/{data}")
	public String irakasleBatzarrak(@PathVariable Integer id, @PathVariable String data) {

		Session saioa = HibernateUtil.getSessionFactory().openSession();
		try {

			String hql = "FROM Reuniones r WHERE r.usersByProfesorId.id = :id AND str(r.fecha) LIKE :fecha";
			Query<Reuniones> galdeketa = saioa.createQuery(hql, Reuniones.class);
			galdeketa.setParameter("id", id);
			galdeketa.setParameter("fecha", "%" + data + "%");

			List<Reuniones> batzarrak = galdeketa.list();


			List<ReunionDTO> dtoZerrenda = new ArrayList<>();
			for (Reuniones r : batzarrak) {
				ReunionDTO dto = new ReunionDTO();
				dto.idReunion = r.getIdReunion();
				dto.idAlumno = r.getUsersByAlumnoId().getId();
				dto.idProfesor = r.getUsersByProfesorId().getId();
				dto.estado = r.getEstado();
				dto.fecha = r.getFecha().toLocalDateTime().toString();
				if (r.getTitulo() != null) {
					dto.titulo = r.getTitulo().toString();
				}

				if (r.getAsunto() != null) {
					dto.asunto = r.getAsunto().toString();
				}

				dtoZerrenda.add(dto);
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			return gson.toJson(dtoZerrenda);

		} catch (Exception e) {
			e.printStackTrace();
			return "Errorea: " + e.getMessage();
		} finally {
			saioa.close();
		}
	}
	
	@PostMapping("/addReunion")
	public ResponseEntity<ReunionDTO> batzarraSortu(@RequestBody ReunionDTO dto) {
	    Session saioa = HibernateUtil.getSessionFactory().openSession();
	    Transaction tx = null;

	    try {
	        tx = saioa.beginTransaction();

	        if (dto.getIdAlumno() == null || dto.getIdProfesor() == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }

	        Users ikaslea = saioa.get(Users.class, dto.getIdAlumno());
	        Users irakaslea = saioa.get(Users.class, dto.getIdProfesor());

	        if (ikaslea == null || irakaslea == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }

	        Reuniones batzarra = new Reuniones();
	        batzarra.setUsersByAlumnoId(ikaslea);
	        batzarra.setUsersByProfesorId(irakaslea);
	        batzarra.setEstado(dto.getEstado());
	        batzarra.setTitulo(dto.getTitulo());
	        batzarra.setAsunto(dto.getAsunto());

	        Timestamp dataTimestamp;
	        try {
	            long millis = Long.parseLong(dto.getFecha());
	            dataTimestamp = new Timestamp(millis);
	        } catch (NumberFormatException e) {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            Date dataObje = sdf.parse(dto.getFecha());
	            dataTimestamp = new Timestamp(dataObje.getTime());
	        }
	        batzarra.setFecha(dataTimestamp);
	        batzarra.setCreatedAt(new Timestamp(System.currentTimeMillis()));
	        
	        batzarra.setAula(dto.getAula());
	        batzarra.setIdCentro(dto.getIdCentro());

	        saioa.save(batzarra);
	        tx.commit();

	        ReunionDTO erantzuna = new ReunionDTO(batzarra);
	        return ResponseEntity.status(HttpStatus.CREATED).body(erantzuna);

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        saioa.close();
	    }
	}
	
	@DeleteMapping("/deleteReunion/{id}")
	public ResponseEntity<Void> batzarraDelete(@PathVariable("id") Integer id) {

	    Session saioa = HibernateUtil.getSessionFactory().openSession();
	    Transaction tx = null;

	    try {
	        tx = saioa.beginTransaction();

	        // Intentamos obtener la reunión
	        Reuniones batzarra = saioa.get(Reuniones.class, id);

	        if (batzarra != null) {
	            saioa.delete(batzarra);
	            tx.commit();
	            // 204 No Content es lo ideal para un delete exitoso
	            return ResponseEntity.noContent().build();
	        } else {
	            // Si el ID no existe, devolvemos 404
	            return ResponseEntity.notFound().build();
	        }

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        saioa.close();
	    }
	}
	
	@PutMapping("/updateReunion/{id}")
	public ResponseEntity<ReunionDTO> egoeraEguneratu(
	        @PathVariable("id") Integer id,
	        @RequestBody EstadoDTO dto) {

	    Session saioa = HibernateUtil.getSessionFactory().openSession();
	    Transaction tx = null;

	    try {
	        tx = saioa.beginTransaction();

	        // 1️⃣ Batzarra ID bidez bilatu
	        Reuniones batzarra = saioa.get(Reuniones.class, id);
	        if (batzarra == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        // 2️⃣ Egoera eguneratu
	        batzarra.setEstado(dto.getEstado());

	        // 3️⃣ Aldaketak gorde
	        saioa.update(batzarra);
	        tx.commit();

	        // 4️⃣ DTO garbia itzuli
	        ReunionDTO erantzuna = new ReunionDTO(batzarra);
	        return ResponseEntity.ok(erantzuna);

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        saioa.close();
	    }
	}
}