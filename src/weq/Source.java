package weq;

import edu.mines.jtk.dsp.*;
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
		_dtsrc = src.getDelta();
		_ftsrc = src.getFirst();
		 
	}
	
	/**
	 * Computes a zero-phase ricker wavelet
	 * @param f the peak frequency of the wavelet
	 * @return the 1D wavelet
	 */
	public float[] ricker(float f) {
		float[] ricker = new float[_ntsrc];
		double fd = (double)f;
		
		for(int it = 0; it < _ntsrc; ++it) {
			double t = _ftsrc + it*_dtsrc;
			float pift = (float)((DBL_PI*DBL_PI)*(fd*fd)*(t*t));
			ricker[it] = (1 - 2 *pift)*(exp(-pift));
		}
		return ricker;
	}

	/////////////////////////////////////////////////////////////////////////////
	// private
	
	private int    _ntsrc;
	private double _dtsrc;
	private double _ftsrc;
	
}
