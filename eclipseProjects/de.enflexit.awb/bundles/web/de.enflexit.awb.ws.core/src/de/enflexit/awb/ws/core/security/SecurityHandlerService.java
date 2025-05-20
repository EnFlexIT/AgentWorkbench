package de.enflexit.awb.ws.core.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService;
import de.enflexit.common.ServiceFinder;

/**
 * The Class SecurityHandlerService provides static help methods to deal with registered
 * {@link AwbSecurityHandlerService}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SecurityHandlerService {
	
	private static List<AwbSecurityHandlerService> localAwbSecurityHandlerServiceList; 
	
	/**
	 * Returns the list of available {@link AwbSecurityHandlerService}s provided by the current bundle.
	 * @return the list of local {@link AwbSecurityHandlerService}
	 */
	private static List<AwbSecurityHandlerService> getLocalAwbSecurityHandlerServiceList() {
		if (localAwbSecurityHandlerServiceList==null) {
			localAwbSecurityHandlerServiceList = new ArrayList<AwbSecurityHandlerService>();
			localAwbSecurityHandlerServiceList.add(new SingleUserSecurityService());
			localAwbSecurityHandlerServiceList.add(new SingleApiKeySecurityService());
			localAwbSecurityHandlerServiceList.add(new JwtSingleUserSecurityService());
			localAwbSecurityHandlerServiceList.add(new OIDCSecurityService());
		}
		return localAwbSecurityHandlerServiceList;
	}
	
	/**
	 * Returns the list of local available and OSGI-registered {@link AwbSecurityHandlerService}s.
	 * @return the list of {@link AwbSecurityHandlerService}
	 */
	public static List<AwbSecurityHandlerService> getAwbSecurityHandlerServiceList() {
		
		// --- Get list of services registered over OSGI ------------ 
		List<AwbSecurityHandlerService> serviceList = ServiceFinder.findServices(AwbSecurityHandlerService.class);
		// --- Add local available services -------------------------
		serviceList.addAll(SecurityHandlerService.getLocalAwbSecurityHandlerServiceList());
		return serviceList;
	}
	/**
	 * Returns the list of local available and OSGI-registered {@link AwbSecurityHandlerService}s sorted by the security handler name.
	 * @return the list of {@link AwbSecurityHandlerService}
	 */
	public static List<AwbSecurityHandlerService> getAwbSecurityHandlerServiceListSorted() {
		
		List<AwbSecurityHandlerService> serviceList = getAwbSecurityHandlerServiceList();
		Collections.sort(serviceList, new Comparator<AwbSecurityHandlerService>() {
			@Override
			public int compare(AwbSecurityHandlerService shs1, AwbSecurityHandlerService shs2) {
				return shs1.getSecurityHandlerName().compareTo(shs2.getSecurityHandlerName());
			}
		});
		return serviceList;
	}
	
	/**
	 * Return the {@link AwbSecurityHandlerService} that is specified by its name.
	 *
	 * @param securityHandlerName the security handler name
	 * @return the AwbSecurityHandlerService found or <code>null</code>
	 */
	public static AwbSecurityHandlerService getAwbSecurityHandlerService(String securityHandlerName) {

		if (securityHandlerName.contentEquals(JettySecuritySettings.ID_NO_SECURITY_HANDLER)) return null;
		for (AwbSecurityHandlerService shService : getAwbSecurityHandlerServiceList()) {
			if (shService.getSecurityHandlerName().equals(securityHandlerName)) return shService;
		}
		return null;
	}
	

}
