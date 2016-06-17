package weq;

/** 
 * Zero pad operator for creating input for wave propagation
 * 
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Guillaume Barnier; Ali Almomin, Stanford University
 *                   Simon Luo; Dave Hale, Colorado School of Mines
 *                   
 * @version 2016.06.14
 */

public class ZeroPad {
	
	/**
	 * Constructs a zero pad operator
	 * for padding the input source wavelet
	 * before as input to a FD propagator
	 * @param xloc x location of the source wavelet
	 * @param zloc z location of the source wavelet
	 */
	public ZeroPad(int xloc, int zloc) {
		_xloc = xloc; _zloc = zloc;
	}
	
	/**
	 * Applies the forward of the zero pad operator
	 * @param src the input source wavelet (model)
	 * @param psrc the output padded source wavelet (data)
	 */
	public void forward(float[] src, float[][][] psrc) {
		int ntsrc = src.length;
		for(int it = 0; it < ntsrc; ++it) {
			psrc[it][_zloc][_xloc] += src[it];
		}
	}

	/**
	 * Applies the adjoint of the zero pad operator
	 * @param psrc the input padded source wavelet (data)
	 * @param src the output 1D source wavelet (model)
	 */
	public void adjoint(float[][][] psrc, float[] src) {
		int ntsrc = src.length;
		for (int it = 0; it < ntsrc; ++it){
			src[it] += psrc[it][_zloc][_xloc];
		}
	}
	
	//////////////////////////////////////////////////////////
	// private
	
	private int _xloc, _zloc;
}
