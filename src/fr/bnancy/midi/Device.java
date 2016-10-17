package fr.bnancy.midi;

import java.io.Serializable;

public class Device implements Serializable {
	
	public Device(int pin, int channel, TYPE type, String name) {
		this.pin = pin;
		this.type = type;
		this.channel = channel;
		this.name = name;
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
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	private int channel;
	private String name;

}
