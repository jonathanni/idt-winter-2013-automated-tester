package com.phs1437.debugger;

/*
 * TODO: Implement tracking of symbols in code
 * TODO: Implement custom streams (such as outputting to file)
 * TODO: Implement code profiling (optional)
 */

public class BuiltInTester implements Debugger
{
    /**
    Indicates whether or not the builtin tester is enabled.
    */

    private boolean isEnabled;

    /**
    Default constructor.
    Options
      enabled: false
    */

    public BuiltInTester()
    {
      this(false);
    }

    /**
    Constructor with one argument.
    Options
      enabled: choice
    */

    public BuiltInTester(boolean enable)
    {
      if(enable)
        enable();
      else
        disable();
    }

    public void expecting(int inputValue, int possibleValue, String expectedString)
    {
    }

    public void expecting(byte inputValue, byte possibleValue, String expectedString)
    {
    }

    public void expecting(short inputValue, short possibleValue, String expectedString)
    {
    }

    public void expecting(long inputValue, long possibleValue, String expectedString)
    {
    }

    public void expecting(float inputValue, float possibleValue, String expectedString)
    {
    }

    public void expecting(double inputValue, double possibleValue, String expectedString)
    {
    }

    public void expecting(boolean inputValue, boolean possibleValue, String expectedString)
    {
    }

    public void expecting(char inputValue, char possibleValue, String expectedString)
    {
    }

    public void expecting(String inputValue, String possibleValue, String expectedString)
    {
    }

    public void expecting(int[] inputValue, int[] possibleValue, String expectedString)
    {
    }

    /*
     * Example usage:
     *   if (inputValue > 2) {
     *       BuiltInTester.log("return true");
     *       return true;
     *   }
     *   
     */

    /**
    Log what is expected in a variable and a String to debug.
    */
    public void log(String expectedString)
    {
      System.out.println(expectedString);
      // TODO: Implement piping to any OutputStream
    }

    /**
    Enable the builtin tester.
    Any functions called with the builtin tester will register after it is enabled.
    */
    public void enable()
    {
      isEnabled = true;
    }

    /**
    Disable the builtin tester.
    Any functions called with the builtin tester will be ignored after it is disabled.
    */
    public void disable()
    {
      isEnabled = false;
    }
}
