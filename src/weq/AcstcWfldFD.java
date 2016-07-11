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
 * @version 2016.07.10 
 */

//TODO: Pass a coefficient operator that allows for easy exchange of
//      boundary conditions?

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
		
		Check.state(checkCFL(v), "Error: please change your samplings. Your propagation will be unstable");

		if(checkPtsPerLength(v,fmax) == 0.f) {
			boolean disp = false;
			Check.state(disp, "Error: please change your samplings. Your propagation will be dispersive.");
		}
		
		_v = v;
	}
	
	/**
	 * Forward acoustic wave propagation without boundary conditions
	 * @param src the source wavelet, padded and interpolated
	 * @param wfld the wavefield
	 */
	public void forward(float[][][] src, float[][][] wfld) {
	  SimpleFloat3 sr3 = new SimpleFloat3(src);
	  SimpleFloat3 wf3 = new SimpleFloat3(wfld);
	  float[][] slc = zerofloat(_nz,_nx);
	  float[][] lap = zerofloat(_nz,_nx);
	  sr3.get12(_nz, _nx, 0, 0, 0, slc); // Initial
	  wf3.set12(_nz, _nx, 0, 0, 1, slc); // condition
	  for(int it=2; it<_nt; ++it){
	    wf3.get12(_nz, _nx, 0, 0, it-1, slc);
	    forward4Stencil(slc,lap);
	    for(int ix=0; ix<_nx; ++ix){
	      for(int iz=0; iz<_nz; ++iz){
	        float v2d2 = (_v[ix][iz]*_v[ix][iz])*(float)(_dt*_dt);
	        wfld[it][ix][iz] = src[it-1][ix][iz] + v2d2*lap[ix][iz] + 2*wfld[it-1][ix][iz] - wfld[it-2][ix][iz];
	      }
	    }
	  }
	}
	
	/**
	 * Adjoint acoustic wave propagation without boundary conditions
	 * @param wfld the wavfield
	 * @param src the source wavelet
	 */
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
		System.out.printf("CFL=%f\n",chk);
		
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
		System.out.printf("PPL=%f\n", ppl);
		
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
	      lap[ix][iz] = a0* wfld[ix  ][iz  ] * (idx2 + idz2)   +
	                    a1*(wfld[ix  ][iz+1] + wfld[ix  ][iz-1])*idz2 +
	                    a2*(wfld[ix  ][iz+2] + wfld[ix  ][iz-2])*idz2 +
	                    a1*(wfld[ix+1][iz  ] + wfld[ix-1][iz  ])*idx2 +
	                    a2*(wfld[ix+2][iz  ] + wfld[ix-2][iz  ])*idx2;
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
	      wfld[ix][iz] = a0* lap[ix  ][iz  ] * (idx2 + idz2)   +
	                     a1*(lap[ix  ][iz+1] + lap[ix  ][iz-1])*idz2 +
	                     a2*(lap[ix  ][iz+2] + lap[ix  ][iz-2])*idz2 +
	                     a1*(lap[ix+1][iz  ] + lap[ix-1][iz  ])*idx2 +
	                     a2*(lap[ix+2][iz  ] + lap[ix-2][iz  ])*idx2;
	      }
	   }
	}
}
