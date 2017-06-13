package net.bean;

import javafx.beans.property.BooleanProperty;

import javafx.beans.property.SimpleBooleanProperty;

import javafx.beans.property.SimpleStringProperty;

import javafx.beans.property.StringProperty;

public class AMessage {

	private final BooleanProperty invited;

	private final StringProperty firstName;

	private final StringProperty lastName;

	private final StringProperty email;

	public AMessage(boolean invited, String fName, String lName, String email) {

		this.invited = new SimpleBooleanProperty(invited);

		this.firstName = new SimpleStringProperty(fName);

		this.lastName = new SimpleStringProperty(lName);

		this.email = new SimpleStringProperty(email);

	}

	public BooleanProperty invitedProperty() {
		return invited;
	}

	public StringProperty firstNameProperty() {
		return firstName;
	}

	public StringProperty lastNameProperty() {
		return lastName;
	}

	public StringProperty emailProperty() {
		return email;
	}

}