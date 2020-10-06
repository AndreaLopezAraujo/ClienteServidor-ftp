import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class InterfasCliente {
	static boolean k = false;

	public static void main(String[] args) throws UnknownHostException, IOException, NoSuchAlgorithmException {
		ClienteTCP ct = new ClienteTCP();

		JFrame f = new JFrame("TCP Client");
		f.setSize(500, 200);
		f.setLocation(300, 200);
		final JLabel l = new JLabel("Cantidad de conexiones simultaneas:");
		JSpinner s = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
		((DefaultEditor) s.getEditor()).getTextField().setEditable(false);
		String[] list = { "100MB", "250MB" };
		JComboBox<String> cb = new JComboBox<String>(list);
		f.setLayout(new GridLayout(4, 2));
		s.setBounds(70, 70, 50, 40);
		f.getContentPane().add(l);
		f.getContentPane().add(s);
		f.getContentPane().add(new JLabel("Archivo a descargar:"));
		f.getContentPane().add(cb);
		f.getContentPane().add(new JLabel("IP del servidor:"));
		JTextField tf = new JTextField("localhost");
		f.getContentPane().add(tf);
		final JButton button = new JButton("Enviar peticion");
		f.getContentPane().add(BorderLayout.SOUTH, button);
		Socket socket = ct.funcConect(tf.getText());
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ct.funcMandar(((Integer) s.getValue()).intValue(), (String) cb.getSelectedItem(), socket);
					k = true;
				} catch (NoSuchAlgorithmException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		ct.funcRecibir(socket);
	}
}
