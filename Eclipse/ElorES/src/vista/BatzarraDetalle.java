package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.Funtzioak;
import controlador.zerbitzaria.ReunionesController;
import modelo.Reuniones;
import modelo.Users;
import modelo.enumerados.EstadosEnum;

public class BatzarraDetalle extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEstatua;
	private JTextField textFieldIrakaslea;
	private JTextField textFieldTitulua;
	private JTextField textFieldIkaslea;
	private JTextField textFieldIkastetxea;
	private JTextField textFieldAsuntua;
	private JTextField textFieldAula;
	private JTextField textFieldData;
	ReunionesController reu = new ReunionesController();

	public BatzarraDetalle(Users currentUser, Reuniones batzarra) {
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
		panelFormulario.setBounds(67, 184, 829, 318);
		contentPane.add(panelFormulario);
		panelFormulario.setLayout(null);

		textFieldEstatua = new JTextField();
		textFieldEstatua.setColumns(10);
		textFieldEstatua.setBounds(50, 75, 154, 33);
		panelFormulario.add(textFieldEstatua);

		textFieldIrakaslea = new JTextField();
		textFieldIrakaslea.setColumns(10);
		textFieldIrakaslea.setBounds(243, 75, 154, 33);
		panelFormulario.add(textFieldIrakaslea);

		textFieldTitulua = new JTextField();
		textFieldTitulua.setColumns(10);
		textFieldTitulua.setBounds(50, 171, 154, 33);
		panelFormulario.add(textFieldTitulua);

		JLabel lblEstado = new JLabel("Estatua");
		lblEstado.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstado.setBounds(50, 34, 154, 30);
		panelFormulario.add(lblEstado);

		JLabel lblIrakasela = new JLabel("Irakaslea");
		lblIrakasela.setHorizontalAlignment(SwingConstants.CENTER);
		lblIrakasela.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblIrakasela.setBounds(243, 34, 154, 30);
		panelFormulario.add(lblIrakasela);

		JLabel lblTitulua = new JLabel("Titulua");
		lblTitulua.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulua.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblTitulua.setBounds(50, 130, 154, 30);
		panelFormulario.add(lblTitulua);
		
		textFieldIkaslea = new JTextField();
		textFieldIkaslea.setColumns(10);
		textFieldIkaslea.setBounds(443, 75, 154, 33);
		panelFormulario.add(textFieldIkaslea);
		
		JLabel lblIkasela = new JLabel("Ikaslea");
		lblIkasela.setHorizontalAlignment(SwingConstants.CENTER);
		lblIkasela.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblIkasela.setBounds(443, 34, 154, 30);
		panelFormulario.add(lblIkasela);
		
		textFieldIkastetxea = new JTextField();
		textFieldIkastetxea.setColumns(10);
		textFieldIkastetxea.setBounds(632, 75, 154, 33);
		panelFormulario.add(textFieldIkastetxea);
		
		JLabel lblIkastetxea = new JLabel("Ikastetxea");
		lblIkastetxea.setHorizontalAlignment(SwingConstants.CENTER);
		lblIkastetxea.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblIkastetxea.setBounds(632, 34, 154, 30);
		panelFormulario.add(lblIkastetxea);
		
		textFieldAsuntua = new JTextField();
		textFieldAsuntua.setColumns(10);
		textFieldAsuntua.setBounds(243, 171, 154, 33);
		panelFormulario.add(textFieldAsuntua);
		
		JLabel lblAsuntua = new JLabel("Asuntua");
		lblAsuntua.setHorizontalAlignment(SwingConstants.CENTER);
		lblAsuntua.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAsuntua.setBounds(243, 130, 154, 30);
		panelFormulario.add(lblAsuntua);
		
		textFieldAula = new JTextField();
		textFieldAula.setColumns(10);
		textFieldAula.setBounds(443, 171, 154, 33);
		panelFormulario.add(textFieldAula);
		
		JLabel lblAula = new JLabel("Aula");
		lblAula.setHorizontalAlignment(SwingConstants.CENTER);
		lblAula.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAula.setBounds(443, 130, 154, 30);
		panelFormulario.add(lblAula);
		
		textFieldData = new JTextField();
		textFieldData.setColumns(10);
		textFieldData.setBounds(632, 171, 154, 33);
		panelFormulario.add(textFieldData);
		
		JLabel lblData = new JLabel("Data");
		lblData.setHorizontalAlignment(SwingConstants.CENTER);
		lblData.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblData.setBounds(632, 130, 154, 30);
		panelFormulario.add(lblData);
		
		JButton btnBaieztatu = new JButton("Baieztatu");
		btnBaieztatu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				batzarra.setEstado(EstadosEnum.ACEPTADA.toString().toLowerCase());
				reu.ModifikatuBatzarra(batzarra);
				beteTxtFields(batzarra);
			}
		});
		btnBaieztatu.setBounds(308, 259, 89, 23);
		panelFormulario.add(btnBaieztatu);
		
		JButton btnEzeztatu = new JButton("Ezeztatu");
		btnEzeztatu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				batzarra.setEstado(EstadosEnum.DENEGADA.toString().toLowerCase());
				reu.ModifikatuBatzarra(batzarra);
				beteTxtFields(batzarra);
			}
		});
		btnEzeztatu.setBounds(443, 259, 89, 23);
		panelFormulario.add(btnEzeztatu);
		
		beteTxtFields(batzarra);
		
	}
	
	public void beteTxtFields(Reuniones batzarra) {
		
		textFieldEstatua.setText(batzarra.getEstado());
		textFieldIrakaslea.setText(String.valueOf(batzarra.getUsersByProfesorId().getId()));
		textFieldIkaslea.setText(String.valueOf(batzarra.getUsersByAlumnoId().getId()));
		textFieldTitulua.setText(batzarra.getTitulo());
		textFieldIkastetxea.setText(batzarra.getIdCentro());
		textFieldAsuntua.setText(batzarra.getAsunto());
		textFieldAula.setText(batzarra.getAula());
		textFieldData.setText(String.valueOf(batzarra.getFecha()));
		
	}
	
	
	
}
