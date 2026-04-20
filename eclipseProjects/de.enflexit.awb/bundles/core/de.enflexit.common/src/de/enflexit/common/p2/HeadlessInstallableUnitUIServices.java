package de.enflexit.common.p2;

import java.net.URI;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.metadata.spi.IInstallableUnitUIServices;

/**
 * Custom implementation of {@link IInstallableUnitUIServices} for headless applications, 
 * accepting a fixed set of artifact repositories without asking the user.  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class HeadlessInstallableUnitUIServices extends UIServices implements IInstallableUnitUIServices {
	
	private final Set<URI> trustedRepositories;
	
	/**
	 * Instantiates a new headless installable unit UI services.
	 * @param trustedRepositories the trusted repositories
	 */
	public HeadlessInstallableUnitUIServices(Set<URI> trustedRepositories) {
        this.trustedRepositories = trustedRepositories;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.p2.repository.metadata.spi.IInstallableUnitUIServices#getTrustAuthorityInfo(java.util.Map, java.util.Map)
	 */
	@Override
	public TrustAuthorityInfo getTrustAuthorityInfo(Map<URI, Set<IInstallableUnit>> siteIUs, Map<URI, List<Certificate>> siteCertificates) {
		return new TrustAuthorityInfo(trustedRepositories, false, false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.p2.core.UIServices#getUsernamePassword(java.lang.String)
	 */
	@Override
	public AuthenticationInfo getUsernamePassword(String location) {
		// --- Not required here for now ------------------
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.p2.core.UIServices#getUsernamePassword(java.lang.String, org.eclipse.equinox.p2.core.UIServices.AuthenticationInfo)
	 */
	@Override
	public AuthenticationInfo getUsernamePassword(String location, AuthenticationInfo previousInfo) {
		// --- Not required here for now ------------------
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.p2.core.UIServices#getTrustInfo(java.security.cert.Certificate[][], java.lang.String[])
	 */
	@Override
	public TrustInfo getTrustInfo(Certificate[][] untrustedChain, String[] unsignedDetail) {
		// --- Not required here for now ------------------
		return null;
	}

}
