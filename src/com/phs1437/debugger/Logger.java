/**
 * 
 * Utility class to log information, warnings, and errors. It is the
 * responsibility of the client class to not log when it is disabled.
 * 
 * @author phs_winter2014
 * 
 */

package com.phs1437.debugger;
class Logger
{
	/**
	 * 
	 * Log information that does not require elevated attention.
	 * 
	 * @param str
	 *            the String to log
	 */

	public static final void logInfo(String str)
	{
		System.out.println("[I] " + str);
	}

	/**
	 * 
	 * Log a warning that requires some elevated attention.
	 * 
	 * @param str
	 *            the String to log
	 */

	public static final void logWarn(String str)
	{
		System.err.println("[W] " + str);
	}

	/**
	 * 
	 * Log an error that requires ultimate elevated attention. Use sparingly.
	 * 
	 * @param str
	 *            the String to log
	 */

	public static final void logError(String str)
	{
		System.err.println("[E] " + str);
	}
}
