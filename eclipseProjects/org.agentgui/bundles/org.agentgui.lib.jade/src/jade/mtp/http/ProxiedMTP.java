package jade.mtp.http;

import java.net.URL;

import jade.core.Profile;
import jade.mtp.InChannel;
import jade.mtp.MTPException;
import jade.mtp.TransportAddress;
import jade.mtp.http.HTTPAddress;
import jade.mtp.http.HTTPSocketFactory;
import jade.mtp.http.MessageTransportProtocol;

public class ProxiedMTP extends MessageTransportProtocol {
	public static final String PROFILE_PREFIX = "jade_mtp_proxiedhttps_";
	public static final String PROFILE_PRIVATE_PROTOCOL = PROFILE_PREFIX + "privateProtocol";
	public static final String PROFILE_PRIVATE_ADDRESS = PROFILE_PREFIX + "privateAddress";
	public static final String PROFILE_PRIVATE_PORT = PROFILE_PREFIX + "privatePort";
	public static final String PROFILE_PRIVATE_PATH = PROFILE_PREFIX + "privatePath";
	public static final String PROFILE_PUBLIC_PROTOCOL = PROFILE_PREFIX + "publicProtocol";
	public static final String PROFILE_PUBLIC_ADDRESS = PROFILE_PREFIX + "publicAddress";
	public static final String PROFILE_PUBLIC_PORT = PROFILE_PREFIX + "publicPort";
	public static final String PROFILE_PUBLIC_PATH = PROFILE_PREFIX + "publicPath";

	public static final String PROTOCOL_HTTP = "http";
	public static final String PROTOCOL_HTTPS = "https";
	public static final String LOOPBACK_ADDRESS = "127.0.0.1";
	public static final String DEFAULT_PATH = "/acc";
	public static final int DEFAULT_PRIVATEPORT = 7778;
	public static final int DEFAULT_PUBLICPORT = 443;

	protected String privateProtocol = PROTOCOL_HTTPS;
	protected String privateAddress = LOOPBACK_ADDRESS;
	protected int privatePort = DEFAULT_PRIVATEPORT;
	protected String privatePath = DEFAULT_PATH;

	protected String publicProtocol = PROTOCOL_HTTPS;
	protected String publicAddress = null;
	protected int publicPort = DEFAULT_PUBLICPORT;
	protected String publicPath = DEFAULT_PATH;
	protected TransportAddress privateMTPAddress = null;
	protected TransportAddress publicMTPAddress = null;

	protected void parseParameters(Profile p) {
		String tmp = null;
		if ((tmp = p.getParameter(PROFILE_PRIVATE_PROTOCOL, null)) != null) {
			privateProtocol = tmp;
		}
		if ((tmp = p.getParameter(PROFILE_PRIVATE_ADDRESS, null)) != null) {
			privateAddress = tmp;
		}
		if ((tmp = p.getParameter(PROFILE_PRIVATE_PORT, null)) != null) {
			privatePort = Integer.parseInt(tmp);
		}
		if ((tmp = p.getParameter(PROFILE_PRIVATE_PATH, null)) != null) {
			privatePath = tmp;
		}
		if ((tmp = p.getParameter(PROFILE_PUBLIC_PROTOCOL, null)) != null) {
			publicProtocol = tmp;
		}
		if ((tmp = p.getParameter(PROFILE_PUBLIC_ADDRESS, null)) != null) {
			publicAddress = tmp;
		}
		if ((tmp = p.getParameter(PROFILE_PUBLIC_PORT, null)) != null) {
			publicPort = Integer.parseInt(tmp);
		}
		if ((tmp = p.getParameter(PROFILE_PUBLIC_PATH, null)) != null) {
			publicPath = tmp;
		}
	}

	// called if NO mtp_http parameters are given
	public TransportAddress activate(InChannel.Dispatcher disp, Profile p) throws MTPException {

		try {
			parseParameters(p);

			// open listening socket only on localhost/private/loopback address
			privateMTPAddress = new HTTPAddress(new URL(privateProtocol, privateAddress, privatePort, privatePath));
			super.activate(disp, privateMTPAddress, p);
			System.out.println("Activated MTP at private address " + privateMTPAddress);

			// use provided parameters for public address (JADE thinks, it is running the MTP on this address and reports it in outgoing messages)
			publicMTPAddress = new HTTPAddress(new URL(publicProtocol, publicAddress, publicPort, publicPath));
			System.out.println("But reporting public address " + publicMTPAddress);

			// use public address with protocol for outgoing connections like usual, because the nginx in front of the opposite party JADE is expecting it (e.g. https)
			HTTPSocketFactory.getInstance().configure(p, (HTTPAddress) publicMTPAddress);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return publicMTPAddress;
	}

	@Override
	public void deactivate(TransportAddress ta) throws MTPException {
		if (ta.toString().equals(publicMTPAddress.toString())) {
			super.deactivate(privateMTPAddress);
		} else {
			System.err.println("Deactivating ProxiedMTP failed for " + ta + " (original publicMTPAddress=" + publicMTPAddress + " privateMTPAddress=" + privateMTPAddress);
			super.deactivate(ta);
		}
	}

	// called if mtp_http parameters are given
	public void activate(InChannel.Dispatcher disp, TransportAddress ta, Profile p) throws MTPException {
		// Do not call this method! It cannot work.
		// Because the return is void, the address in the calling method cannot be overwritten and so it's old (the provided) address will be in use (which is not, what we want)
		System.err.println("ProxiedMTP failed: Do not provide explicit mtp_http parameters.");
	}

	public String[] getSupportedProtocols() {
		String[] protocols = { PROTOCOL_HTTP, PROTOCOL_HTTPS };
		return protocols;
	}
}