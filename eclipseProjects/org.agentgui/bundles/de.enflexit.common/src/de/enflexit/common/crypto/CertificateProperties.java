/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package de.enflexit.common.crypto;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

/**
 * The Class KeyStoreSettings.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class CertificateProperties {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    protected String keyStoreName;
    protected String password;
    protected String alias;
    protected String commonName;
    protected String organization;
    protected String organizationalUnit;
    protected String cityOrLocality;
    protected String stateOrProvince;
    protected String countryCode;
    protected String path;
    protected String validity;

    /**
     * Gets the key store name.
     * @return the key store name
     */
    public String getKeyStoreName() {
        return keyStoreName;
    }

    /**
     * Sets the key store name.
     * @param value the new key store name
     */
    public void setKeyStoreName(String value) {
        this.keyStoreName = value;
    }

    /**
     * Gets the password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param value the new password
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the alias.
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias.
     * @param value the new alias
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Gets the common name.
     * @return the common name
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * Sets the common name.
     * @param value the new common name
     */
    public void setCommonName(String value) {
        this.commonName = value;
    }

    /**
     * Gets the organization.
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the organization.
     * @param value the new organization
     */
    public void setOrganization(String value) {
        this.organization = value;
    }

    /**
     * Gets the organizational unit.
     * @return the organizational unit
     */
    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    /**
     * Sets the organizational unit.
     * @param value the new organizational unit
     */
    public void setOrganizationalUnit(String value) {
        this.organizationalUnit = value;
    }

    /**
     * Gets the city or locality.
     * @return the city or locality
     */
    public String getCityOrLocality() {
        return cityOrLocality;
    }

    /**
     * Sets the city or locality.
     * @param value the new city or locality
     */
    public void setCityOrLocality(String value) {
        this.cityOrLocality = value;
    }

    /**
     * Gets the state or province.
     * @return the state or province
     */
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * Sets the state or province.
     * @param value the new state or province
     */
    public void setStateOrProvince(String value) {
        this.stateOrProvince = value;
    }

    /**
     * Gets the coutry code.
     * @return the coutry code
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the coutry code.
     * @param value the new coutry code
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the path.
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     * @param value the new path
     */
    public void setPath(String value) {
        this.path = value;
    }
    
    /**
     * Gets the validity.
     * @return the validity
     */
    public String getValidity() {
		return validity;
	}
    
    /**
     * Sets the validity.
     * @param validity the new validity
     */
    public void setValidity(String validity) {
		this.validity = validity;
	}

    public void parseFromCertificate(X509Certificate cert) {
		String provider = cert.getSubjectX500Principal().getName();

		LdapName ldapDN;
		try {
			ldapDN = new LdapName(provider);
			HashMap<String,String> providerParts = new HashMap<String,String>();
			for(Rdn rdn: ldapDN.getRdns()) {
				if(rdn.getValue() instanceof String){
					providerParts.put(rdn.getType(), (String) rdn.getValue());
				} else {
					// multi-valued RDN
//					System.err.println(rdn.getType());
//					System.err.println(rdn.getValue());
				}
			}				

	        this.setCommonName(providerParts.get("CN"));
	        this.setOrganizationalUnit(providerParts.get("OU"));
	        this.setOrganization(providerParts.get("O"));
	        this.setCityOrLocality(providerParts.get("L"));
	        this.setStateOrProvince(providerParts.get("ST"));
	        this.setCountryCode(providerParts.get("C"));
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
      
        Date date = ((X509Certificate) cert).getNotAfter();
        this.setValidity(DATE_FORMAT.format(date));
    	
    }
}
