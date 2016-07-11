package weq;

/** 
 * Source scale operator for scaling zero-padded source
 * before interpolation
 * 
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Guillaume Barnier; Ali Almomin, Stanford University
 *                   Simon Luo; Dave Hale, Colorado School of Mines
 *                   
 * @version 2016.07.11
 */

public class SourceScale {
	
  /**
   * Constructs a SourceScale operator to scale the zero padded
   * source before interpolation
   * @param dtwf the temporal sampling of the wavefield
   * @param vel the input velocity
   */
  public SourceScale(double dtwf, float[][] vel) {
	  _dtwf = (float)dtwf;
	  _vel = vel;
	}
	
  /**
   * Applies the forward of the source scaling operator
   * @param psrc padded source wavelet
   * @param spsrc scaled padded source wavelet
   */
	public void forward(float[][][] psrc, float spsrc[][][]) {
	  int nz    = psrc[0][0].length;
	  int nx    = psrc[0].length;
	  int ntsrc = psrc.length;
	  for(int it = 0; it < ntsrc; ++it){
	    for(int ix = 0; ix < nx; ++ix){
	      for(int iz = 0; iz < nz; ++iz){
	        float v2  = _vel[ix][iz]*_vel[ix][iz];
	        float dt2 = _dtwf*_dtwf;
	        spsrc[it][ix][iz] += -v2*dt2*psrc[it][ix][iz];
	      }
	    }
	  }
	}
	
	/**
	 * Applies tha adjoint of the source scaling operator
	 * @param spsrc scaled padded source wavelet
	 * @param psrc padded source wavelet
	 */
	public void adjoint(float[][][] spsrc, float psrc[][][]) {
	  int nz    = psrc[0][0].length;
	  int nx    = psrc[0].length;
	  int ntsrc = psrc.length;
	  for(int it = 0; it < ntsrc; ++it){
	    for(int ix = 0; ix < nx; ++ix){
	      for(int iz = 0; iz < nz; ++iz){
	        float v2  = _vel[ix][iz]*_vel[ix][iz];
	        float dt2 = _dtwf*_dtwf;
	        psrc[it][ix][iz] += -v2*dt2*spsrc[it][ix][iz];
	      }
	    }
	  }
	}
	
	/////////////////////////////////////////////////
	// private
	
	float _dtwf;
	float[][] _vel;
	
}
