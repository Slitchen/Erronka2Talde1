package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controlador.Funtzioak;
import controlador.zerbitzaria.HorariosController;
import controlador.zerbitzaria.ReunionesController;
import modelo.Horarios;
import modelo.Reuniones;
import modelo.Users;

public class Ordutegia extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	HorariosController horario = new HorariosController();
	ReunionesController reunion = new ReunionesController();
	private Integer[][] idHorariosMatriz = new Integer[6][6];

	public Ordutegia(Users currentUser, Users irakaslea) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(Funtzioak.cordX, Funtzioak.cordY, Funtzioak.resolucionX, Funtzioak.resolucionY);

		ArrayList<Horarios> listaOrdutegiak = horario.irakasleOrdutegiak(irakaslea.getId());
		ArrayList<Reuniones> listaBatzarrak = reunion.irakasleReuniones(irakaslea.getId());

		contentPane = new JPanel();
		contentPane.setBackground(new Color(220, 235, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 1. Columnas (Euskera)
		String[] columnas = { "Ordua", "Astelehena", "Asteartea", "Asteazkena", "Osteguna", "Ostirala" };

		// 2. Modelo y Tabla
		model = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Desactiva la edición en todas las celdas
			}
		};
		table = new JTable(model);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(80);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(170, 200, 240));

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) { // Detectar doble clic
					int fila = table.getSelectedRow();
					int col = table.getSelectedColumn();

					// Solo actuamos si no es la columna de las horas
					if (col > 0) {
						Integer idHorario = idHorariosMatriz[fila][col];

						// Solo abrimos el detalle si existe un ID de horario en esta celda
						if (idHorario != null) {
							irekiOrdutegiDetalle(currentUser, irakaslea, idHorario, listaOrdutegiak);
						}
						// Si idHorario es null (aunque haya una reunión), el programa no hará nada
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30, 50, 940, 500);
		contentPane.add(scrollPane);

		// 3. Inicializar filas vacías con las sesiones
		for (int i = 1; i <= 6; i++) {
			model.addRow(new Object[] { "Saioa " + i, "", "", "", "", "" });
		}

		// 4. LLAMADA AL SERVIDOR Y LLENADO

		if (listaOrdutegiak != null && !listaOrdutegiak.isEmpty()) {
			beteTaula(listaOrdutegiak);
		}

		if (listaBatzarrak != null && !listaBatzarrak.isEmpty()) {
			gehituReunioak(listaBatzarrak);
		}

		// Botón Atzera
		JButton btnAtzera = new JButton("Atzera");
		btnAtzera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentUser.getId() == irakaslea.getId()) {
					Funtzioak.irekiMenu(currentUser);
				}else {
					Funtzioak.irekiBesteOrdutegiak(currentUser);
				}
				dispose();
			}
		});
		btnAtzera.setBounds(833, 570, 137, 33);
		contentPane.add(btnAtzera);
	}

	private void irekiOrdutegiDetalle(Users currentUser, Users irakaslea, Integer id, ArrayList<Horarios> ordutegia) {
		Funtzioak.irekiOrdutegiDetalle(currentUser, irakaslea, id, ordutegia);
		dispose();
	}

	/**
	 * Rellena las celdas de la tabla con la información de los horarios.
	 */
	private void beteTaula(ArrayList<Horarios> ordutegiZerrenda) {
		for (Horarios h : ordutegiZerrenda) {
			// Calculamos posición
			int fila = h.getHora() - 1; // Si hora es 1, va a la fila 0
			int col = lortuZutabea(h.getDia());

			// Verificamos que los datos sean correctos para evitar errores
			if (fila >= 0 && fila < 6 && col != -1) {
				idHorariosMatriz[fila][col] = h.getId();
				String moduloIzena = (h.getModulos() != null) ? h.getModulos().getNombre() : "---";
				String gela = (h.getAula() != null) ? h.getAula() : "";

				// Formato HTML para centrar y poner negrita
				String textoCelda = "<html><center><b>" + moduloIzena + "</b><br>" + gela + "</center></html>";

				model.setValueAt(textoCelda, fila, col);
			}
		}
	}

	private void gehituReunioak(ArrayList<Reuniones> zerrenda) {
		for (Reuniones r : zerrenda) {
			// 1. Validar que la fecha no sea null
			if (r.getFecha() == null)
				continue;

			// 2. Usamos Calendar para extraer los datos de r.getFecha()
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(r.getFecha());

			// Extraer el día de la semana (1=Domingo, 2=Lunes...)
			int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);

			// Extraer la hora real (0-23)
			int horaReal = cal.get(java.util.Calendar.HOUR_OF_DAY);

			// 3. Convertir a coordenadas de tu tabla (Fila y Columna)
			int col = transformarDiaAColumna(dayOfWeek);
			int fila = transformarHoraAFila(horaReal);

			if (fila != -1 && col != -1) {

				Object actual = model.getValueAt(fila, col);
				String txtOriginal = (actual != null) ? actual.toString() : "";
				String htmlReunion = "";
				if (r.getEstado().equals("aceptada")) {
					htmlReunion = "<center><font color='green'><b> REUNIOA:</b> " + r.getAsunto() + "</font></center>";
				} else if (r.getEstado().equals("pendiente")) {
					htmlReunion = "<center><font color='yellow'><b> REUNIOA:</b> " + r.getAsunto() + "</font></center>";
				} else if (r.getEstado().equals("denegada")) {
					htmlReunion = "<center><font color='red'><b> REUNIOA:</b> " + r.getAsunto() + "</font></center>";
				} else if (r.getEstado().equals("conflicto")) {
					htmlReunion = "<center><font color='grey'><b> REUNIOA:</b> " + r.getAsunto() + "</font></center>";
				}

				if (txtOriginal.isEmpty() || !txtOriginal.contains("<html>")) {
					model.setValueAt("<html><center>" + htmlReunion + "</center></html>", fila, col);
				} else {
					String concatenado = txtOriginal.replace("</html>", "") + "<hr>" + htmlReunion + "</html>";
					model.setValueAt(concatenado, fila, col);
				}
			}
		}
	}

	// Convierte el Calendar.DAY_OF_WEEK a tu columna (1-5)
	private int transformarDiaAColumna(int dayOfWeek) {
		switch (dayOfWeek) {
		case java.util.Calendar.MONDAY:
			return 1;
		case java.util.Calendar.TUESDAY:
			return 2;
		case java.util.Calendar.WEDNESDAY:
			return 3;
		case java.util.Calendar.THURSDAY:
			return 4;
		case java.util.Calendar.FRIDAY:
			return 5;
		default:
			return -1; // Sábados o domingos no se pintan
		}
	}

	// Convierte la hora (8, 9, 10...) a la fila de la tabla (0-5)
	private int transformarHoraAFila(int horaReal) {
		if (horaReal >= 8 && horaReal < 9)
			return 0; // Saioa 1
		if (horaReal >= 9 && horaReal < 10)
			return 1; // Saioa 2
		if (horaReal >= 10 && horaReal < 11)
			return 2; // Saioa 3
		if (horaReal >= 11 && horaReal < 12)
			return 3; // Saioa 4
		if (horaReal >= 12 && horaReal < 13)
			return 4; // Saioa 5
		if (horaReal >= 13 && horaReal < 14)
			return 5; // Saioa 6
		return -1;
	}

	/**
	 * Mapea el día de la base de datos a la columna correspondiente.
	 */
	private int lortuZutabea(String eguna) {
		if (eguna == null)
			return -1;
		switch (eguna.toUpperCase()) {
		case "LUNES":
		case "ASTELEHENA":
			return 1;
		case "MARTES":
		case "ASTEARTEA":
			return 2;
		case "MIERCOLES":
		case "ASTEAZKENA":
			return 3;
		case "JUEVES":
		case "OSTEGUNA":
			return 4;
		case "VIERNES":
		case "OSTIRALA":
			return 5;
		default:
			return -1;
		}
	}
}