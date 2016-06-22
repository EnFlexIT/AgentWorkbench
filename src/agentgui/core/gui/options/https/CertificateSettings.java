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
 * The Class CertificateSettings.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class CertificateSettings {

    protected KeyStoreSettings keyStoreSettings;
    protected String certificateName;
    protected String certificateAlias;
    protected String validity;
    protected String path;

    /**
     * Gets the certificate name.
     * @return the certificate name
     */
    public String getCertificateName() {
        return certificateName;
    }

    /**
     * Sets the certificate name.
     * @param value the new certificate name
     */
    public void setCertificateName(String value) {
        this.certificateName = value;
    }

    /**
     * Gets the certificate alias.
     * @return the certificate alias
     */
    public String getCertificateAlias() {
        return certificateAlias;
    }

    /**
     * Sets the certificate alias.
     * @param value the new certificate alias
     */
    public void setCertificateAlias(String value) {
        this.certificateAlias = value;
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
     *
     * @param validity the new validity
     */
    public void setValidity(String validity) {
		this.validity = validity;
	}
    
    /**
     * Gets the KeyStoreSettings.
     * @return the KeyStoreSettings
     */
    public KeyStoreSettings getKeyStoreSettings() {
		if(keyStoreSettings == null){
		   keyStoreSettings = new KeyStoreSettings();
		}
    	return keyStoreSettings;
	}
}
