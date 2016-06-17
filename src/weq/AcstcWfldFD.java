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
		_fx = sx.getFirst(); _fz = sz.getFirst(); _ft = st.getFirst();
		
		Check.state(checkCFL(v), "Error: please change your samplings. Your propagator will be unstable");

		if(checkPtsPerLength(v,fmax) == 0.f) {
			boolean disp = false;
			Check.state(disp, "Error: please change your samplings. Your propagation will be dispersive.");
		}
	}
	
	public void forward(float[][][] src, float[][][] wfld) {
		//TODO: First need a stencil
		
	}
	
	public void adjoint(float[][][] wfld, float[][][] src) {
	}
	
	
	///////////////////////////////////////////////////////////////////////
	// private
	private int _nx, _nz, _nt;
	private double _dx, _dz, _dt;
	private double _fx, _fz, _ft;
	
	private static final double CFL = sqrt(2)/2;
	private static final int T2S4 = 4;
	
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
		
		if(chk < CFL) {
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
	public float checkPtsPerLength(float[][] v, float fmax) {
		
		float vmin   = min(v);;
		float ppl    = 0.f;
		
		ppl = (float)(vmin/(fmax*max(_dx,_dz)));
		
		if(ppl < T2S4) {
			ppl = 0.f;
		}
		
		return ppl;
	}
	
	///////////////////////////////////////////////////////////////////////
	// private

	//private float[][] applyStencil4() {
	//}
	
}
