package fr.bnancy.midi.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import fr.bnancy.midi.Device;
import fr.bnancy.midi.Device.TYPE;
import fr.bnancy.midi.MidiApplication;
import fr.bnancy.midi.util.MidiCommon;
import javax.swing.BoxLayout;

public class MainWindow extends JFrame {
	
	JComboBox<String> midiSelect;
	JComboBox<String> comSelect;
	JScrollPane cc_panel;
	JScrollPane pc_panel;
	MidiApplication app;
	
	public MainWindow(MidiApplication app) {
		
		this.app = app;
		
		setAutoRequestFocus(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MIDI");
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblSelectAMidi = new JLabel("Select a MIDI port :");
		lblSelectAMidi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectAMidi.setBounds(10, 11, 143, 19);
		getContentPane().add(lblSelectAMidi);
		
		midiSelect = new JComboBox<String>();
		midiSelect.setBounds(10, 36, 153, 20);
		getContentPane().add(midiSelect);
		
		JButton btnRefreshList = new JButton("Refresh list");
		btnRefreshList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshList();
			}
		});
		
		btnRefreshList.setBounds(10, 59, 89, 23);
		getContentPane().add(btnRefreshList);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(185, 0, 1, 400);
		getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBackground(Color.BLACK);
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
		
		ssid = new JTextField();
		panel.add(ssid, "4, 2, fill, default");
		ssid.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password :");
		panel.add(lblPassword, "2, 4, right, default");
		
		password = new JPasswordField();
		panel.add(password, "4, 4, fill, default");
		password.setColumns(10);
		
		JLabel lblServerAddress = new JLabel("Server :");
		panel.add(lblServerAddress, "2, 6, right, default");
		
		serverIp = new JTextField();
		panel.add(serverIp, "4, 6, fill, default");
		serverIp.setColumns(10);
		
		JButton btnTestConfiguration = new JButton("Test configuration");
		btnTestConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnTestConfiguration.setBounds(33, 224, 120, 23);
		getContentPane().add(btnTestConfiguration);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.BLACK);
		separator_2.setBackground(Color.BLACK);
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
		
		comSelect = new JComboBox<String>();
		panel_1.add(comSelect, "4, 2, fill, default");
		
		JButton btnTset = new JButton("Test communication");
		btnTset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean success = app.pingSerial((String) comSelect.getSelectedItem());
				JOptionPane.showMessageDialog(MainWindow.this, "" + success);
			}
		});
		panel_1.add(btnTset, "2, 4, 3, 1, fill, default");
		
		JButton btnConfigure = new JButton("Configure");
		panel_1.add(btnConfigure, "2, 6, 3, 1, fill, default");
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(186, 30, 555, 370);
		getContentPane().add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		pc_panel = new JScrollPane();
		pc_panel.setBorder(new MatteBorder(1, 0, 0, 1, (Color) new Color(0, 0, 0)));
		panel_2.add(pc_panel);
		
		JLabel lblNewLabel = new JLabel("Preset Change :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pc_panel.setColumnHeaderView(lblNewLabel);
		
		JPanel panel_3 = new JPanel();
		pc_panel.setViewportView(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		cc_panel = new JScrollPane();
		cc_panel.setBorder(new MatteBorder(1, 0, 0, 1, (Color) new Color(0, 0, 0)));
		panel_2.add(cc_panel);
		
		JLabel lblControlChange = new JLabel("Control Change :");
		lblControlChange.setHorizontalAlignment(SwingConstants.CENTER);
		lblControlChange.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cc_panel.setColumnHeaderView(lblControlChange);
		
		JPanel panel_4 = new JPanel();
		cc_panel.setViewportView(panel_4);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(185, 0, 555, 30);
		getContentPane().add(panel_6);
		
		JLabel lblDevicesConfiguration = new JLabel("Devices configuration :");
		lblDevicesConfiguration.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_6.add(lblDevicesConfiguration);
	}

	private static final long serialVersionUID = -502108666750029894L;
	private JTextField ssid;
	private JPasswordField password;
	private JTextField serverIp;
	
	public void refreshList() {
		midiSelect.removeAllItems();
		for(String name : MidiCommon.listDevices(true,  false)) {
			midiSelect.addItem(name);
		}
	}
	
	public void addLogMessage(String msg) {
		System.out.println(msg);
	}
	
	public String getSelectedMidiDevice() {
		return (String) midiSelect.getSelectedItem();
	}

	public void addNewComPort(String portName) {
		if(((DefaultComboBoxModel<String>)comSelect.getModel()).getIndexOf(portName) == -1)
			comSelect.addItem(portName);
	}

	public void setDevices(ArrayList<Device> devices) {
		for(Device device : devices) {
			if(device.getType() != TYPE.UNKNOWN) {
				if(device.getType() == TYPE.ANALOG || device.getType() == TYPE.CONTROL_CHANGE) {
					cc_panel.setViewportView(updateViewport((JPanel) cc_panel.getViewport().getView(), device));
				} else if(device.getType() == TYPE.PRESET_CHANGE) {
					pc_panel.setViewportView(updateViewport((JPanel) pc_panel.getViewport().getView(), device));
				}
			}
		}
	}

	private Component updateViewport(JPanel component, Device device) {
		component.add(createDeviceComponent(device));
		return component;
	}

	private JPanel createDeviceComponent(Device device) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("" + device.getName()));
		return panel;
	}

	public void removePort(String portName) {
		((DefaultComboBoxModel<String>)comSelect.getModel()).removeElement(portName);
	}
}
