package fr.bnancy.midi;

import java.io.Serializable;

public class Device implements Serializable {
	
	public Device(int pin, int channel, TYPE type, String name, boolean enabled) {
		this.pin = pin;
		this.type = type;
		this.number = channel;
		this.name = name;
		this.setEnabled(enabled);
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public TYPE getType() {
		return type;
	}
	public void setType(TYPE type) {
		this.type = type;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public enum TYPE {
		UNKNOWN, PRESET_CHANGE, CONTROL_CHANGE, ANALOG;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1725254505103627728L;
	
	private int pin;
	private TYPE type;
	private int number;
	private String name;
	private boolean enabled;
}
