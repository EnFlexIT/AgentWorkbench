package de.enflexit.awb.ws.core.security.jwt;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class JwtPrincipal.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtPrincipal implements Principal {

	private List<String> jwtTokenList;
	private int maxNumberOfJwtToken = 3;
	
	private String name;
	private String role;
	
	/**
	 * Instantiates a new JwtPrincipal.
	 * @param name the name
	 */
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
	 * Returns the list of JWT token for the current principal.
	 * @return the jwtTokenList
	 */
	List<String> getJwtTokenList() {
		if (jwtTokenList==null) {
			jwtTokenList = new ArrayList<>();
		}
		return jwtTokenList;
	}
	
	/**
	 * Returns the last JWT token in the list allowed token.
	 * @return the JWT token
	 */
	public String getJwtToken() {
		// --- Always return the last token ----- 
		return this.getJwtTokenList().get(this.getJwtTokenList().size()-1);
	}
	
	/**
	 * Sets the JWT token.
	 * @param jwtToken the new JWT token
	 */
	public void addJwtToken(String jwtToken) {
		this.getJwtTokenList().add(jwtToken);
	}
	/**
	 * Return the list of invalid jwt token that are to be used as delete candidates.
	 * @return the invalid jwt token
	 */
	public List<String> getInvalidJwtToken() {
		
		List<String> invalidJWTs = new ArrayList<>();
		int endIndex = this.getJwtTokenList().size() - maxNumberOfJwtToken - 1;
		if (endIndex>=0) {
			for (int i = 0; i <= endIndex; i++) {
				invalidJWTs.add(this.getJwtTokenList().get(i));
			}
		}
		return invalidJWTs;
	}
	/**
	 * Removes the specified JWT token.
	 * @param jwtToken the JWT token
	 */
	public void removeJwtToken(String jwtToken) {
		this.getJwtTokenList().remove(jwtToken);
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
