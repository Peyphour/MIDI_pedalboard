package fr.bnancy.midi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import fr.bnancy.midi.Device;
import fr.bnancy.midi.Device.TYPE;
import fr.bnancy.midi.MidiApplication;
import fr.bnancy.midi.util.MidiCommon;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -502108666750029894L;
	private JTextField ssid;
	private JPasswordField password;
	private JTextField serverIp;
	private JComboBox<String> midiSelect;
	private JComboBox<String> comSelect;
	private JPanel cc_panel;
	private JPanel pc_panel;
	private MidiApplication app;
	private ArrayList<Device> devices;
	private boolean pingWifi;

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
		ssid.setText("netx2");
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
		serverIp.setText("192.168.1.121");
		panel.add(serverIp, "4, 6, fill, default");
		serverIp.setColumns(10);

		JButton btnTestConfiguration = new JButton("Test configuration");
		btnTestConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				app.pingWifi((String) comSelect.getSelectedItem(), ssid.getText(), new String(password.getPassword()), serverIp.getText());
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(10000);
							if(!pingWifi)
								pingWifiTimeout();
							else
								pingWifi = false;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
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

		pc_panel = new JPanel();
		pc_panel.setBorder(new MatteBorder(1, 0, 0, 1, (Color) new Color(0, 0, 0)));
		panel_2.add(pc_panel);
		pc_panel.setLayout(new BorderLayout(0, 0));

		cc_panel = new JPanel();
		cc_panel.setBorder(new MatteBorder(1, 0, 0, 1, (Color) new Color(0, 0, 0)));
		panel_2.add(cc_panel);
		cc_panel.setLayout(new BorderLayout(0, 0));

		cc_panel.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
				"Control Change",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		pc_panel.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
				"Program Change",
				TitledBorder.CENTER,
				TitledBorder.TOP));

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(185, 0, 555, 30);
		getContentPane().add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel lblDevicesConfiguration = new JLabel("Devices configuration :");
		lblDevicesConfiguration.setHorizontalAlignment(SwingConstants.CENTER);
		lblDevicesConfiguration.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_6.add(lblDevicesConfiguration, BorderLayout.CENTER);

		JButton btnAddDevice = new JButton("Add device");
		btnAddDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] unaffectedDevices = devices.stream()
						.filter(d -> d.getType() == Device.TYPE.UNKNOWN)
						.map(d -> d.getName())
						.toArray(String[]::new);
				String selected = (String) JOptionPane.showInputDialog(MainWindow.this, 
						"Which device ?",
						"",
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						unaffectedDevices, 
						unaffectedDevices[0]);
				for(Device d : devices) {
					if(d.getName().equals(selected))
						d.setType(TYPE.ANALOG);
				}

				setDevices(devices);
			}
		});
		panel_6.add(btnAddDevice, BorderLayout.EAST);

		Component horizontalStrut = Box.createHorizontalStrut(85);
		panel_6.add(horizontalStrut, BorderLayout.WEST);

	}

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

	public void removePort(String portName) {
		((DefaultComboBoxModel<String>)comSelect.getModel()).removeElement(portName);
	}

	public void setDevices(ArrayList<Device> devices) {

		this.devices = devices;

		cc_panel.removeAll();
		pc_panel.removeAll();

		String[] title = new String[] {
				"Enabled", "Name", "Number"	
		};

		ArrayList<Device> cc_data = new ArrayList<>(), pc_data = new ArrayList<>();

		for(Device device : devices) {
			if(device.getType() != TYPE.UNKNOWN) {
				if(device.getType() == TYPE.ANALOG || device.getType() == TYPE.CONTROL_CHANGE) {
					cc_data.add(device);
				} else if(device.getType() == TYPE.PRESET_CHANGE) {
					pc_data.add(device);
				}
			}
		}

		JTable cc_table = new JTable(new TableModel(cc_data, title));
		JTable pc_table = new JTable(new TableModel(pc_data, title));

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if(value instanceof JCheckBox)
					return (JCheckBox) value;
				else if(value instanceof JTextField) {
					return (JTextField) value;
				}
				else
					return this;
			}
		};

		cc_table.setDefaultRenderer(JComponent.class, renderer);
		pc_table.setDefaultRenderer(JComponent.class, renderer);

		cc_panel.add(new JScrollPane(cc_table));
		pc_panel.add(new JScrollPane(pc_table));

		cc_panel.revalidate();
		pc_panel.revalidate();
	}

	class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = -7722580004929375864L;
		private ArrayList<Device> data;
		private String[] title;

		//Constructeur
		public TableModel(ArrayList<Device> cc_data, String[] title){
			this.data = cc_data;
			this.title = title;
		}

		//Retourne le nombre de colonnes
		public int getColumnCount() {
			return this.title.length;
		}

		@Override
		public String getColumnName(int index) {
			return this.title[index];
		}

		//Retourne le nombre de lignes
		public int getRowCount() {
			return this.data.size();
		}

		//Retourne la valeur à l'emplacement spécifié
		public Object getValueAt(int row, int col) {
			if(col == 0) 
				return data.get(row).isEnabled();
			else if(col == 1)
				return data.get(row).getName();
			else
				return data.get(row).getNumber();
		} 

		public void setValueAt(Object object, int row, int col) {
			if(col == 0) 
				data.get(row).setEnabled((boolean) object);
			else if(col == 2 && (int) object <= 127 && (int) object >= 0) {
				data.get(row).setNumber((int) object);
				data.get(row).setEnabled(true);
				this.fireTableDataChanged();
			}
		}

		@Override 
		public boolean isCellEditable(int row, int col) {
			return col == 2 || col == 0;
		}

		@Override
		public Class<?> getColumnClass(int col) {
			if(col == 0)
				return Boolean.class;
			else if(col == 1)
				return String.class;
			else
				return Integer.class;
		}
	}

	public void pingWifiSuccess() {
		this.pingWifi = true;
		System.out.println("success");
	}

	public void pingWifiTimeout() {
		System.out.println("timeout");
		app.pingWifiTimeout();
	}
}
