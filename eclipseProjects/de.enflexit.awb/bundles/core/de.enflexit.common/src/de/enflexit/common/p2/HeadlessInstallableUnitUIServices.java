package de.enflexit.common.p2;

import java.net.URI;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.metadata.spi.IInstallableUnitUIServices;

public class HeadlessInstallableUnitUIServices implements IInstallableUnitUIServices {
	
	private final Set<URI> trustedRepositories;
	
	public HeadlessInstallableUnitUIServices(Set<URI> trustedRepositories) {
        this.trustedRepositories = trustedRepositories;
    }

	@Override
	public TrustAuthorityInfo getTrustAuthorityInfo(Map<URI, Set<IInstallableUnit>> siteIUs, Map<URI, List<Certificate>> siteCertificates) {
		return new TrustAuthorityInfo(trustedRepositories, false, false);
	}

}
