package de.enflexit.awb.ws.core.util;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;

import de.enflexit.common.NumberHelper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The Class MonitoringFilter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MonitoringFilter implements Filter {

	public static final String IS_PRINT_MONITORING_OUTPUT = "isPrintMonitoringOutput";
	public static final String EXCLUDE_PATH_ARRAY = "excludedPathArray";
	public static final String NO_OF_ELEMENTS_FOR_AVERAGE = "noOfElementsForAverage";
	
	private boolean defaultIsPrintMonitoringOutput = false;

	private FilterConfig filterConfig;
	private Boolean isPrintMonitoringOutput;
	private HashSet<String> excludedUriForSessionExtension;
	private AverageMonitor averageMonitor;

	private long lastDebugOutput;
	
	
	/* (non-Javadoc)
	 * @see jakarta.servlet.Filter#init(jakarta.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	/**
	 * Checks if is prints the monitoring output.
	 * @return true, if is prints the monitoring output
	 */
	protected boolean isPrintMonitoringOutput() {
		if (isPrintMonitoringOutput==null) {
			String paraIsPrintMonitoringOutput = this.filterConfig.getInitParameter(IS_PRINT_MONITORING_OUTPUT);
			if (paraIsPrintMonitoringOutput==null || paraIsPrintMonitoringOutput.isBlank()==true) {
				isPrintMonitoringOutput	= this.defaultIsPrintMonitoringOutput;
			} else {
				isPrintMonitoringOutput = Boolean.parseBoolean(paraIsPrintMonitoringOutput);
			}
		}
		return isPrintMonitoringOutput;
	}
	/**
	 * Returns the excluded URI's for the monitoring.
	 * @return the monitoring-excluded UTI's as HashSet
	 */
	protected HashSet<String> getExcludedUriForSessionExtension() {
		if (excludedUriForSessionExtension==null) {
			excludedUriForSessionExtension = new HashSet<>();
			
			String excludePathConfig = this.filterConfig.getInitParameter(EXCLUDE_PATH_ARRAY);
			if (excludePathConfig!=null && excludePathConfig.isBlank()==false) {
				String[] excludePathArray = excludePathConfig.split(";");
				for (String excludePath : excludePathArray) {
					if (excludePath.isBlank()==false) {
						excludedUriForSessionExtension.add(excludePath);
					}
				}
			}
			
		}
		return excludedUriForSessionExtension;
	}
	/**
	 * Returns the average monitor.
	 * @return the average monitor
	 */
	protected AverageMonitor getAverageMonitor() {
		if (averageMonitor==null) {
			Integer no4Avg = NumberHelper.parseInteger(this.filterConfig.getInitParameter(EXCLUDE_PATH_ARRAY));
			if (no4Avg==null) {
				no4Avg = 20;
			}
			averageMonitor = new AverageMonitor(no4Avg);
		}
		return averageMonitor;
	}

	
	/**
	 * Returns the current principal.
	 *
	 * @param sRequest the s request
	 * @return the principal
	 */
	private Principal getPrincipal(HttpServletRequest sRequest) {
		Principal principal = null;
		try {
			principal = sRequest.getUserPrincipal();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return principal;
	}
	
	/* (non-Javadoc)
	 * @see jakarta.servlet.Filter#doFilter(jakarta.servlet.ServletRequest, jakarta.servlet.ServletResponse, jakarta.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// --- Check request ----------------------------------------
		if (request instanceof HttpServletRequest sRequest) {

			String userID = null;
			String reqURI = sRequest.getRequestURI();
			
			if (this.getExcludedUriForSessionExtension().contains(reqURI.toLowerCase())==false) {
				
				// --- Get the registered user / Principal ----------
				Principal principal = this.getPrincipal(sRequest);
				if (principal!=null) {
					userID = principal.getName();
				}
			}
			// --- Print a monitoring message -----------------------
			String monitoringMsg = "Requested URL: " + reqURI + (userID!=null ? "\tUserID: " + userID : "");
			this.print(monitoringMsg);
		} 
		
		
		// ----------------------------------------------------------
		// --- Forward to further filter jobs -----------------------
		chain.doFilter(request, response);
	}
	
	/**
	 * Debug prints the specified message to the console.
	 * @param message the message
	 */
	private void print(String message) {
		this.print(message, false);
	}
	/**
	 * Prints the specified message to the console.
	 *
	 * @param message the message
	 * @param isError the is error
	 */
	private void print(String message, boolean isError) {
		if (message==null || message.isBlank()==true) return;
		
		long now = System.currentTimeMillis();
		long diffToLastOutput = 0;
		if (this.lastDebugOutput > 0) {
			diffToLastOutput = now - this.lastDebugOutput;
		}
		this.lastDebugOutput = now;

		// --- Prepare message parts --------------------------------
		String diffBlueprint = "     ";
		String diffOutput = String.valueOf(diffToLastOutput);
		if (diffOutput.length()<diffBlueprint.length()) {
			diffOutput = diffBlueprint.substring(0, diffBlueprint.length() - diffOutput.length()) + diffOutput;
		}
		
		@SuppressWarnings("unused")
		String avgDescription = "|avg: " + this.getAverageMonitor().nextAverage(diffToLastOutput);
		
		// --- Prepare overall message ------------------------------
		String consoleMsg = "[" + this.getClass().getSimpleName() + "].[" + diffOutput + "] " + message;
		if (isError==true) {
			System.err.println(consoleMsg);
		} else {
			System.out.println(consoleMsg);
		}
	}
	
	
	/**
	 * The Class AverageMonitor.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class AverageMonitor {

		private final long[] buffer;
		private int head = 0;
		private int size = 0;
		private long sum = 0;

		public AverageMonitor(int noOfElements) {
			this.buffer = new long[noOfElements];
		}

		public long nextAverage(long val) {
			if (size < buffer.length) {
				// Fill up the buffer initially
				buffer[size] = val;
				sum += val;
				size++;
			} else {
				// Replace the oldest value and update the sum
				sum -= buffer[head];
				buffer[head] = val;
				sum += val;

				// Advance the circular head pointer
				head = (head + 1) % buffer.length;
			}
			return Math.round((double) sum / size);
		}
	}
	
	
}
