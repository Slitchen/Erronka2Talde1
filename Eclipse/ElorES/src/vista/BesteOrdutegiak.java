package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controlador.Funtzioak;
import controlador.zerbitzaria.ProfesorController;
import modelo.Users;
import modelo.Combos.ComboIrakasleAbizenaItem;
import modelo.Combos.ComboIrakasleItem;

public class BesteOrdutegiak extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	ProfesorController profe = new ProfesorController();
	private JTable table;
	ArrayList<Users> irakasleak = new ArrayList<Users>();
	
	public BesteOrdutegiak(Users currentUser) {
		
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
		
		JComboBox<ComboIrakasleAbizenaItem> comboBoxIrakasleAbizena = new JComboBox<>();
		comboBoxIrakasleAbizena.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxIrakasleAbizena.setBounds(587, 36, 137, 34);
		contentPane.add(comboBoxIrakasleAbizena);

		JComboBox<ComboIrakasleItem> comboBoxIrakasleIzena = new JComboBox<>();
		comboBoxIrakasleIzena.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxIrakasleIzena.setBounds(221, 36, 137, 34);
		contentPane.add(comboBoxIrakasleIzena);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(151, 104, 684, 438);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		irakasleak = profe.irakasleIkerketa(Funtzioak.idTipoIrakaslea);
		
		beteTaula(irakasleak, currentUser);
		beteCombos(comboBoxIrakasleAbizena, comboBoxIrakasleIzena);
		
		JButton btnBaieztapena = new JButton("Baieztatu");
		btnBaieztapena.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ComboIrakasleAbizenaItem abizenaSelect = (ComboIrakasleAbizenaItem) comboBoxIrakasleAbizena.getSelectedItem();
				String abizena = abizenaSelect.getNombre();

				ComboIrakasleItem izenaSelect = (ComboIrakasleItem) comboBoxIrakasleIzena.getSelectedItem();
				String izena = izenaSelect.getNombre();

				if (izenaSelect.getId() != -1) {
					irakasleak = profe.irakasleIkerketaIzena(izena);
				}
				

				if (abizenaSelect.getId() != -1) {
					irakasleak = profe.irakasleIkerketaAbizena(abizena);
				}
				
				if(abizenaSelect.getId() == -1 && izenaSelect.getId() == -1 ) {
					irakasleak = profe.irakasleIkerketa(Funtzioak.idTipoIrakaslea);
				}

				beteTaula(irakasleak, currentUser);
				
			}
		});
		btnBaieztapena.setBounds(776, 38, 104, 34);
		contentPane.add(btnBaieztapena);

	}
	
	public void beteTaula(ArrayList<Users> irakasleak, Users currentUser) {

		String[] columnas = { "Izena", "Email" };

		DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Evitar edición directa
			}
		};

		for (Users user : irakasleak) {
			modelo.addRow(new Object[] { user.getNombre(), user.getEmail() });
		}

		// Asignar el modelo al JTable
		table.setModel(modelo);

		// Opcional: ajustar tamaño de columnas
		table.getColumnModel().getColumn(0).setPreferredWidth(150); // Nombre
		table.getColumnModel().getColumn(1).setPreferredWidth(200); // Email

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) { // doble click
					int lerroa = table.getSelectedRow();
					if (lerroa >= 0) {
						Users aukeratuta = irakasleak.get(lerroa);
						Funtzioak.irekiOrdutegia(currentUser, aukeratuta);
						dispose();
					}
				}
			}
		});

	}
	
	private void beteCombos(JComboBox<ComboIrakasleAbizenaItem> comboBoxIrakasleAbizena, JComboBox<ComboIrakasleItem> comboBoxIrakasleIzena) {
	    ArrayList<Users> listaIrakasleak = profe.irakasleIkerketa(Funtzioak.idTipoIrakaslea);

	    // Limpiar los combos
	    comboBoxIrakasleAbizena.removeAllItems();
	    comboBoxIrakasleIzena.removeAllItems();

	    // Ítems por defecto
	    comboBoxIrakasleIzena.addItem(new ComboIrakasleItem(-1, "Aukeratu izena"));
	    comboBoxIrakasleAbizena.addItem(new ComboIrakasleAbizenaItem(-1, "Aukeratu abizena"));

	    // Usamos Sets para controlar qué nombres y apellidos ya hemos añadido
	    Set<String> nombresAgregados = new HashSet<>();
	    Set<String> apellidosAgregados = new HashSet<>();

	    for (Users c : listaIrakasleak) {
	        
	        // 1. Filtrar NOMBRES (para que no se repitan)
	        if (c.getNombre() != null) {
	            String nombre = c.getNombre().trim();
	            if (!nombresAgregados.contains(nombre.toUpperCase())) {
	                comboBoxIrakasleIzena.addItem(new ComboIrakasleItem(c.getId(), nombre));
	                nombresAgregados.add(nombre.toUpperCase());
	            }
	        }

	        // 2. Filtrar APELLIDOS (para que no se repitan)
	        if (c.getApellidos() != null) {
	            String apellido = c.getApellidos().trim();
	            if (!apellidosAgregados.contains(apellido.toUpperCase())) {
	                comboBoxIrakasleAbizena.addItem(new ComboIrakasleAbizenaItem(c.getId(), apellido));
	                apellidosAgregados.add(apellido.toUpperCase());
	            }
	        }
	    }
	}
}
