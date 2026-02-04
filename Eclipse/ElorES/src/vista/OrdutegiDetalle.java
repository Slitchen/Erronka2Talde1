package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.Funtzioak;
import modelo.Horarios;
import modelo.Users;

public class OrdutegiDetalle extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldCiclo;
	private JTextField textFieldModulo;
	private JTextField textFieldCurso;
	private JTextField textFieldProfesor;

	public OrdutegiDetalle(Users currentUser, Users irakaslea, Integer ikasgaiId, ArrayList<Horarios> ordutegia) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(Funtzioak.cordX, Funtzioak.cordY, Funtzioak.resolucionX, Funtzioak.resolucionY);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(220, 235, 255)); // Fondo principal azul claro
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Horarios ordua = new Horarios();
		
		for(int i = 0; i < ordutegia.size(); i ++) {
			if(ordutegia.get(i).getId() == ikasgaiId) {
				ordua = ordutegia.get(i);
			}
		}

		JButton btnAtzera = new JButton("Atzera");
		btnAtzera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funtzioak.irekiOrdutegia(currentUser, irakaslea);
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

		JPanel panelFormulario = new JPanel();
		panelFormulario.setBackground(new Color(0, 123, 255));
		panelFormulario.setBounds(156, 180, 654, 293);
		contentPane.add(panelFormulario);
		panelFormulario.setLayout(null);

		textFieldCiclo = new JTextField();
		textFieldCiclo.setColumns(10);
		textFieldCiclo.setBounds(50, 75, 154, 33);
		panelFormulario.add(textFieldCiclo);

		textFieldModulo = new JTextField();
		textFieldModulo.setColumns(10);
		textFieldModulo.setBounds(243, 75, 154, 33);
		panelFormulario.add(textFieldModulo);

		textFieldCurso = new JTextField();
		textFieldCurso.setColumns(10);
		textFieldCurso.setBounds(437, 75, 154, 33);
		panelFormulario.add(textFieldCurso);

		textFieldProfesor = new JTextField();
		textFieldProfesor.setColumns(10);
		textFieldProfesor.setBounds(50, 171, 154, 33);
		panelFormulario.add(textFieldProfesor);

		JLabel lblCiclo = new JLabel("Zikloa");
		lblCiclo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblCiclo.setHorizontalAlignment(SwingConstants.CENTER);
		lblCiclo.setBounds(50, 34, 154, 30);
		panelFormulario.add(lblCiclo);

		JLabel lblModulo = new JLabel("Modulua");
		lblModulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblModulo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblModulo.setBounds(243, 34, 154, 30);
		panelFormulario.add(lblModulo);

		JLabel lblCurso = new JLabel("Kurtsoa");
		lblCurso.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurso.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblCurso.setBounds(437, 34, 154, 30);
		panelFormulario.add(lblCurso);

		JLabel lblProfesor = new JLabel("Irakaslea");
		lblProfesor.setHorizontalAlignment(SwingConstants.CENTER);
		lblProfesor.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblProfesor.setBounds(50, 130, 154, 30);
		panelFormulario.add(lblProfesor);

		beteTxtFields(ordua);
	}
	
	public void beteTxtFields(Horarios hora) {
		
		textFieldCiclo.setText(hora.getModulos().getCiclos().getNombre());
		textFieldModulo.setText(hora.getModulos().getNombre());
		if(hora.getUsers() != null) {
		textFieldProfesor.setText(hora.getUsers().getNombre());
		}
		textFieldCurso.setText(String.valueOf(hora.getModulos().getCurso()));
		
	}
}
