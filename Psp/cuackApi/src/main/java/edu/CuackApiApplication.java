package edu;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.controller.HibernateUtil;
import modelo.Users;
import serverSocket.ServidorSocket;



@SpringBootApplication
public class CuackApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuackApiApplication.class, args);
		
		ServidorSocket server = new ServidorSocket();
		
		
		server.hasiera();
		
	}

}
