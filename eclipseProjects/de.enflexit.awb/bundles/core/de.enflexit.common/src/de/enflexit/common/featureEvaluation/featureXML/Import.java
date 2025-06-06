//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.12.07 at 08:57:04 PM CET 
//


package de.enflexit.common.featureEvaluation.featureXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.NormalizedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "import")
public class Import {

    @XmlAttribute(name = "plugin")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String plugin;
    @XmlAttribute(name = "feature")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String feature;
    @XmlAttribute(name = "version")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String version;
    @XmlAttribute(name = "match")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String match;
    @XmlAttribute(name = "patch")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String patch;

    /**
     * Gets the value of the plugin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlugin() {
        return plugin;
    }

    /**
     * Sets the value of the plugin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlugin(String value) {
        this.plugin = value;
    }

    /**
     * Gets the value of the feature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeature() {
        return feature;
    }

    /**
     * Sets the value of the feature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeature(String value) {
        this.feature = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the match property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatch() {
        if (match == null) {
            return "compatible";
        } else {
            return match;
        }
    }

    /**
     * Sets the value of the match property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatch(String value) {
        this.match = value;
    }

    /**
     * Gets the value of the patch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatch() {
        if (patch == null) {
            return "false";
        } else {
            return patch;
        }
    }

    /**
     * Sets the value of the patch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatch(String value) {
        this.patch = value;
    }

}
