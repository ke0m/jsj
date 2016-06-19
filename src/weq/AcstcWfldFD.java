package weq;

import edu.mines.jtk.dsp.*;
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
	
	/**
	 * Constructs an acoustic wavefield object for propagating acoustic wavefields
	 * and checks for stability and dispersion in propagation.
	 * @param sx sampling of grid in x
	 * @param sz sampling of grid in z
	 * @param st sampling of wavefield in t
	 */
	public AcstcWfldFD(Sampling sx, Sampling sz, Sampling st, float[][] v, float fmax) {
		_nx = sx.getCount(); _nz = sz.getCount(); _nt = st.getCount();
		_dx = sx.getDelta(); _dz = sz.getDelta(); _dt = st.getDelta();
		
		Check.state(checkCFL(v), "Error: please change your samplings. Your propagator will be unstable");

		if(checkPtsPerLength(v,fmax) == 0.f) {
			boolean disp = false;
			Check.state(disp, "Error: please change your samplings. Your propagation will be dispersive.");
		}
		
		_v = v;
	}
	
	public void forward(float[][][] src, float[][][] wfld) {
	  //TODO: Initial conditions
	  SimpleFloat3 wf3 = new SimpleFloat3(wfld);
	  float[][] slc = zerofloat(_nz,_nx);
	  float[][] lap = zerofloat(_nz,_nx);
	  for(int it=2; it<_nt; ++it){
	    for(int ix=0; ix<_nx; ++ix){
	      for(int iz=0; iz<_nz; ++iz){
	        float v2d2 = (_v[iz][ix]*_v[iz][ix])*(float)(_dt*_dt);
	        wf3.get12(_nx, _nz, 0, 0, it, slc);
	        forward4Stencil(slc,lap);
	        wfld[it][iz][ix] = src[it][iz][ix]+ v2d2*lap[iz][ix] + 2*wfld[it-1][iz][ix]-wfld[it-2][iz][ix];
	      }
	    }
	  }
	}
	
	public void adjoint(float[][][] wfld, float[][][] src) {
	}
	
	///////////////////////////////////////////////////////////////////////
	// private
	private int _nx, _nz, _nt;
	private double _dx, _dz, _dt;
	
	private static final double _CFL = sqrt(2)/2;
	private static final int _T2S4 = 4;
	
	private float[][] _v;
	
	/**
	 * Checks the stability of the 2D acoustic propagator based on the 
	 * Courant-Fleury-Lewis (CFL) condition
	 * @param v input velocity
	 * @return a boolean indicating if the CFL condition is satisfied
	 */
	private boolean checkCFL(float[][] v) {
		
		float vmax     = max(v);
		boolean stable = false;
		float chk    = 0.f;
		
		chk = (float)(_dt*vmax/(min(_dx,_dz)));
		
		if(chk < _CFL) {
			stable = true;
		}
		
		return stable;
	}
	
	/**
	 * Checks for dispersive wave propagation for 2D 
	 * second order in time and fourth order in space
	 * derivatives
	 * @param v input velocity
	 * @param fmax the maximum frequency of the source wavelet
	 * @return if sufficient enough gridpoints per wavelength, returns the 
	 *         number of gridpoints per wavelength. Else,
	 *         returns 0.
	 */
	private float checkPtsPerLength(float[][] v, float fmax) {
		
		float vmin = min(v);;
		float ppl  = 0.f;
		
		ppl = (float)(vmin/(fmax*max(_dx,_dz)));
		
		if(ppl < _T2S4) {
			ppl = 0.f;
		}
		return ppl;
	}
	
	/**
	 * The forward fourth order differencing stencil (laplacian)
	 * @param wfld the input wavefield
	 * @param lap the differentiated wavefield
	 */
	private void forward4Stencil(float[][] wfld, float[][] lap) {
	  float a2=-0.08333f; float a1=1.33333f; float a0=-2.50000f;
	  float idx2 = (float)(1/(_dx*_dx)); float idz2 = (float)(1/(_dz*_dz));
	  for(int ix=2; ix<_nx-2; ++ix){
	    for(int iz=2; iz<_nz-2; ++iz){
	      lap[iz][ix] = a0* wfld[iz  ][ix  ] * (idx2 + idz2)   +
	                    a1*(wfld[iz  ][ix+1] + wfld[iz  ][ix-1])*idx2 +
	                    a2*(wfld[iz  ][ix+2] + wfld[iz  ][ix-2])*idx2 +
	                    a1*(wfld[iz+1][ix  ] + wfld[iz-1][ix  ])*idz2 +
	                    a2*(wfld[iz+2][ix  ] + wfld[iz-2][ix  ])*idz2;
	    }
	  }
	}
	
	/**
	 * The adjoint fourth order differencing stencil (also laplacian)
	 * @param lap the input differentiated wavefield
	 * @param wfld the output wavefield
	 */
	private void adjoint4Stencil(float[][] lap, float[][] wfld) {
	   float a2=-0.08333f; float a1=1.33333f; float a0=-2.50000f;
	    float idx2 = (float)(1/(_dx*_dx)); float idz2 = (float)(1/(_dz*_dz));
	    for(int ix=2; ix<_nx-2; ++ix){
	      for(int iz=2; iz<_nz-2; ++iz){
	        wfld[iz][ix] = a0* lap[iz  ][ix  ] * (idx2 + idz2)   +
	                       a1*(lap[iz  ][ix+1] + lap[iz  ][ix-1])*idx2 +
	                       a2*(lap[iz  ][ix+2] + lap[iz  ][ix-2])*idx2 +
	                       a1*(lap[iz+1][ix  ] + lap[iz-1][ix  ])*idz2 +
	                       a2*(lap[iz+2][ix  ] + lap[iz-2][ix  ])*idz2;
	      }
	    }
	}
}
