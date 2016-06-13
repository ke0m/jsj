package weq;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/** 
 * Finite difference acoustic wave equation
 * solver for constant density.
 * 
 * @author Joseph Jennings, Stanford University
 * @acknowledgements Guillaume Barnier; Ali Almomin, Stanford University
 *                   Simon Luo; Dave Hale, Colorado School of Mines
 * @version 2016.06.11 
 */

public class AcstcWfldFD {
	
	//TODO: Add sampling in x,z and t
	//      but I need to remember the CFL condition
	//      and write a method for that
	public AcstcWfldFD(Sampling x, Sampling z, Sampling t) {
	}
	
	public void forwardProp() {
		
	}
	
	public void adjointProp() {
		
	}
	

	
	public boolean checkCFL(Sampling x, Sampling z, Sampling t, float[][] v) {
		
		boolean test = false;
		
		return test;
	}
	
	public boolean checkPtsPerLength(Sampling x, Sampling z, float[][] v, float[] src) {
		boolean test = false;
		
		return test;
	}
	
	///////////////////////////////////////////////////////////////////////
	// private
	
}
