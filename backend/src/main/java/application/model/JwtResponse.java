package application.model;

import java.io.Serializable;
import java.util.Date;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final Date expiresIn;

	public JwtResponse(String jwttoken, Date expiresIn) {
		this.jwttoken = jwttoken;
		this.expiresIn = expiresIn;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public Date getExpiresIn() {
		return expiresIn;
	}
}
