package jade.mtp.http;

import jade.core.Profile;
import jade.mtp.InChannel;
import jade.mtp.MTPException;
import jade.mtp.TransportAddress;
import jade.mtp.http.HTTPAddress;
import jade.mtp.http.HTTPSocketFactory;
import jade.mtp.http.MessageTransportProtocol;

public class ProxiedMTP extends MessageTransportProtocol {

	// called if NO mtp_http parameters are given
	public TransportAddress activate(InChannel.Dispatcher disp, Profile p) throws MTPException {
		TransportAddress privateMTPAddress;
		TransportAddress publicMTPAddress = null;
		try {
			// open listening socket only on localhost/private address
			privateMTPAddress = new HTTPAddress("127.0.0.1", 7778, false);
			super.activate(disp, privateMTPAddress, p);
			System.out.println("Activated MTP at private address" + privateMTPAddress);

			// use provided parameters for public address (JADE thinks, it is running the MTP on this address and reports it in outgoing messages)
			publicMTPAddress = new HTTPAddress("132.252.61.127", 443, true); // FIXME implement a method to provide the address/port/protocol, e.g. a new profile parameter mtp_http_proxy
			System.out.println("But report public address " + publicMTPAddress);

			// use https for outgoing connections like usual, because the nginx in front of the opposite party JADE is expecting it
			HTTPSocketFactory.getInstance().configure(p, (HTTPAddress) publicMTPAddress);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return publicMTPAddress;
	}

	// called if mtp_http parameters are given
	public void activate(InChannel.Dispatcher disp, TransportAddress ta, Profile p) throws MTPException {
		// because the return is void, the address in the calling method cannot be overwritten and so it's old (the provided) address will be in use (which is not, what we want)
		System.err.println("Do not call this method! It cannot work. Do not provide explicit mtp_http parameters.");
	}

	public String[] getSupportedProtocols() {
		String[] protocols = { "http", "https" };

		return protocols;
	}

}
