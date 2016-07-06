package weq;

import static edu.mines.jtk.util.ArrayMath.*;

import edu.mines.jtk.util.Check;

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
	 * Applies the forward of the zero pad operator
	 * @param img the input image (model)
	 * @param pimg the output padded image (data)
	 */
	public void forward(float[][] img, float[][] pimg){
	  int nxi =  img[0].length; int nzi =  img.length;
	  int nxp = pimg[0].length; int nzp = pimg.length;
	  if(nxp < nxi || nzp < nzi) {
	    Check.state(false, "The input image must be smaller than the output");
	  }
	  fill(0.f,pimg);
	  for(int ix = _xloc; ix < nxi; ix++) {
	    for(int iz = _zloc; iz < nzi; iz++) {
	      pimg[iz][ix] = img[iz][ix];
	    }
	  }
	}

	/**
	 * Applies the adjoint of the zero pad operator (truncation)
	 * @param psrc the input padded source wavelet (data)
	 * @param src the output 1D source wavelet (model)
	 */
	public void adjoint(float[][][] psrc, float[] src) {
		int ntsrc = src.length;
		for(int it = 0; it < ntsrc; ++it){
			src[it] += psrc[it][_zloc][_xloc];
		}
	}
	
	
	/**
	 * Applies the adjoint of the zero pad operator (truncation)
	 * @param pimg input padded image (data)
	 * @param img the output truncated image (model)
	 */
	public void adjoint(float[][] pimg, float[][] img) {
	  int nxi =  img[0].length; int nzi =  img.length;
	  int nxp = pimg[0].length; int nzp = pimg.length;
	  if(nxp < nxi || nzp < nzi) {
	    Check.state(false, "The output image must be smaller than the input");
	  }
	  for(int ix = _xloc; ix < nxi; ix++){
	    for(int iz = _zloc; iz < nzi; iz++) {
	      img[iz][ix] = pimg[iz][ix];
	    }
	  }
	}
	
	//////////////////////////////////////////////////////////
	// private
	
	private int _xloc, _zloc;
}
