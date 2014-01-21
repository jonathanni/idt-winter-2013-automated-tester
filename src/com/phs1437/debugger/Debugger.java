package com.phs1437.debugger;

/**
 * Debugger Interface ~~~~~~~~~~~~~~~~~~~
 * 
 * Outlines the general project structure: the required methods, fields, etc.
 * 
 * @author phs_winter2013, Diwakar Ganesan, Kent Ma, Jonathan Ni
 */
interface Debugger
{
	/**
	 * Creates the basic test, which takes as much information as possible from
	 * the code to be tested, and puts it away for further use (see the log
	 * function).
	 * 
	 * @param mutablePossibleValue
	 *            A possible value for the variable.
	 * 
	 * @param expectedString
	 *            Specifies the string needed to be logged in order to pass the
	 *            test. Use {@link #log(String)} to actually log the string
	 *            specified for this.
	 * 
	 * @param variableID
	 *            A unique string that identifies the variable inputted. This is
	 *            used to differentiate between different tests simultaneously
	 *            running for {@link #log(String)}
	 * 
	 * @param functionID
	 *            A unique string to identify the function the tested code
	 *            resided in.
	 * 
	 * @param type
	 *            The data type of the variable to be tested. Accepts array
	 *            types.
	 * 
	 * @return 0 on success and 1 on failure
	 * 
	 */

	public int expecting(Object mutableInputValue, Object mutablePossibleValue,
			String expectedString, String variableID, String functionID,
			Class<?> type);

	public int expecting(Object[] mutableInputValue,
			Object[] mutablePossibleValue, String expectedString,
			String variableID, String functionID, Class<?> type);

	/**
	 * Logs `expectedString`
	 * 
	 * Example usage: if (inputValue > 2) { BuiltInTester.log("return true");
	 * return true; }
	 * 
	 * @param variableID
	 *            the ID of the variable (given to the expecting function)
	 *            associated with the test
	 * @param expectedString
	 *            String to log. See {@link #expected(Object, Object, String,
	 *            String, String, Class<?>)} for the use of this string.
	 * @return 0 on success, 1 on failure, and -1 on test case not added
	 * 
	 */
	public int log(String variableID, String expectedString);

	/**
	 * Enables and disables the BuiltInTester Example Usage:
	 * BuiltInTester.enable();
	 */
	public void enable();

	public void disable();
}
