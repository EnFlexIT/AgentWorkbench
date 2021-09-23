package de.enflexit.common.swing;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The Class TimeZoneIdAndOffsets describes a single time zone and its zoneId.
 * It provides some static access methods to get list of the available ZoneId's
 * in different sorting orders.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeZoneIdAndOffsets {

    private ZoneId zoneID;
    private Integer zoneOffsetLocalInHours;
    private Integer zoneOffsetUtcInHours;

    /**
     * Instantiates a new zone id and offset.
     * 
     * @param zoneId the zone id
     */
    public TimeZoneIdAndOffsets(ZoneId zoneId) {
	this.zoneID = zoneId;
    }

    /**
     * Returns the zone ID.
     * 
     * @return the zone ID
     */
    public ZoneId getZoneID() {
	return zoneID;
    }

    /**
     * Returns the zone offset to the local time.
     * 
     * @return the zone offset local
     */
    public Integer getZoneOffsetToLocalInHours() {
	if (zoneOffsetLocalInHours == null) {

	    LocalDateTime ldt = LocalDateTime.now();

	    ZoneId localZoneId = ZoneId.systemDefault();
	    ZoneId otherZoneId = this.getZoneID();

	    ZonedDateTime localDateTime = ZonedDateTime.of(ldt, localZoneId);
	    ZonedDateTime otherDateTime = ZonedDateTime.of(ldt, otherZoneId);

	    int localDiffToUtc = localDateTime.getOffset().getTotalSeconds();
	    int otherDiffToUtc = otherDateTime.getOffset().getTotalSeconds();
	    int zoneDiff = otherDiffToUtc - localDiffToUtc;

	    zoneOffsetLocalInHours = zoneDiff / (60 * 60);
	}
	return zoneOffsetLocalInHours;
    }

    /**
     * Return the zone offset to UTC.
     * 
     * @return the zone offset UTC
     */
    public Integer getZoneOffsetToUtcInHours() {
	if (zoneOffsetUtcInHours == null) {
	    Instant instant = Instant.now();
	    ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, this.getZoneID());
	    zoneOffsetUtcInHours = zdt.getOffset().getTotalSeconds() / (60 * 60);
	}
	return zoneOffsetUtcInHours;
    }

    /**
     * Returns the zone offset as string; e.g. in the form +2:00 or-7:00.
     * 
     * @param zoneOffsetInHours the zone Off set In Hours
     * @return the zone offset as string
     */
    public String getZoneOffsetAsString(Integer zoneOffsetInHours) {

	if (zoneOffsetInHours == null)
	    return null;

	String zoString = "";
	if (zoneOffsetInHours >= 0)
	    zoString = "+";
	return zoString + zoneOffsetInHours + ":00";
    }

    /**
     * Returns a customized zone description without offset descriptions.
     * 
     * @return the zone description
     */
    public String getZoneDescription() {
	return this.getZoneDescription(false, false, true);
    }

    /**
     * Returns a customized zone description including offset descriptions if
     * configured.
     *
     * @param includingOffsetToLocal the including offset to local
     * @param includingOffsetToUTC   the including offset to UTC
     * @param withLeadingOffset      the with leading offset to place the
     * @return the zone description
     */
    public String getZoneDescription(boolean includingOffsetToLocal, boolean includingOffsetToUTC,
	    boolean withLeadingOffset) {

	// --- Get zone ID --------------------------------------
	String zoneDescription = this.getZoneID().getId();

	// --- Configure the offset description -----------------
	String offsetDescription = "";
	if (includingOffsetToLocal == true || includingOffsetToUTC == true) {
	    offsetDescription = "[";
	    if (includingOffsetToLocal == true) {
		offsetDescription += "Local Time " + this.getZoneOffsetAsString(this.getZoneOffsetToLocalInHours());
	    }

	    if (includingOffsetToUTC == true) {
		if (offsetDescription.length() > 1) {
		    offsetDescription += ", ";
		}
		offsetDescription += "UTC " + this.getZoneOffsetAsString(this.getZoneOffsetToUtcInHours());
	    }
	    offsetDescription += "]";
	}

	// --- Return with leading offset description? ----------
	if (withLeadingOffset == true) {
	    return (offsetDescription + " " + zoneDescription).trim();
	} else {
	    return (zoneDescription + " " + offsetDescription).trim();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

	if (!(obj instanceof TimeZoneIdAndOffsets))
	    return false;

	TimeZoneIdAndOffsets comp = (TimeZoneIdAndOffsets) obj;
	return comp.getZoneID().getId().equals(this.getZoneID().getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return this.getZoneDescription(true, true, true);
    }

    // ----------------------------------------------------------------------------------
    // --- From here static help methods can be found
    // -----------------------------------
    // ----------------------------------------------------------------------------------
    /**
     * Returns the available {@link ZoneId}s with its offsets to local time and UTC.
     * 
     * @return the available zone ids with offsets
     */
    public static List<TimeZoneIdAndOffsets> getAvailableZoneIdsWithOffsets() {

	List<String> zoneIDList = new ArrayList<>(ZoneId.getAvailableZoneIds());
	Collections.sort(zoneIDList);

	List<TimeZoneIdAndOffsets> zoneIdAndOffsetList = new ArrayList<>();
	for (String id : zoneIDList) {
	    ZoneId zoneId = ZoneId.of(id);
	    zoneIdAndOffsetList.add(new TimeZoneIdAndOffsets(zoneId));
	}
	return zoneIdAndOffsetList;
    }

    /**
     * Returns the available {@link ZoneId}s with its offsets to local time and UTC
     * that matches the specified saechPhrase.
     *
     * @param searchPhrase the search phrase
     * @return the TimeZoneIdAndOffsets
     */
    public static List<TimeZoneIdAndOffsets> getZoneIdsWithOffsets(String searchPhrase) {

	if (searchPhrase == null || searchPhrase.isEmpty() == true)
	    return getAvailableZoneIdsWithOffsets();

	List<TimeZoneIdAndOffsets> zoneIDListFound = new ArrayList<>();
	List<TimeZoneIdAndOffsets> zoneIDList = getAvailableZoneIdsWithOffsets();

	// --- Split search phrase by blanks --------------
	String[] searchArray = searchPhrase.toLowerCase().split(" ");

	// --- Filter for searchArray elements ------------
	for (int i = 0; i < zoneIDList.size(); i++) {

	    TimeZoneIdAndOffsets tzo = zoneIDList.get(i);
	    String tzoID = tzo.getZoneID().getId().toLowerCase();
	    boolean addToFilteredList = true;

	    // --- Search for all expressions -------------
	    for (int j = 0; j < searchArray.length; j++) {
		String searchExpresion = searchArray[j];
		if (tzoID.indexOf(searchExpresion) == -1) {
		    addToFilteredList = false;
		    break;
		}
	    }
	    // --- Add to filtered list? ------------------
	    if (addToFilteredList == true) {
		zoneIDListFound.add(tzo);
	    }
	}
	return zoneIDListFound;
    }

    /**
     * Returns a comparator for sorting with respect to ZoneId.
     * 
     * @return the comparator zone id
     */
    public static Comparator<TimeZoneIdAndOffsets> getComparatorForZoneId() {

	Comparator<TimeZoneIdAndOffsets> comp = new Comparator<TimeZoneIdAndOffsets>() {
	    @Override
	    public int compare(TimeZoneIdAndOffsets tz1, TimeZoneIdAndOffsets tz2) {
		return tz1.getZoneID().getId().compareTo(tz2.getZoneID().getId());
	    }
	};
	return comp;
    }

    /**
     * Returns a comparator for sorting with respect to the time offset to the local
     * time.
     * 
     * @return the comparator for offset to local time
     */
    public static Comparator<TimeZoneIdAndOffsets> getComparatorForOffsetToLocalTime() {

	Comparator<TimeZoneIdAndOffsets> comp = new Comparator<TimeZoneIdAndOffsets>() {
	    @Override
	    public int compare(TimeZoneIdAndOffsets tz1, TimeZoneIdAndOffsets tz2) {
		if (tz1.getZoneOffsetToLocalInHours() == tz2.getZoneOffsetToLocalInHours()) {
		    return tz1.getZoneID().getId().compareTo(tz2.getZoneID().getId());
		}
		return tz1.getZoneOffsetToLocalInHours().compareTo(tz2.getZoneOffsetToLocalInHours());
	    }
	};
	return comp;
    }

    /**
     * Returns a comparator for sorting with respect to the time offset to UTC.
     * 
     * @return the comparator for offset to UTC
     */
    public static Comparator<TimeZoneIdAndOffsets> getComparatorForOffsetToUTC() {

	Comparator<TimeZoneIdAndOffsets> comp = new Comparator<TimeZoneIdAndOffsets>() {
	    @Override
	    public int compare(TimeZoneIdAndOffsets tz1, TimeZoneIdAndOffsets tz2) {
		if (tz1.getZoneOffsetToUtcInHours() == tz2.getZoneOffsetToUtcInHours()) {
		    return tz1.getZoneID().getId().compareTo(tz2.getZoneID().getId());
		}
		return tz1.getZoneOffsetToUtcInHours().compareTo(tz2.getZoneOffsetToUtcInHours());
	    }
	};
	return comp;
    }

}
