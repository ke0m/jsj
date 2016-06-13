package weq;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/** 
 * Build the source function for wave propagation
 * 
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Guillaume Barnier; Ali Almomin, Stanford University
 *                   Simon Luo; Dave Hale, Colorado School of Mines
 *                   
 * @version 2016.06.12
 */

public class Source {

	/** 
	 * Constructs a 1D source wavelet with specified sampling 
	 * @param src the sampling of the source wavelet
	 */
	public Source(Sampling src){
		_ntsrc = src.getCount();
		_ftsrc = src.getFirst();
		_dtsrc = src.getDelta();
	}
	
	/**
	 * Computes a zero-phase ricker wavelet
	 * @param f the peak frequency of the wavelet
	 * @return the 1D wavelet
	 */
	public double[] ricker(double f) {
		double[] ricker = new double[_ntsrc];
		
		for(int it = 0; it < _ntsrc; ++it) {
			double t = _ftsrc + it*_dtsrc;
			double pift = (DBL_PI*DBL_PI)*(f*f)*(t*t);
			ricker[it] = (1 - 2 *pift)*(exp(-pift));
		}
		
		return ricker;
	}

	
	/////////////////////////////////////////////////////////////////////////////
	// private
	
	private int _ntsrc;
	double _ftsrc;
	double _dtsrc;
	
}
