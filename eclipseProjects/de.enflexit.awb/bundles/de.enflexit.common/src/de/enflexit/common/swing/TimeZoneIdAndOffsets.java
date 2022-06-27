package de.enflexit.common.swing;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * The Class TimeZoneIdAndOffsets describes a single time zone and its zoneId.
 * It provides some static access methods to get list of the available ZoneId's
 * in different sorting orders.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeZoneIdAndOffsets {

    private ZoneId zoneId;
    private TimeZone timeZone;
    private Integer zoneOffsetLocalInHours;
    private Integer zoneOffsetUtcInHours;

	
    /**
	 * Instantiates a new zone id and offset.
	 * 
	 * @param zoneId the zone id
	 */
	public TimeZoneIdAndOffsets(ZoneId zoneId) {
		this(zoneId, null);
	}
    /**
	 * Instantiates a new zone id and offset.
	 *
	 * @param zoneId the zone id
	 * @param timeZone the time zone
	 */
	public TimeZoneIdAndOffsets(ZoneId zoneId, TimeZone timeZone) {
		this.zoneId = zoneId;
		this.timeZone = timeZone;
	}
	
	/**
	 * Returns the zone ID.
	 * @return the zone ID
	 */
	public ZoneId getZoneId() {
		return zoneId;
	}
	/**
	 * Returns the {@link TimeZone} of the current ZoneId.
	 * @return the time zone
	 * 
	 * @see #getZoneId()
	 */
	public TimeZone getTimeZone() {
		if (timeZone==null) {
			try {
				timeZone = TimeZone.getTimeZone(this.getZoneId());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return timeZone;
	}
	
	/**
	 * Returns the zone offset to the local time.
	 * @return the zone offset local
	 */
	public Integer getZoneOffsetToLocalInHours() {
		if (zoneOffsetLocalInHours == null) {

			LocalDateTime ldt = LocalDateTime.now();

			ZoneId localZoneId = ZoneId.systemDefault();
			ZoneId otherZoneId = this.getZoneId();

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
	 * @return the zone offset UTC
	 */
	public Integer getZoneOffsetToUtcInHours() {
		if (zoneOffsetUtcInHours == null) {
			Instant instant = Instant.now();
			ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, this.getZoneId());
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
		if (zoneOffsetInHours == null) return null;
		String zoString = "";
		if (zoneOffsetInHours >= 0) zoString = "+";
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
	 * Returns a customized zone description including offset descriptions if configured.
	 *
	 * @param includingOffsetToLocal the including offset to local
	 * @param includingOffsetToUTC   the including offset to UTC
	 * @param withLeadingOffset      the with leading offset to place the
	 * @return the zone description
	 */
	public String getZoneDescription(boolean includingOffsetToLocal, boolean includingOffsetToUTC, boolean withLeadingOffset) {

		// --- Get zone ID --------------------------------------
		String zoneDescription = this.getTimeZoneName();

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

	/**
	 * Returns the unique time zone name that is build from the ID's of the {@link ZoneId} and the {@link TimeZone}.
	 * @return the unique time zone name
	 */
	public String getTimeZoneName() {
		String zoneID = this.getZoneId().getId();
		String timeZoneID = this.getTimeZone().getID();
		if (zoneID.equals(timeZoneID)==true) {
			return zoneID;
		} else {
			return timeZoneID + " (" + zoneID + ")";
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getTimeZoneName().hashCode();
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof TimeZoneIdAndOffsets)) return false;
		
		TimeZoneIdAndOffsets comp = (TimeZoneIdAndOffsets) obj;
		boolean equals = comp.getTimeZoneName().equals(this.getTimeZoneName()); 
		if (equals==false) {
			// --- Additionally check, TimeZone ID's ------
			equals = comp.getZoneId().getId().equals(this.getZoneId().getId());
		}
		return equals;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getZoneDescription(true, true, true);
	}

	
	// ----------------------------------------------------------------------------------
	// --- From here static help methods can be found -----------------------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Returns the available {@link ZoneId}s with its offsets to local time and UTC.
	 * 
	 * @return the available zone ids with offsets
	 */
	public static List<TimeZoneIdAndOffsets> getAvailableZoneIdsWithOffsets() {

		// --- Merge together all available ZoneId's and TimeZoneId's --------- 
		Set<String> zoneIdSet = new HashSet<>(ZoneId.getAvailableZoneIds());
		Set<String> timeZoneIdSet = Set.of(TimeZone.getAvailableIDs());

		// --------------------------------------------------------------------
		// --- Debugging and test area ----------------------------------------
		// --------------------------------------------------------------------
		boolean debug = false;
		if (debug==true) {
			// --- Check which time zone ID's are new or already available ----
			List<String> timeZoneIDListNew   = new ArrayList<String>();
			for (String timeZoneId : timeZoneIdSet) {
				if (zoneIdSet.contains(timeZoneId)==false) {
					timeZoneIDListNew.add(timeZoneId);
				}
			}
			System.out.println("New elements from list of java.util.TimeZone.getAvailableIDs():  " + timeZoneIDListNew.toString());
		}
		// --------------------------------------------------------------------
		
		
		// --- Set the list to further work on -------------------------------- 
		zoneIdSet.addAll(timeZoneIdSet);
		
		// --- Fill the set with elements to return ---------------------------
		HashSet<TimeZoneIdAndOffsets> zoneIdAndOffsetSet = new HashSet<>();
		for (String id : zoneIdSet) {
			TimeZone timeZone = null;
			ZoneId zoneId = getZoneId(id);
			if (zoneId==null) {
				// --- Get ZoneId by using the TimeZone methods ---------------
				timeZone = TimeZone.getTimeZone(id);
				zoneId = timeZone.toZoneId();
				if (debug==true) System.out.println("ID '" + id + "' to TimeZone '" + timeZone.getID() + "' to ZoneId '" + zoneId + "'");
			} else {
				// --- Get TimeZone for current ZoneId ------------------------
				timeZone = TimeZone.getTimeZone(zoneId);
			}
			zoneIdAndOffsetSet.add(new TimeZoneIdAndOffsets(zoneId, timeZone));
		}
		
		// --- Prepare the list to return ------------------------------------- 
		List<TimeZoneIdAndOffsets> zoneIdAndOffsetList = new ArrayList<TimeZoneIdAndOffsets>(zoneIdAndOffsetSet);
		Collections.sort(zoneIdAndOffsetList, getComparatorForZoneName());
		return zoneIdAndOffsetList;
	}
	/**
	 * Return the zone id based on the specified id .
	 *
	 * @param id the id of the ZoneId
	 * @return the ZoneId or null
	 */
	private static ZoneId getZoneId(String id) {
		ZoneId zoneId = null;
		try {
			zoneId = ZoneId.of(id);
		} catch (DateTimeException ex) {
			// --- Don't show exceptions ---
		}
		return zoneId;
	}

	/**
	 * Returns the available {@link ZoneId}s with its offsets to local time and UTC
	 * that matches the specified saechPhrase.
	 *
	 * @param searchPhrase the search phrase
	 * @return the TimeZoneIdAndOffsets
	 */
	public static List<TimeZoneIdAndOffsets> getZoneIdsWithOffsets(String searchPhrase) {

		if (searchPhrase == null || searchPhrase.isEmpty() == true) return getAvailableZoneIdsWithOffsets();

		List<TimeZoneIdAndOffsets> zoneIDListFound = new ArrayList<>();
		List<TimeZoneIdAndOffsets> zoneIDList = getAvailableZoneIdsWithOffsets();

		// --- Split search phrase by blanks --------------
		String[] searchArray = searchPhrase.toLowerCase().split(" ");

		// --- Filter for searchArray elements ------------
		for (int i = 0; i < zoneIDList.size(); i++) {

			TimeZoneIdAndOffsets tzo = zoneIDList.get(i);
			String tzoName = tzo.getTimeZoneName().toLowerCase();
			boolean addToFilteredList = true;

			// --- Search for all expressions -------------
			for (int j = 0; j < searchArray.length; j++) {
				String searchExpresion = searchArray[j];
				if (tzoName.indexOf(searchExpresion) == -1) {
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
	 * @return the comparator zone id
	 */
	public static Comparator<TimeZoneIdAndOffsets> getComparatorForZoneName() {

		Comparator<TimeZoneIdAndOffsets> comp = new Comparator<TimeZoneIdAndOffsets>() {
			@Override
			public int compare(TimeZoneIdAndOffsets tz1, TimeZoneIdAndOffsets tz2) {
				return tz1.getTimeZoneName().compareTo(tz2.getTimeZoneName());
			}
		};
		return comp;
	}

	/**
	 * Returns a comparator for sorting with respect to the time offset to the local time.
	 * @return the comparator for offset to local time
	 */
	public static Comparator<TimeZoneIdAndOffsets> getComparatorForOffsetToLocalTime() {

		Comparator<TimeZoneIdAndOffsets> comp = new Comparator<TimeZoneIdAndOffsets>() {
			@Override
			public int compare(TimeZoneIdAndOffsets tz1, TimeZoneIdAndOffsets tz2) {
				if (tz1.getZoneOffsetToLocalInHours() == tz2.getZoneOffsetToLocalInHours()) {
					return tz1.getTimeZoneName().compareTo(tz2.getTimeZoneName());
				}
				return tz1.getZoneOffsetToLocalInHours().compareTo(tz2.getZoneOffsetToLocalInHours());
			}
		};
		return comp;
	}

	/**
	 * Returns a comparator for sorting with respect to the time offset to UTC.
	 * @return the comparator for offset to UTC
	 */
	public static Comparator<TimeZoneIdAndOffsets> getComparatorForOffsetToUTC() {

		Comparator<TimeZoneIdAndOffsets> comp = new Comparator<TimeZoneIdAndOffsets>() {
			@Override
			public int compare(TimeZoneIdAndOffsets tz1, TimeZoneIdAndOffsets tz2) {
				if (tz1.getZoneOffsetToUtcInHours() == tz2.getZoneOffsetToUtcInHours()) {
					return tz1.getTimeZoneName().compareTo(tz2.getTimeZoneName());
				}
				return tz1.getZoneOffsetToUtcInHours().compareTo(tz2.getZoneOffsetToUtcInHours());
			}
		};
		return comp;
	}

}
