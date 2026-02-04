package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controlador.Funtzioak;
import controlador.zerbitzaria.AlumnosController;
import modelo.Ciclos;
import modelo.Users;
import modelo.Combos.ComboZikloaItem;
import modelo.Combos.ComboKurtsoItem;

public class Ikasleak extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	AlumnosController alum = new AlumnosController();
	private JTable table;
	ArrayList<Users> ikasleak = new ArrayList<Users>();

	public Ikasleak(Users currentUser) {
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

		JComboBox<ComboZikloaItem> comboBoxZikloa = new JComboBox<>();
		comboBoxZikloa.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxZikloa.setBounds(236, 39, 137, 34);
		contentPane.add(comboBoxZikloa);

		JComboBox<ComboKurtsoItem> comboBoxKurtsoa = new JComboBox<>();
		comboBoxKurtsoa.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxKurtsoa.setBounds(549, 39, 137, 34);
		contentPane.add(comboBoxKurtsoa);

		JLabel lblFiltroZikloa = new JLabel("Zikloa");
		lblFiltroZikloa.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFiltroZikloa.setHorizontalAlignment(SwingConstants.CENTER);
		lblFiltroZikloa.setBounds(260, 10, 79, 22);
		contentPane.add(lblFiltroZikloa);

		JLabel lblFiltroKurtsoa = new JLabel("Kurtsoa");
		lblFiltroKurtsoa.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFiltroKurtsoa.setHorizontalAlignment(SwingConstants.CENTER);
		lblFiltroKurtsoa.setBounds(580, 10, 79, 22);
		contentPane.add(lblFiltroKurtsoa);

		ikasleak = alum.ikasleIkerketa(currentUser.getId());

		beteTaula(ikasleak, currentUser);
		beteCombos(comboBoxZikloa, comboBoxKurtsoa, currentUser.getId());

		JButton btnNewButton = new JButton("Baieztatu");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				ComboKurtsoItem kurtsoSelect = (ComboKurtsoItem) comboBoxKurtsoa.getSelectedItem();
				Integer idKurtsoa = kurtsoSelect.getId();

				ComboZikloaItem zikloSelect = (ComboZikloaItem) comboBoxZikloa.getSelectedItem();
				Integer idZikloa = zikloSelect.getId();

				if (idKurtsoa != -1) {
					ikasleak = alum.ikasleKurtsoak(idKurtsoa, Funtzioak.idTipoIrakaslea);
				}

				if (idZikloa != -1) {
					ikasleak = alum.ikasleZikloa(idZikloa, Funtzioak.idTipoIrakaslea);
				}

				beteTaula(ikasleak, currentUser);

			}
		});
		btnNewButton.setBounds(762, 42, 108, 33);
		contentPane.add(btnNewButton);

	}

	private void beteCombos(JComboBox<ComboZikloaItem> comboBoxCiclo, JComboBox<ComboKurtsoItem> comboBoxCurso,
			Integer userId) {
		ArrayList<Ciclos> listaZikloak = alum.irakasleZikloak(userId);
		ArrayList<Byte> listaModulos = alum.irakasleModulos(userId);

		// Limpiar el combo antes de llenarlo
		comboBoxCiclo.removeAllItems();
		comboBoxCurso.removeAllItems();
		// Ítem por defecto

		// Ítem por defecto
		comboBoxCiclo.addItem(new ComboZikloaItem(-1, "	Aukeratu ziklo bat"));
		comboBoxCurso.addItem(new ComboKurtsoItem(-1, "Aukeratu kurtso bat"));

		// Agregar los ciclos reales
		for (Ciclos c : listaZikloak) {
			comboBoxCiclo.addItem(new ComboZikloaItem(c.getId(), c.getNombre()));
		}

		for (Byte mo : listaModulos) {
			comboBoxCurso.addItem(new ComboKurtsoItem(mo, String.valueOf(mo)));
		}

	}

	public void beteTaula(ArrayList<Users> ikasleak, Users currentUser) {

		String[] columnas = { "Izena", "Email" };

		DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Evitar edición directa
			}
		};

		for (Users user : ikasleak) {
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
						Users aukeratuta = ikasleak.get(lerroa);
						Funtzioak.irekiIkasleDetalle(aukeratuta, currentUser);
						dispose();
					}
				}
			}
		});

	}
}
