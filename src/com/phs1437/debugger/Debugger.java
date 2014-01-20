package com.phs1437.debugger;

/**
 * Debugger Interface
 * ~~~~~~~~~~~~~~~~~~~
 * Outlines the general project structure: the required methods, fields, etc.
 *
 * @author phs_winter2013, Diwakar Ganesan, Kent Ma, Jonathan Ni
 */
public interface Debugger
{
	/**
	 * Creates the basic test
     * * @param mutableInputValue    The variable to be tested.
     *
     * @param mutablePossibleValue A possible value for the variable.
     *
     * @param expectedString       Specifies the string needed to be logged in 
     *                             order to pass the test. Use {@link 
     *                             #log(String)} to actually log the string 
     *                             specified for this. 
     *
     * @param variableID           A unique string that identifies the variable 
     *                             inputted. This is used to differentiate 
     *                             between different tests simultaneously 
     *                             running for {@link #log(String)}
     *
     * @param functionID           A unique string to identify the function the 
     *                             tested code resided in.
     *
     * @param type                 The data type of the variable to be tested.
     * 
     * @param numInputs
     *
     * @param numOutputs
	 */

	public void expecting(Object mutableInputValue,
			Object mutablePossibleValue, String expectedString,
			String variableID, String functionID, Class<?> type);

	public void expecting(Object[] mutableInputValue,
			Object[] mutablePossibleValue, String expectedString,
			String variableID, String functionID, Class<?> type, int numInputs, 
            int numOutputs);

	/**
	 * Logs `expectedString` 
     *
     * Example usage:
     *     if (inputValue > 2) {
     *         BuiltInTester.log("return true");
     *         return true;
     *     }
     *
     * @param expectedString String to log. See {@link #expected(Object, Object,
     *                       String, String, String, Class<?>)} for the use of
     *                       this string.
     *
	 */
	public void log(String expectedString);

	/**
	 * Enables and disables the BuiltInTester 
	 * Example Usage:
     *     BuiltInTester.enable();
	 */
	public void enable();
	public void disable();
}
