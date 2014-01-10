package com.phs1437.debugger;

/*
 * Debugger Interface
 * ~~~~~~~~~~~~~~~~~~~
 * Outlines the general project structure: the required methods, fields, etc.
 *
 * Copyright: (c) 2013 by Diwakar Ganesan, Kent Ma, Jonathan Ni
 * License: spaghetti, see LICENSE for details
 */


public interface Debugger
{
	/*
	 * Creates the basic test. Has 3 arguments: 1 - A variable 2 - Expected
	 * value of the variable 3 - Expected string to be logged
	 * 
	 * Example Usage: BuiltinTester.expecting(inputValue, 0, "return false", )
	 */

	public void expecting(Object mutableInputValue,
			Object mutablePossibleValue, String expectedString,
			String variableID, String functionID, Class<?> type);

	public void expecting(Object[] mutableInputValue,
			Object[] mutablePossibleValue, String expectedString,
			String variableID, String functionID, Class<?> type, int numInputs, int numOutputs);

	/*
	 * Logs :field:`expectedString` Example usage: if (inputValue > 2) {
	 * BuiltInTester.log("return true"); return true; }
	 */
	public void log(String expectedString);

	/*
	 * Enables and disables the BuiltInTester Has 0 arguments
	 * 
	 * Example Usage: BuiltInTester.enable();
	 */

	public void enable();

	public void disable();
}