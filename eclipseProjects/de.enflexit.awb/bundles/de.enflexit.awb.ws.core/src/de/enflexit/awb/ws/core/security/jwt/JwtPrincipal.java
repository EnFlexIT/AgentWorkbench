package de.enflexit.awb.ws.core.security.jwt;

import java.security.Principal;

public class JwtPrincipal implements Principal {

	private String name;
	private String jwtToken;
	private String role;
	
	public JwtPrincipal(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the JWT token.
	 * @return the JWT token
	 */
	public String getJwtToken() {
		return jwtToken;
	}
	/**
	 * Sets the JWT token.
	 * @param jwtToken the new JWT token
	 */
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	/**
	 * Returns the role.
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * Sets the role.
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role= role;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
        return getName();
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof JwtPrincipal)) {
            return false;
        } else {
            return getName().equals(((JwtPrincipal) o).getName());
        }
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode() {
        return 37 * getName().hashCode();
    }

}
