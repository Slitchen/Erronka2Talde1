package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.Funtzioak;
import modelo.Users;
import modelo.enumerados.TiposEnum;

public class IkasleDetalle extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEmail;
	private JTextField textFieldErabiltzailea;
	private JTextField textFieldArgazkiUrl;
	private JTextField textFieldIzena;
	private JTextField textFieldAbizenak;
	private JTextField textFieldNif;
	private JTextField textFieldHelbidea;
	private JTextField textFieldTelZbk1;
	private JTextField textFieldTelZbk2;
	private JLabel lblHelbidea;
	private JLabel lblTelfZbk1;
	private JLabel lblTelfZbk2;

	@SuppressWarnings("deprecation")
	public IkasleDetalle(Users ikasleDetalle, Users currentUser) {
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
				Funtzioak.irekiIkasleak(currentUser);
				dispose();
			}
		});
		btnAtzera.setBounds(837, 567, 137, 33);
		contentPane.add(btnAtzera);

		// Panel superior (header) azul intenso
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 123, 255)); // Azul vibrante moderno
		panel.setBounds(0, 0, 984, 85);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblProfila = new JLabel("Detalle");
		lblProfila.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblProfila.setForeground(Color.WHITE);
		lblProfila.setHorizontalAlignment(SwingConstants.CENTER);
		lblProfila.setBounds(270, 11, 429, 69);
		panel.add(lblProfila);

		TiposEnum tipo = TiposEnum.fromCodigo(ikasleDetalle.getTipos().getId());

		JLabel lblUsernameTitulua = new JLabel(tipo.toString());
		lblUsernameTitulua.setBounds(780, 25, 117, 34);
		panel.add(lblUsernameTitulua);
		lblUsernameTitulua.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsernameTitulua.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panelArgazkia = new JPanel();
		panelArgazkia.setBackground(new Color(0, 123, 255));
		panelArgazkia.setBounds(900, 10, 63, 63);
		panelArgazkia.setLayout(new BorderLayout()); // importante

		try {
			if (ikasleDetalle.getArgazkiaUrl() != null) {
				URL imageUrl = new URL(ikasleDetalle.getArgazkiaUrl());
				ImageIcon icon = new ImageIcon(imageUrl);

				// Escalar la imagen al tamaño del panel
				Image img = icon.getImage().getScaledInstance(63, 63, Image.SCALE_SMOOTH);
				JLabel label = new JLabel(new ImageIcon(img));

				panelArgazkia.add(label, BorderLayout.CENTER);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		panel.add(panelArgazkia);

		JPanel panelFormulario = new JPanel();
		panelFormulario.setBackground(new Color(0, 123, 255));
		panelFormulario.setBounds(157, 118, 654, 391);
		contentPane.add(panelFormulario);
		panelFormulario.setLayout(null);

		textFieldEmail = new JTextField();
		textFieldEmail.setColumns(10);
		textFieldEmail.setBounds(50, 75, 154, 33);
		panelFormulario.add(textFieldEmail);

		textFieldErabiltzailea = new JTextField();
		textFieldErabiltzailea.setColumns(10);
		textFieldErabiltzailea.setBounds(243, 75, 154, 33);
		panelFormulario.add(textFieldErabiltzailea);

		textFieldArgazkiUrl = new JTextField();
		textFieldArgazkiUrl.setColumns(10);
		textFieldArgazkiUrl.setBounds(437, 75, 154, 33);
		panelFormulario.add(textFieldArgazkiUrl);

		textFieldIzena = new JTextField();
		textFieldIzena.setColumns(10);
		textFieldIzena.setBounds(50, 171, 154, 33);
		panelFormulario.add(textFieldIzena);

		textFieldAbizenak = new JTextField();
		textFieldAbizenak.setColumns(10);
		textFieldAbizenak.setBounds(243, 171, 154, 33);
		panelFormulario.add(textFieldAbizenak);

		textFieldNif = new JTextField();
		textFieldNif.setColumns(10);
		textFieldNif.setBounds(437, 171, 154, 33);
		panelFormulario.add(textFieldNif);

		textFieldHelbidea = new JTextField();
		textFieldHelbidea.setColumns(10);
		textFieldHelbidea.setBounds(51, 270, 154, 33);
		panelFormulario.add(textFieldHelbidea);

		textFieldTelZbk1 = new JTextField();
		textFieldTelZbk1.setColumns(10);
		textFieldTelZbk1.setBounds(244, 270, 154, 33);
		panelFormulario.add(textFieldTelZbk1);

		textFieldTelZbk2 = new JTextField();
		textFieldTelZbk2.setColumns(10);
		textFieldTelZbk2.setBounds(438, 270, 154, 33);
		panelFormulario.add(textFieldTelZbk2);

		JLabel lblEmail = new JLabel("Email-a");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setBounds(50, 34, 154, 30);
		panelFormulario.add(lblEmail);

		JLabel lblUsername = new JLabel("Erabiltzailea");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblUsername.setBounds(243, 34, 154, 30);
		panelFormulario.add(lblUsername);

		JLabel lblArgazkiUrl = new JLabel("ArgazkiUrl");
		lblArgazkiUrl.setHorizontalAlignment(SwingConstants.CENTER);
		lblArgazkiUrl.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblArgazkiUrl.setBounds(437, 34, 154, 30);
		panelFormulario.add(lblArgazkiUrl);

		JLabel lblIzena = new JLabel("Izena");
		lblIzena.setHorizontalAlignment(SwingConstants.CENTER);
		lblIzena.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblIzena.setBounds(50, 130, 154, 30);
		panelFormulario.add(lblIzena);

		JLabel lblAbizenak = new JLabel("Abizenak");
		lblAbizenak.setHorizontalAlignment(SwingConstants.CENTER);
		lblAbizenak.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAbizenak.setBounds(243, 130, 154, 30);
		panelFormulario.add(lblAbizenak);

		JLabel lblNif = new JLabel("Nif");
		lblNif.setHorizontalAlignment(SwingConstants.CENTER);
		lblNif.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNif.setBounds(437, 130, 154, 30);
		panelFormulario.add(lblNif);

		lblHelbidea = new JLabel("Helbidea");
		lblHelbidea.setHorizontalAlignment(SwingConstants.CENTER);
		lblHelbidea.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblHelbidea.setBounds(51, 229, 154, 30);
		panelFormulario.add(lblHelbidea);

		lblTelfZbk1 = new JLabel("Tel Zbk 1");
		lblTelfZbk1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTelfZbk1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblTelfZbk1.setBounds(244, 229, 154, 30);
		panelFormulario.add(lblTelfZbk1);

		lblTelfZbk2 = new JLabel("Tel Zbk 2");
		lblTelfZbk2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTelfZbk2.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblTelfZbk2.setBounds(438, 229, 154, 30);
		panelFormulario.add(lblTelfZbk2);
		
		beteTxtFields(ikasleDetalle);
	}
	
	public void beteTxtFields(Users ikasleDetalle) {
		
		textFieldEmail.setText(ikasleDetalle.getEmail());
		textFieldErabiltzailea.setText(ikasleDetalle.getUsername());
		textFieldIzena.setText(ikasleDetalle.getNombre());
		textFieldAbizenak.setText(ikasleDetalle.getApellidos());
		textFieldNif.setText(ikasleDetalle.getDni());
		textFieldHelbidea.setText(ikasleDetalle.getDireccion());
		textFieldTelZbk1.setText(ikasleDetalle.getTelefono1());
		textFieldTelZbk2.setText(ikasleDetalle.getTelefono2());
		textFieldArgazkiUrl.setText(ikasleDetalle.getArgazkiaUrl());
		
	}
}
