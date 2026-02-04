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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.Funtzioak;
import modelo.Users;
import modelo.enumerados.TiposEnum;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public Menu(Users currentUser) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(Funtzioak.cordX, Funtzioak.cordY, Funtzioak.resolucionX, Funtzioak.resolucionY);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(220, 235, 255)); // Fondo principal azul muy claro
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Botones con azul medio
		Color btnColor = new Color(30, 144, 255); // Dodger Blue

		JButton btnProfila = new JButton("Profila");
		btnProfila.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnProfila.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiProfila(currentUser);
				dispose();
			}
		});
		btnProfila.setBounds(47, 370, 226, 217);
		btnProfila.setBackground(btnColor);
		btnProfila.setForeground(Color.WHITE);
		contentPane.add(btnProfila);
		
		JButton btnOrdutegia = new JButton("Ordutegia");
		btnOrdutegia.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnOrdutegia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiOrdutegia(currentUser, currentUser);
				dispose();
			}
		});
		btnOrdutegia.setBounds(47, 122, 226, 217);
		btnOrdutegia.setBackground(btnColor);
		btnOrdutegia.setForeground(Color.WHITE);
		contentPane.add(btnOrdutegia);
		
		JButton btnBesteOrdutegiak = new JButton("Beste Ordutegiak");
		btnBesteOrdutegiak.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnBesteOrdutegiak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiBesteOrdutegiak(currentUser);
				dispose();
			}
		});
		btnBesteOrdutegiak.setBounds(372, 122, 226, 217);
		btnBesteOrdutegiak.setBackground(btnColor);
		btnBesteOrdutegiak.setForeground(Color.WHITE);
		contentPane.add(btnBesteOrdutegiak);
		
		JButton btnIkasleak = new JButton("Ikasleak");
		btnIkasleak.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnIkasleak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiIkasleak(currentUser);
				dispose();
			}
		});
		btnIkasleak.setBounds(372, 370, 226, 217);
		btnIkasleak.setBackground(btnColor);
		btnIkasleak.setForeground(Color.WHITE);
		contentPane.add(btnIkasleak);
		
		JButton btnBatzarrak = new JButton("Batzarrak");
		btnBatzarrak.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnBatzarrak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiBatzarrak(currentUser);
				dispose();
			}
		});
		btnBatzarrak.setBounds(703, 122, 226, 217);
		btnBatzarrak.setBackground(btnColor);
		btnBatzarrak.setForeground(Color.WHITE);
		contentPane.add(btnBatzarrak);
		
		JButton btnLogOut = new JButton("Log out");
		btnLogOut.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiLogin();
				dispose();
			}
		});
		btnLogOut.setBounds(703, 370, 226, 217);
		btnLogOut.setBackground(btnColor);
		btnLogOut.setForeground(Color.WHITE);
		contentPane.add(btnLogOut);
		
		// Panel superior (header) azul intenso
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 123, 255)); // Azul vibrante moderno
		panel.setBounds(0, 0, 984, 85);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblMenu = new JLabel("Menu");
		lblMenu.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblMenu.setForeground(Color.WHITE);
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenu.setBounds(270, 11, 429, 69);
		panel.add(lblMenu);
		
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 123, 255));
		panel_1.setBounds(900, 10, 63, 63);
		panel_1.setLayout(new BorderLayout()); // importante

		try {
			if(currentUser.getArgazkiaUrl() != null) {
		    @SuppressWarnings("deprecation")
			URL imageUrl = new URL(currentUser.getArgazkiaUrl());
		    ImageIcon icon = new ImageIcon(imageUrl);

		    // Escalar la imagen al tamaño del panel
		    Image img = icon.getImage().getScaledInstance(63, 63, Image.SCALE_SMOOTH);
		    JLabel label = new JLabel(new ImageIcon(img));

		    panel_1.add(label, BorderLayout.CENTER);
			}

		} catch (Exception e) {
		    e.printStackTrace();
		}

		panel.add(panel_1);
		
		TiposEnum tipo = TiposEnum.fromCodigo(currentUser.getTipos().getId());
		
		JLabel lblUsername = new JLabel(tipo.toString());
		lblUsername.setBounds(780, 25, 117, 34);
		panel.add(lblUsername);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
	}
}
