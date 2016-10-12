package fr.bnancy.midi.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

import fr.bnancy.midi.util.MidiCommon;

public class MainWindow extends JFrame {
	
	JComboBox<String> comboBox;
	JTextPane textPane;
	
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MIDI");
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblSelectAMidi = new JLabel("Select a MIDI port :");
		lblSelectAMidi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectAMidi.setBounds(10, 39, 143, 19);
		getContentPane().add(lblSelectAMidi);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(163, 40, 153, 20);
		getContentPane().add(comboBox);
		
		JButton btnRefreshList = new JButton("Refresh list");
		btnRefreshList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(String name : MidiCommon.listDevices(true,  false)) {
					comboBox.addItem(name);
				}
			}
		});
		btnRefreshList.setBounds(20, 69, 89, 23);
		getContentPane().add(btnRefreshList);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(454, 173, 276, 232);
		getContentPane().add(textPane);
		
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setBounds(569, 148, 46, 14);
		getContentPane().add(lblLogs);
	}

	private static final long serialVersionUID = -502108666750029894L;
	
	public void refreshList() {
		comboBox.removeAllItems();
		for(String name : MidiCommon.listDevices(true,  false)) {
			comboBox.addItem(name);
		}
	}
	
	public void addLogMessage(String msg) {
		this.textPane.setText(new StringBuilder(this.textPane.getText()).append(msg + "\n").toString());
	}
	
	public String getSelectedMidiDevice() {
		return (String) comboBox.getSelectedItem();
	}
}
