package ch.romibi.irc.romibot.irclisteners.commands.data;

import java.io.Serializable;

public class Fact implements Serializable{
	private String name;
	private String value;
	private String owner;
	private String type;
	
	public Fact(String pName, String pValue, String pOwner) {
		name = pName;
		value = pValue;
		owner = pOwner;
		type = "text";
	}
	
	public Fact(String pName, String pValue, String pOwner, String pType) {
		name = pName;
		value = pValue;
		owner = pOwner;
		type = pType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		switch (type) {
		// case "js":
		// return JSPARSER.PARSE(value); <-- pseudo code ... not implemented yet
		case "text":
		default:
			return value;
		}
	}
}
