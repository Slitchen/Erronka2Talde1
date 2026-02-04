package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JCalendar;

import controlador.Funtzioak;
import controlador.zerbitzaria.AlumnosController;
import controlador.zerbitzaria.CentrosController;
import controlador.zerbitzaria.ProfesorController;
import controlador.zerbitzaria.ReunionesController;
import modelo.Centro;
import modelo.Reuniones;
import modelo.Users;
import modelo.Combos.ComboIkastetxeaItem;
import modelo.Combos.ComboIkasleItem;
import modelo.Combos.ComboIrakasleItem;
import modelo.enumerados.EstadosEnum;

public class SortuBatzarrak extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldTitulo;
	private JTextField textFieldAsuntua;
	private JTextField textFieldKlasea;

	private JCalendar jCalendar;
	private JSpinner timeSpinner;

	AlumnosController alum = new AlumnosController();
	ProfesorController profe = new ProfesorController();
	ReunionesController reu = new ReunionesController();
	CentrosController centro = new CentrosController();

	public SortuBatzarrak(Users currentUser) {
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
				Funtzioak.irekiBatzarrak(currentUser);
				dispose();
			}
		});
		btnAtzera.setBounds(868, 567, 106, 33);
		contentPane.add(btnAtzera);

		// Panel superior (header) azul intenso
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 123, 255)); // Azul vibrante moderno
		panel.setBounds(0, 0, 984, 85);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblProfila = new JLabel("Batzarrak");
		lblProfila.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblProfila.setForeground(Color.WHITE);
		lblProfila.setHorizontalAlignment(SwingConstants.CENTER);
		lblProfila.setBounds(270, 11, 429, 69);
		panel.add(lblProfila);

		JPanel panelFormulario = new JPanel();
		panelFormulario.setBackground(new Color(0, 123, 255));
		panelFormulario.setBounds(173, 112, 654, 464);
		contentPane.add(panelFormulario);
		panelFormulario.setLayout(null);

		JComboBox<EstadosEnum> comboBoxEstatua = new JComboBox<>();
		comboBoxEstatua.setBounds(60, 75, 144, 33);
		panelFormulario.add(comboBoxEstatua);

		// JCalendar para seleccionar fecha
		jCalendar = new JCalendar();
		jCalendar.setBounds(35, 172, 180, 120); // Ajusta posición y tamaño
		panelFormulario.add(jCalendar);

		// En el constructor, cuando creas el spinner:
		SpinnerDateModel timeModel = new SpinnerDateModel();
		timeSpinner = new JSpinner(timeModel);
		// "HH" es formato 24h. Si usas "hh" (minúscula) es formato 12h y causa errores AM/PM.
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
		timeSpinner.setEditor(timeEditor);
		
		// --- LA SOLUCIÓN: Bloquear el teclado en el editor ---
		timeEditor.getTextField().setEditable(false);
		// Esto hace que solo se puedan usar las flechitas o las teclas de dirección,
		// impidiendo que escriban letras.

		timeSpinner.setBounds(250, 172, 154, 33);
		panelFormulario.add(timeSpinner);

		textFieldTitulo = new JTextField();
		textFieldTitulo.setColumns(10);
		textFieldTitulo.setBounds(243, 75, 154, 33);
		panelFormulario.add(textFieldTitulo);

		textFieldAsuntua = new JTextField();
		textFieldAsuntua.setColumns(10);
		textFieldAsuntua.setBounds(437, 75, 154, 33);
		panelFormulario.add(textFieldAsuntua);

		textFieldKlasea = new JTextField();
		textFieldKlasea.setColumns(10);
		textFieldKlasea.setBounds(437, 171, 154, 33);
		panelFormulario.add(textFieldKlasea);

		JLabel lblEstatua = new JLabel("Estatua");
		lblEstatua.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblEstatua.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstatua.setBounds(50, 34, 154, 30);
		panelFormulario.add(lblEstatua);

		JLabel lblTitulo = new JLabel("Titulo");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblTitulo.setBounds(243, 34, 154, 30);
		panelFormulario.add(lblTitulo);

		JLabel lblAsuntua = new JLabel("Asuntua");
		lblAsuntua.setHorizontalAlignment(SwingConstants.CENTER);
		lblAsuntua.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAsuntua.setBounds(437, 34, 154, 30);
		panelFormulario.add(lblAsuntua);

		JLabel lblData = new JLabel("Data");
		lblData.setHorizontalAlignment(SwingConstants.CENTER);
		lblData.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblData.setBounds(43, 129, 154, 30);
		panelFormulario.add(lblData);

		JLabel lblOrdua = new JLabel("Ordua");
		lblOrdua.setHorizontalAlignment(SwingConstants.CENTER);
		lblOrdua.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblOrdua.setBounds(243, 130, 154, 30);
		panelFormulario.add(lblOrdua);

		JLabel lblKlasea = new JLabel("Klasea");
		lblKlasea.setHorizontalAlignment(SwingConstants.CENTER);
		lblKlasea.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblKlasea.setBounds(437, 130, 154, 30);
		panelFormulario.add(lblKlasea);

		JComboBox<ComboIkastetxeaItem> comboBoxIkastetxea = new JComboBox<>();
		comboBoxIkastetxea.setBounds(253, 256, 339, 29);
		panelFormulario.add(comboBoxIkastetxea);

		JLabel lblIkastetxea = new JLabel("Ikastetxeak");
		lblIkastetxea.setHorizontalAlignment(SwingConstants.CENTER);
		lblIkastetxea.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblIkastetxea.setBounds(345, 219, 154, 30);
		panelFormulario.add(lblIkastetxea);

		JComboBox<ComboIrakasleItem> comboBoxIrakaslea = new JComboBox<>();
		comboBoxIrakaslea.setBounds(71, 355, 128, 35);
		panelFormulario.add(comboBoxIrakaslea);

		JComboBox<ComboIkasleItem> comboBoxIkasleak = new JComboBox<>();
		comboBoxIkasleak.setBounds(450, 355, 128, 35);
		panelFormulario.add(comboBoxIkasleak);

		JLabel lblIrakaslea = new JLabel("Irakasleak");
		lblIrakaslea.setHorizontalAlignment(SwingConstants.CENTER);
		lblIrakaslea.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblIrakaslea.setBounds(60, 314, 154, 30);
		panelFormulario.add(lblIrakaslea);

		JLabel lblIkaslea = new JLabel("Ikasleak");
		lblIkaslea.setHorizontalAlignment(SwingConstants.CENTER);
		lblIkaslea.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblIkaslea.setBounds(437, 314, 154, 30);
		panelFormulario.add(lblIkaslea);

		JButton btnGorde = new JButton("Gorde");
		btnGorde.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String estado = null;
				if(comboBoxEstatua.getSelectedItem() != null) {
					estado = comboBoxEstatua.getSelectedItem().toString().toLowerCase();
				}
				
				
				String titulua = textFieldTitulo.getText();

				String asuntua = textFieldAsuntua.getText();

				// 1. Fecha del JCalendar
				Date selectedDate = jCalendar.getDate();

				// 2. Hora del spinner
				Date selectedTime = (Date) timeSpinner.getValue();

				// 3. Calendar para la fecha
				Calendar cal = Calendar.getInstance();
				cal.setTime(selectedDate);

				// 4. Calendar para la hora
				Calendar calTime = Calendar.getInstance();
				calTime.setTime(selectedTime);

				// 5. Poner la hora, minutos y segundos EXACTOS
				cal.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
				cal.set(Calendar.SECOND, calTime.get(Calendar.SECOND));
				cal.set(Calendar.MILLISECOND, 0);

				// 6. Crear Timestamp correcto
				Timestamp timestamp = new Timestamp(cal.getTimeInMillis());

				System.out.println("Timestamp final: " + timestamp);


				String klasea = textFieldKlasea.getText();

				// Hacemos el casting al objeto que metimos originalmente
				ComboIkastetxeaItem seleccionado = (ComboIkastetxeaItem) comboBoxIkastetxea.getSelectedItem();
				String idCentro = String.valueOf(seleccionado.getId());

				ComboIrakasleItem profeSelect = (ComboIrakasleItem) comboBoxIrakaslea.getSelectedItem();
				Integer idProfe =  profeSelect.getId();

				// Para el alumno
				ComboIkasleItem ikasleSelect = (ComboIkasleItem) comboBoxIkasleak.getSelectedItem();
				Integer idIkasle = ikasleSelect.getId();

				if (titulua.isEmpty() || asuntua.isEmpty() || klasea.isEmpty() || idCentro.equals("-1") || idProfe == -1
						|| idIkasle == -1 || estado.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Ezin ahal da geratu ezer hutsik", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Users ikasle = new Users();
				ikasle.setId(idIkasle);
				
				Users irakasle = new Users();
				irakasle.setId(idProfe);
				
				Reuniones newReu = new Reuniones(ikasle, irakasle, estado, null, idCentro, titulua, asuntua, klasea, timestamp, new Timestamp(System.currentTimeMillis()), null);

				reu.SortuBatzarrak(newReu);
				
			}
		});
		btnGorde.setBounds(260, 420, 137, 33);
		panelFormulario.add(btnGorde);

		beteCombos(comboBoxIkastetxea, comboBoxIrakaslea, comboBoxIkasleak, comboBoxEstatua);

	}

	private void beteCombos(JComboBox<ComboIkastetxeaItem> comboBoxIkastetxea,
			JComboBox<ComboIrakasleItem> comboBoxIrakaslea, JComboBox<ComboIkasleItem> comboBoxIkasleak,
			JComboBox<EstadosEnum> comboBoxEstatua) {
		ArrayList<Users> allIkasleak = alum.ikasleAllIkerketa(Funtzioak.idTipoIkaslea);
		ArrayList<Users> allIrakasleak = profe.irakasleIkerketa(Funtzioak.idTipoIrakaslea);
		ArrayList<Centro> allIkastetxeak = centro.getCentrosJson();

		// Limpiar el combo antes de llenarlo
		comboBoxIkastetxea.removeAllItems();
		comboBoxIrakaslea.removeAllItems();
		comboBoxIkasleak.removeAllItems();
		comboBoxEstatua.removeAllItems();
		// Ítem por defecto

		// Ítem por defecto
		comboBoxIkastetxea.addItem(new ComboIkastetxeaItem(-1, "Aukeratu ikastetxe bat"));
		comboBoxIrakaslea.addItem(new ComboIrakasleItem(-1, "Aukeratu irakasle bat"));
		comboBoxIkasleak.addItem(new ComboIkasleItem(-1, "Aukeratu ikasle bat"));

		// Agregar los ciclos reales
		for (Centro c : allIkastetxeak) {
			comboBoxIkastetxea.addItem(new ComboIkastetxeaItem(c.getIdCentro(), c.getNombreCas()));
		}

		// Agregar los ciclos reales
		for (Users c : allIkasleak) {
			comboBoxIkasleak.addItem(new ComboIkasleItem(c.getId(), c.getNombre()));
		}

		// Agregar los ciclos reales
		for (Users c : allIrakasleak) {
			comboBoxIrakaslea.addItem(new ComboIrakasleItem(c.getId(), c.getNombre()));
		}

		// Placeholder
		comboBoxEstatua.insertItemAt(null, 0);
		comboBoxEstatua.setSelectedIndex(0);

		// Cargar enum
		for (EstadosEnum e : EstadosEnum.values()) {
			comboBoxEstatua.addItem(e);
		}

	}

}
