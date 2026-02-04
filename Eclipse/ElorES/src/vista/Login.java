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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.Funtzioak;
import controlador.zerbitzaria.UsersController;
import modelo.Users;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldErabiltzailea;
	UsersController userC = new UsersController();

	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(Funtzioak.cordX, Funtzioak.cordY, Funtzioak.resolucionX, Funtzioak.resolucionY);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(220, 235, 255)); // Fondo principal azul claro
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Panel central
		JPanel panel = new JPanel();
		panel.setBackground(new Color(13, 71, 161)); // Azul oscuro moderno
		panel.setBounds(301, 86, 361, 365);
		contentPane.add(panel);
		panel.setLayout(null);

		// Label Erabiltzailea
		JLabel lblErabiltzailea = new JLabel("Erabiltzailea");
		lblErabiltzailea.setForeground(Color.WHITE);
		lblErabiltzailea.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblErabiltzailea.setHorizontalAlignment(SwingConstants.CENTER);
		lblErabiltzailea.setBounds(112, 115, 132, 30);
		panel.add(lblErabiltzailea);

		// Campo usuario
		textFieldErabiltzailea = new JTextField();
		textFieldErabiltzailea.setBounds(99, 156, 159, 30);
		panel.add(textFieldErabiltzailea);
		textFieldErabiltzailea.setColumns(10);

		// Label Pasahitza
		JLabel lblPasahitza = new JLabel("Pasahitza");
		lblPasahitza.setForeground(Color.WHITE);
		lblPasahitza.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasahitza.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPasahitza.setBounds(112, 202, 132, 30);
		panel.add(lblPasahitza);

		// 1. Cambia el tipo de la variable (asegúrate de cambiarlo también arriba en los atributos de la clase)
		JPasswordField textFieldPasahitza = new JPasswordField();

		// 2. Configuración (se mantiene igual que tenías)
		textFieldPasahitza.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textFieldPasahitza.setColumns(10);
		textFieldPasahitza.setBounds(99, 243, 159, 30);

		// Opcional: Si quieres que el carácter sea específicamente un asterisco '*' 
		// (por defecto suele ser un punto negro)
		textFieldPasahitza.setEchoChar('*'); 

		panel.add(textFieldPasahitza);
		
		// 1. Crear el CheckBox para mostrar/ocultar
		JCheckBox chckbxErakutsi = new JCheckBox("Ikusi");
		chckbxErakutsi.setForeground(new Color(255, 255, 255));
		chckbxErakutsi.setBackground(new Color(13, 71, 161)); // Pon el color de tu panel
		chckbxErakutsi.setBounds(264, 241, 70, 30);
		panel.add(chckbxErakutsi);

		// 2. Añadir la lógica
		chckbxErakutsi.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (chckbxErakutsi.isSelected()) {
		            // Si está seleccionado, mostramos el texto plano (0 es el carácter nulo)
		            textFieldPasahitza.setEchoChar((char) 0);
		        } else {
		            // Si no, volvemos a poner los asteriscos
		            textFieldPasahitza.setEchoChar('*');
		        }
		    }
		});
		
		// Label error
		JLabel lblError = new JLabel("");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setForeground(Color.PINK); // Se puede destacar en rosa claro
		lblError.setBounds(59, 282, 246, 21);
		panel.add(lblError);

		// Botón confirmar
		JButton btnBaieztatu = new JButton("Baieztatu");
		btnBaieztatu.setBackground(new Color(25, 118, 210)); // Azul vibrante moderno
		btnBaieztatu.setForeground(Color.WHITE);
		btnBaieztatu.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBaieztatu.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				String erabiltzailea = textFieldErabiltzailea.getText();
				String pasahitza = textFieldPasahitza.getText();

				if (!erabiltzailea.isEmpty() && !pasahitza.isEmpty()) {
					Users currentUser = userC.loginIkerketa(erabiltzailea, pasahitza, lblError);
					if (currentUser != null) {
						Funtzioak.irekiMenu(currentUser);
						dispose();
					}
				} else {
					lblError.setText("Ezin ahal da geratu ezer hutsik !!!");
				}
			}
		});
		btnBaieztatu.setBounds(131, 309, 106, 30);
		panel.add(btnBaieztatu);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 341, 78);
		panel_1.setBackground(new Color(0, 102, 204));

		try {
			@SuppressWarnings("deprecation")
			URL imageUrl = new URL(
					"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9YNJWt9pyckMNBxYnaja6NyLisCHjQFT5QQ&s");
			ImageIcon icon = new ImageIcon(imageUrl);

			// Escalar la imagen al tamaño del panel
			Image img = icon.getImage().getScaledInstance(63, 63, Image.SCALE_SMOOTH);
			JLabel label = new JLabel(new ImageIcon(img));

			panel_1.add(label, BorderLayout.CENTER);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			URL localUrl = Thread.currentThread().getContextClassLoader().getResource("Cuack.png");
			ImageIcon iconoOriginal = new ImageIcon(localUrl);
			
			Image img2 = iconoOriginal.getImage().getScaledInstance(63, 63, Image.SCALE_SMOOTH);
			JLabel label2 = new JLabel(new ImageIcon(img2));

			panel_1.add(label2, BorderLayout.CENTER);

		} catch (Exception e) {
			e.printStackTrace();
		}

		panel.add(panel_1);
	}
}
