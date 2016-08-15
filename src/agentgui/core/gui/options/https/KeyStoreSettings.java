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
package agentgui.core.gui.options.https;

/**
 * The Class KeyStoreSettings.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class KeyStoreSettings {

    protected String keyStoreName;
    protected String password;
    protected String alias;
    protected String fullName;
    protected String organization;
    protected String orginazationalUnit;
    protected String cityOrLocality;
    protected String stateOrProvince;
    protected String coutryCode;
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
     * Gets the full name.
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     * @param value the new full name
     */
    public void setFullName(String value) {
        this.fullName = value;
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
     * Gets the orginazational unit.
     * @return the orginazational unit
     */
    public String getOrginazationalUnit() {
        return orginazationalUnit;
    }

    /**
     * Sets the orginazational unit.
     * @param value the new orginazational unit
     */
    public void setOrginazationalUnit(String value) {
        this.orginazationalUnit = value;
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
    public String getCoutryCode() {
        return coutryCode;
    }

    /**
     * Sets the coutry code.
     * @param value the new coutry code
     */
    public void setCoutryCode(String value) {
        this.coutryCode = value;
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

}
