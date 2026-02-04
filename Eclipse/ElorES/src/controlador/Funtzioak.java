package controlador;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

import modelo.Horarios;
import modelo.Reuniones;
import modelo.Users;
import vista.SortuBatzarrak;
import vista.BatzarraDetalle;
import vista.Batzarrak;
import vista.BesteOrdutegiak;
import vista.IkasleDetalle;
import vista.Ikasleak;
import vista.Login;
import vista.Menu;
import vista.OrdutegiDetalle;
import vista.Ordutegia;
import vista.Profila;

public class Funtzioak {
	
	Toolkit tk = Toolkit.getDefaultToolkit();
	
	public static String host = "10.5.104.131";
//	public static String host = "192.168.1.192";
	
	public static int puerto = 12345;

	public static Integer idTipoIkaslea = 4;
	public static Integer idTipoIrakaslea = 3;
	
	public static int resolucionX = 1000;
	public static int resolucionY = 650;
	
	public static int cordX = 500;
	public static int cordY = 200;

	public static void cambioVentana(Rectangle Cordenada, int x, int y) {
		cordenadas(Cordenada);
		resolucion(x, y);
	}
	
	public static void cordenadas(Rectangle Cordenada) {
		cordX = (int)Cordenada.getX();
    	cordY = (int)Cordenada.getY();
	}
	
	public static void resolucion(int x, int y) {
		resolucionX = x;
		resolucionY = y;
	}
	
	public static void irekiLogin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login login = new Login();
					login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiMenu(Users currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu menu = new Menu(currentUser);
					menu.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiBatzarrak(Users currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Batzarrak batzarrak = new Batzarrak(currentUser);
					batzarrak.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiSortuBatzarrak(Users currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SortuBatzarrak batzarrak = new SortuBatzarrak(currentUser);
					batzarrak.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiBesteOrdutegiak(Users currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BesteOrdutegiak besteOrdutegiak = new BesteOrdutegiak(currentUser);
					besteOrdutegiak.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiIkasleak(Users currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ikasleak ikasleak = new Ikasleak(currentUser);
					ikasleak.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiIkasleDetalle(Users ikasle, Users currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IkasleDetalle ikasleDetalle = new IkasleDetalle(ikasle, currentUser);
					ikasleDetalle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void irekiProfila(Users currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Profila profila = new Profila(currentUser);
					profila.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiOrdutegia(Users currentUser, Users irakasle) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ordutegia ordutegia = new Ordutegia(currentUser, irakasle);
					ordutegia.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiOrdutegiDetalle(Users currentUser, Users irakasle, Integer idAsgignatura, ArrayList<Horarios> horario) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrdutegiDetalle ordutegiDetalle = new OrdutegiDetalle(currentUser, irakasle, idAsgignatura, horario);
					ordutegiDetalle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void irekiBatzarraDetalle(Users currentUser, Reuniones reunion) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BatzarraDetalle batzarraDetalle = new BatzarraDetalle(currentUser,reunion);
					batzarraDetalle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
