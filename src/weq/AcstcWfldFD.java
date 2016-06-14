package weq;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.mosaic.*;
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
	 * @param sx sampling of grid in x
	 * @param sz sampling of grid in z
	 * @param st sampling of grid in t
	 */
	public AcstcWfldFD(Sampling sx, Sampling sz, Sampling st) {
		_nx = sx.getCount(); _nz = sz.getCount(); _nt = st.getCount();
		_dx = sx.getDelta(); _dz = sz.getDelta(); _dt = st.getDelta();
		_fx = sx.getFirst(); _fz = sz.getFirst(); _ft = st.getFirst();
	}
	
	public void forwardProp() {
		
	}
	
	public void adjointProp() {
		
	}
	
	/**
	 * Checks the stability of the 2D acoustic propagator based on the 
	 * Courant-Fleury-Lewis (CFL) condition
	 * @param v input velocity
	 * @return a boolean indicating if the CFL condition is satisfied
	 */
	public boolean checkCFL(float[][] v) {
		
		float vmax     = max(v);
		boolean stable = false;
		float chk    = 0.f;
		
		if(_dx > _dz) {
			chk = (float)(_dt*vmax/_dz);
		}
		else if(_dx < _dz) {
			chk = (float)(_dt*vmax/_dx);
		}
		else {
			chk = (float)(_dt*vmax/_dx);
		}
		
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
	 * @return if the condition is satisfied, returns the 
	 *         number of gridpoints per wavelength. Else,
	 *         returns 0.
	 */
	public float checkPtsPerLength(float[][] v) {
		
		float vmin   = min(v);		
		double fmax  = 1/(2*_dt);
		float ppl    = 0.f;
		
		if(_dx > _dz) {
			ppl = (float)(vmin/fmax*_dx);
		}
		else if(_dx < _dz){
			ppl = (float)(vmin/fmax*_dz);
		}
		else {
			ppl = (float)(vmin/fmax*_dx);
		}
		
		if(ppl < T2S4) {
			ppl = 0.f;
		}
		
		return ppl;
	}
	
	///////////////////////////////////////////////////////////////////////
	// private
	private int _nx, _nz, _nt;
	private double _dx, _dz, _dt;
	private double _fx, _fz, _ft;
	
	private static final double CFL = sqrt(2)/2;
	private static final int T2S4 = 4;
	
}
