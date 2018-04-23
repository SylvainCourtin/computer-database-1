package com.excilys.cdb.persistence.exceptions;

public class DAOConfigurationException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3740823104873709188L;

	public DAOConfigurationException(String string) {
		super(string);
	}

	public DAOConfigurationException(String string, Exception e) {
		super(string, e);
	}

}
