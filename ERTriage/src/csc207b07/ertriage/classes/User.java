package csc207b07.ertriage.classes;

import java.io.Serializable;

/**
 * @author uri An abstract class User has a String username, password and a
 *         String[] name.
 */
public abstract class User implements Serializable {

	private static final long serialVersionUID = 3064195083976762780L;
	/**
	 * This User's first name, middle name and last name, a User's username and
	 * password.
	 */
	private String username, password, role;
	private String[] name;

	/**
	 * Constructs a User with String[] name, String username and password. Since
	 * this is an abstract class, it cannot be constructed, but these methods
	 * will exists in classes that inherit them.
	 * 
	 * @param name
	 * @param username
	 * @param password
	 */
	public User(String[] name, String username, String password, String role) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	/**
	 * Returns this User's name.
	 * 
	 * @return the name of this User.
	 */
	public String[] getName() {
		return name;
	}

	/**
	 * Sets the name of this User.
	 * 
	 * @param name
	 *            the new name of this User.
	 */
	public void setName(String[] name) {
		this.name = name;
	}

	/**
	 * Returns this User's username.
	 * 
	 * @return the username of this User.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Return this User's password.
	 * 
	 * @return the password of this User.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of this User.
	 * 
	 * @param password
	 *            the new password of this User.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the username of this User.
	 * 
	 * @param username
	 *            the new username of this User.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the role of the user.
	 * 
	 * @return the role of the user.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the patient's time of arrival.
	 * 
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
