package de.enflexit.common.p2;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Collections;

import org.eclipse.osgi.service.security.TrustEngine;

public class CustomP2TrustEngine extends TrustEngine {
	
	private KeyStore trustStore;
    private boolean allowUnsigned;
    
    /**
     * Instantiates a new custom P2 trust engine.
     * @param trustStore the trust store to be used
     * @param allowUnsigned specifies if unsigned content should be accepted
     */
    public CustomP2TrustEngine(KeyStore trustStore, boolean allowUnsigned) {
    	
    	if (trustStore==null) {
    		throw new IllegalArgumentException("The trust store must not be null!");
    	}
    	
        this.trustStore = trustStore;
        this.allowUnsigned = allowUnsigned;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#findTrustAnchor(java.security.cert.Certificate[])
	 */
	@Override
	public Certificate findTrustAnchor(Certificate[] chain) throws IOException {
		
		// --- No certificates passed -> unsigned -------------------
		if (chain == null || chain.length == 0) {
            return allowUnsigned ? new DevModeCertificate() : null;
        }
		
		// --- Check the trust store for a matching certificate -----
		try {
            for (Certificate cert : chain) {
                String alias = trustStore.getCertificateAlias(cert);
                if (alias != null) {
                	// --- Certificate found -> trusted -------------
                    return cert;
                }
            }
        } catch (KeyStoreException e) {
            throw new IOException(e);
        }
		
		// --- No matching certificate found -> untrusted -----------
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#doAddTrustAnchor(java.security.cert.Certificate, java.lang.String)
	 */
	@Override
	protected String doAddTrustAnchor(Certificate anchor, String alias) throws IOException, GeneralSecurityException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#doRemoveTrustAnchor(java.security.cert.Certificate)
	 */
	@Override
	protected void doRemoveTrustAnchor(Certificate anchor) throws IOException, GeneralSecurityException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#doRemoveTrustAnchor(java.lang.String)
	 */
	@Override
	protected void doRemoveTrustAnchor(String alias) throws IOException, GeneralSecurityException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#getTrustAnchor(java.lang.String)
	 */
	@Override
	public Certificate getTrustAnchor(String alias) throws IOException, GeneralSecurityException {
		return trustStore.getCertificate(alias);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#getAliases()
	 */
	@Override
	public String[] getAliases() throws IOException, GeneralSecurityException {
		return Collections.list(trustStore.aliases()).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.service.security.TrustEngine#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * A dummy certificate to accept unsigned content in development mode.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private static class DevModeCertificate extends Certificate {

        protected DevModeCertificate() {
            super("DevModeCert");
        }

        /* (non-Javadoc)
         * @see java.security.cert.Certificate#getEncoded()
         */
        @Override
        public byte[] getEncoded() {
            return new byte[0];
        }

        /* (non-Javadoc)
         * @see java.security.cert.Certificate#verify(java.security.PublicKey)
         */
        @Override
        public void verify(PublicKey key) { }

        /* (non-Javadoc)
         * @see java.security.cert.Certificate#verify(java.security.PublicKey, java.lang.String)
         */
        @Override
        public void verify(PublicKey key, String sigProvider) { }

        /* (non-Javadoc)
         * @see java.security.cert.Certificate#toString()
         */
        @Override
        public String toString() {
            return "Accept unsigned content (development mode only!)";
        }

        /* (non-Javadoc)
         * @see java.security.cert.Certificate#getPublicKey()
         */
        @Override
        public PublicKey getPublicKey() {
            return null;
        }
    }

}
