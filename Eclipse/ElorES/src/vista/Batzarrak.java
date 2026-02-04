package vista;

import java.awt.Color;
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
import controlador.zerbitzaria.ReunionesController;
import modelo.Reuniones;
import modelo.Users;

public class Batzarrak extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	ReunionesController reu = new ReunionesController();
	private JTable table;

	public Batzarrak(Users currentUser) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(Funtzioak.cordX, Funtzioak.cordY, Funtzioak.resolucionX, Funtzioak.resolucionY);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(220, 235, 255)); // Fondo principal azul claro
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnAtzera = new JButton("Atzera");
		btnAtzera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiMenu(currentUser);
				dispose();
			}
		});
		btnAtzera.setBounds(837, 567, 137, 33);
		contentPane.add(btnAtzera);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(151, 104, 684, 438);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnSortuBatzarra = new JButton("Sortu Batzarra");
		btnSortuBatzarra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiSortuBatzarrak(currentUser);
				dispose();
			}
		});
		btnSortuBatzarra.setBounds(418, 567, 128, 28);
		contentPane.add(btnSortuBatzarra);

		beteTaula(currentUser);

	}
	
	public void beteTaula(Users currentUser) {
		ArrayList<Reuniones> batzarrak = reu.irakasleReuniones(currentUser.getId());

		String[] columnas = { "Egoera", "Gaia", "Ikastetxea", "Data"  };

		DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Evitar edición directa
			}
		};

		for (Reuniones reunion : batzarrak) {
			modelo.addRow(new Object[] { reunion.getEstado(), reunion.getAsunto(), reunion.getIdCentro(), reunion.getFecha() });
		}

		// Asignar el modelo al JTable
		table.setModel(modelo);

		// Opcional: ajustar tamaño de columnas
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) { // doble click
					int lerroa = table.getSelectedRow();
					if (lerroa >= 0) {
						Reuniones aukeratuta = batzarrak.get(lerroa);
						Funtzioak.irekiBatzarraDetalle(currentUser, aukeratuta);
						dispose();
					}
				}
			}
		});

	}
	
	
}
