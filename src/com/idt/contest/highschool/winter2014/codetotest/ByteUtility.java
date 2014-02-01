package com.idt.contest.highschool.winter2014.codetotest;

import com.idt.contest.highschool.winter2014.framework.FrameworkConstants;
import com.phs1437.debugger.BuiltInTester;

/**
 * Class containing byte related utility methods
 */
public class ByteUtility {

	/**
	 * Method to translate a byte to a binary string
	 * This algorithm returns the 2's compliment for negative bytes
	 * @param b - byte to translate to binary string (e.g. 57)
	 * @return - String binary representation of byte, 2's compliment if the byte is negative
	 */
    public static BuiltInTester tester = new BuiltInTester(true);
	public String byteToBinytaryString(byte b) {
		
		byte remainder = 0;
		byte number = b;
		String binaryRepresentation = "";
		
		tester.expecting(b, (byte)0, "0", "b", "byteToBinytaryString(byte)", Byte.class, String.class);
        tester.expecting(b, (byte)12, "1100", "b", "byteToBinytaryString(byte)", Byte.class, String.class);
        tester.expecting(b, (byte)64, "1000000", "b", "byteToBinytaryString(byte)", Byte.class, String.class);
        tester.expecting(b, (byte)256, "100000000", "b", "byteToBinytaryString(byte)", Byte.class, String.class);
		// handle the case of zero 
		if (b == 0) {
			//
			//
			//
			//
			//
			//
			// BUG below... the wrong value is being returned for zero.
			// Instead of returning FrameworkConstants.ONE_STRING,
			// the code should return FrameworkConstants.ZERO_STRING.
			//	
			//
			//
			//
			//
			//
           
			tester.log("b", binaryRepresentation);
			return FrameworkConstants.ONE_STRING;
		}
		
		// number is greater than zero 
		while (number != 0) {	

			remainder = (byte) (number % 2);
			number = (byte) (number / 2); 
			
			// add a binary digit to the binary string representation
			if (remainder == 0) {
				// if we have a zero, add a zero to the front of the builder string
				binaryRepresentation = FrameworkConstants.ZERO_STRING + binaryRepresentation;
               
			} else {
				// if we have any value other than zero, add a one to the front of the builder string
				binaryRepresentation = FrameworkConstants.ONE_STRING + binaryRepresentation;
			}		
		}
		
		// handle negative sign by returning the 2's compliment
		if (b < 0) {
			StringUtility su = new StringUtility();
			binaryRepresentation = su.binaryByteTwosCompliment(binaryRepresentation);
          



		}
		
		System.out.println(binaryRepresentation);
		
		tester.log("b", binaryRepresentation);
		
		return binaryRepresentation;
	}
	
	
	/**
	 * Method to shift a byte by a number of bits, 
	 * right shifting is done using the signed right shift operator
	 * @param b - byte to shift
	 * @param placesToShift - number of bits to shift
	 * @param left - boolean, if true shift left, if false shift right
	 * @return - byte after shifting number of bit, 
	 * 			 	 if placesToShift is greater than 8 or negative, return 0
	 */
	public byte shiftByte(byte b, int placesToShift, boolean left) {
		
		byte shiftedByte;
	    tester.expecting(left, true, "true", "left", "shiftByte", boolean.class, boolean.class);
	    tester.expecting(left, false, "true", "left", "shiftByte", boolean.class, boolean.class);
	
		if (placesToShift > FrameworkConstants.BITS_IN_BYTE || placesToShift < 0) {
            tester.log("placesToShift", placesToShift);
            tester.log("left", left);
			shiftedByte = 0;
		} else if (left) {
			shiftedByte = (byte) (b << placesToShift);
            tester.log("left", left);
		} else {
			shiftedByte = (byte) (b >> placesToShift);
            tester.log("left", left);
		}
	    tester.expecting(shiftedByte, FrameworkConstants.BITS_IN_BYTE, "true",
                "shiftedByte", "shiftedByte", byte.class, byte.class);
	    tester.expecting(placesToShift, FrameworkConstants.BITS_IN_BYTE, "true",
                "placesToShift", "shiftedByte", int.class, int.class);
        tester.log("shiftedByte", shiftedByte);

		
		return shiftedByte;
	}
	
	
}
