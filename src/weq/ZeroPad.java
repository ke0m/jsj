package weq;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

public class ZeroPad {
	
	/**
	 * Constructs a zero pad operator
	 * for padding the input source wavelet
	 * before as input to a FD propagator
	 * @param ssrc the sampling of the source wavelet
	 * @param xloc x location of the source wavelet
	 * @param zloc z location of the source wavelet
	 */
	public ZeroPad(Sampling ssrc, int xloc, int zloc) {
		_xloc = xloc; _zloc = zloc;
		_ntsrc = ssrc.getCount();
	}
	
	public void forward(float[] src, float[][][] psrc) {
		for(int it = 0; it < _ntsrc; ++it) {
			psrc[it][_zloc][_xloc] += src[it];
		}
	}

	public void adjoint(float[][][] psrc, float[] src) {
		for (int it = 0; it < _ntsrc; ++it){
			src[it] = psrc[it][_zloc][_xloc];
		}
	}
	
	//////////////////////////////////////////////////////////
	// private
	
	private int _xloc, _zloc, _ntsrc;
	
}
