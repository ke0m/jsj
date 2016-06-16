package weq;

/** 
 * Source scale operator for scaling zero-padded source
 * before interpolation
 * 
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Guillaume Barnier; Ali Almomin, Stanford University
 *                   Simon Luo; Dave Hale, Colorado School of Mines
 *                   
 * @version 2016.06.16
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
	
	public void forward(float[][][] psrc, float spsrc[][][]) {
		int nx    = psrc[0][0].length;
		int nz    = psrc[0].length;
		int ntsrc = psrc.length;
		for(int it = 0; it < ntsrc; ++it){
			for(int iz = 0; iz < nz; ++iz){
				for(int ix = 0; ix < nx; ++ix){
					float v2  = _vel[iz][ix]*_vel[iz][ix];
					float dt2 = _dtwf*_dtwf;
					spsrc[it][iz][ix] += -v2*dt2*psrc[it][ix][iz];
				}
			}
		}
	}
	
	public void adjoint(float[][][] spsrc, float psrc[][][]) {
		int nx    = psrc[0][0].length;
		int nz    = psrc[0].length;
		int ntsrc = psrc.length;
		for(int it = 0; it < ntsrc; ++it){
			for(int iz = 0; iz < nz; ++iz){
				for(int ix = 0; ix < nx; ++ix){
					float v2  = _vel[iz][ix]*_vel[iz][ix];
					float dt2 = _dtwf*_dtwf;
					psrc[it][iz][ix] += -v2*dt2*spsrc[it][ix][iz];
				}
			}
		}
	}
	
	/////////////////////////////////////////////////
	// private
	
	float _dtwf;
	float[][] _vel;
	
}
