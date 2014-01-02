/*
 * Debugger Interface
 * ~~~~~~~~~~~~~~~~~~~
 * Outlines the general project structure: the required methods, fields, etc.
 *
 * Copyright: (c) 2013 by Diwakar Ganesan, Kent Ma, Jonathan Ni
 * License: spaghetti, see LICENSE for details
 */
package com.phs1437.debugger;

public interface Debugger 
{
    /*
     * Creates the basic test.
     * Has 3 arguments:
     *    1 - A variable
     *    2 - Expected value of the variable
     *    3 - Expected string to be logged
     *
     * Example Usage:
     *     BuiltinTester.expecting(inputValue, 0, "return false")
     *
     * TODO: There is probably a better way of doing this. Find what that is.
     */
    public void expecting(int inputValue, int possibleValue, String expectedString);
    public void expecting(byte inputValue, byte possibleValue, String expectedString);
    public void expecting(short inputValue, short possibleValue, String expectedString);
    public void expecting(long inputValue, long possibleValue, String expectedString);
    public void expecting(float inputValue, float possibleValue, String expectedString);
    public void expecting(double inputValue, double possibleValue, String expectedString);
    public void expecting(boolean inputValue, boolean possibleValue, String expectedString);
    public void expecting(char inputValue, char possibleValue, String expectedString);
    public void expecting(String inputValue, String possibleValue, String expectedString);
    public void expecting(int[] inputValue, int[] possibleValue, String expectedString);
     
    /*
     * Logs :field:`expectedString`
     * Example usage:
     *   if (inputValue > 2) {
     *       BuiltInTester.log("return true");
     *       return true;
     *   }
     */
    public void log(String expectedString);
    
    /*
     * Enables and disables the BuiltInTester
     * Has 0 arguments
     *
     * Example Usage:
     *     BuiltInTester.enable();
     */
     
    public void enable();
  
    public void disable();
}
