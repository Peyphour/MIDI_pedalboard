package fr.bnancy.midi.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import fr.bnancy.midi.util.MidiCommon;

public class MainWindow extends JFrame {
	
	JComboBox<String> comboBox;
	JComboBox<String> comboBox_1;
	
	public MainWindow() {
		setAutoRequestFocus(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MIDI");
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblSelectAMidi = new JLabel("Select a MIDI port :");
		lblSelectAMidi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectAMidi.setBounds(10, 11, 143, 19);
		getContentPane().add(lblSelectAMidi);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(10, 36, 153, 20);
		getContentPane().add(comboBox);
		
		JButton btnRefreshList = new JButton("Refresh list");
		btnRefreshList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshList();
			}
		});
		
		btnRefreshList.setBounds(10, 59, 89, 23);
		getContentPane().add(btnRefreshList);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(185, 0, 1, 400);
		getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 90, 185, 1);
		getContentPane().add(separator_1);
		
		JLabel lblNetworkConfiguration = new JLabel("Network configuration :");
		lblNetworkConfiguration.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNetworkConfiguration.setBounds(10, 102, 160, 19);
		getContentPane().add(lblNetworkConfiguration);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(10, 132, 160, 81);
		getContentPane().add(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblSsid = new JLabel("SSID :");
		panel.add(lblSsid, "2, 2, right, default");
		
		textField = new JTextField();
		panel.add(textField, "4, 2, fill, default");
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password :");
		panel.add(lblPassword, "2, 4, right, default");
		
		textField_1 = new JPasswordField();
		panel.add(textField_1, "4, 4, fill, default");
		textField_1.setColumns(10);
		
		JLabel lblServerAddress = new JLabel("Server :");
		panel.add(lblServerAddress, "2, 6, right, default");
		
		textField_2 = new JTextField();
		panel.add(textField_2, "4, 6, fill, default");
		textField_2.setColumns(10);
		
		JButton btnTestConfiguration = new JButton("Test configuration");
		btnTestConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnTestConfiguration.setBounds(33, 224, 120, 23);
		getContentPane().add(btnTestConfiguration);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 258, 186, 1);
		getContentPane().add(separator_2);
		
		JLabel lblArduinoCommunication = new JLabel("Arduino communication :");
		lblArduinoCommunication.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblArduinoCommunication.setBounds(10, 264, 169, 19);
		getContentPane().add(lblArduinoCommunication);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 294, 165, 93);
		getContentPane().add(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblComPort = new JLabel("COM port :");
		panel_1.add(lblComPort, "2, 2, right, default");
		
		comboBox_1 = new JComboBox<String>();
		panel_1.add(comboBox_1, "4, 2, fill, default");
		
		JButton btnTset = new JButton("Test communication");
		btnTset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panel_1.add(btnTset, "2, 4, 3, 1, fill, default");
		
		JButton btnConfigure = new JButton("Configure");
		panel_1.add(btnConfigure, "2, 6, 3, 1, fill, default");
	}

	private static final long serialVersionUID = -502108666750029894L;
	private JTextField textField;
	private JPasswordField textField_1;
	private JTextField textField_2;
	
	public void refreshList() {
		comboBox.removeAllItems();
		for(String name : MidiCommon.listDevices(true,  false)) {
			comboBox.addItem(name);
		}
	}
	
	public void addLogMessage(String msg) {
		System.out.println(msg);
	}
	
	public String getSelectedMidiDevice() {
		return (String) comboBox.getSelectedItem();
	}

	public void addNewComPort(String portName) {
		if(((DefaultComboBoxModel<String>)comboBox_1.getModel()).getIndexOf(portName) == -1)
			comboBox_1.addItem(portName);
		
	}

	public void cleanComPorts() {
		comboBox_1.removeAllItems();
	}
}
