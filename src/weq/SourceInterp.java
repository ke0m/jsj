package weq;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.*;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Source interpolation operator. Interpolates source wavelet
 * before being input to finite-difference operator
 * 
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Guillaume Barnier; Ali Almomin, Stanford University 
 * @version 2016.06.16
 */
public class SourceInterp {
  
  /**
   * Constructs a SourceInterp operator
   * @param ss temporal sampling of source wavelet
   * @param sf temporal sampling of wavefield
   */
  public SourceInterp(Sampling ss, Sampling sf){
    _nts = ss.getCount(); _ntf = sf.getCount();
    _dts = ss.getDelta(); _dtf = sf.getDelta();
    _fts = ss.getFirst(); _ftf = sf.getFirst();
  }
  
  /**
   * Interpolates the input padded source to the temporal 
   * sampling of the wavefield. Assumes input source
   * is regularly sampled in time and does not extrapolate.
   * @param psrc the input padded source
   * @param sinp output interpolated source
   */
  public void forward(float[][][] psrc, float[][][] sinp) {
    Check.argument(sinp.length == _ntf, "Output array length does not match sampling length.");
    Check.argument(psrc.length == _nts, "Input array length does not match sampling length.");
    SimpleFloat3 sc3 = new SimpleFloat3(psrc);
    SimpleFloat3 sf3 = new SimpleFloat3(sinp);
    float[] crs = zerofloat(_nts);
    float[] fin = zerofloat(_ntf);
    int nx    = psrc[0][0].length;
    int nz    = psrc[0].length;
    LinearInterpolator li = new LinearInterpolator();
    li.setUniform(_nts, _dts, _fts, crs);
    for(int iz=0; iz<nz; ++iz) {
      for(int ix=0; ix<nx; ++ix){
        sc3.get3(_nts,ix,iz,0,crs);
        li.interpSimp(_ntf,_dtf,_ftf,fin);
        sf3.set3(_ntf,ix,iz,0,fin);
      }
    }
  }
  
  /**
   * Applies the adjoint of the source interpolator operator.
   * Takes in the finely sampled source wavelet (input to FD)
   * and effectively subsamples back to sampling of source wavelet
   * @param sinp the finely interpolated source wavelet
   * @param psrc the output of the adjoint interpolator
   */
  public void adjoint(float[][][] sinp, float[][][] psrc){
    SimpleFloat3 sc3 = new SimpleFloat3(psrc);
    SimpleFloat3 sf3 = new SimpleFloat3(sinp);
    float[] crs = zerofloat(_nts);
    float[] fin = zerofloat(_ntf);
    int nx    = psrc[0][0].length;
    int nz    = psrc[0].length;
    LinearInterpolator li = new LinearInterpolator();
    li.setUniform(_nts, _dts, _fts, fin);
    for(int iz=0; iz<nz; ++iz){
      for(int ix=0; ix<nx; ++ix) {
        sf3.get3(_ntf,ix,iz,0,fin);
        li.adjInterpSimp(_ntf,_dtf,_ftf,crs);
        sc3.set3(_nts,ix,iz,0,crs);
      }
    }
  }
  
  /////////////////////////////////////////////////
  // private
  
  int    _nts, _ntf;
  double _dts, _dtf;
  double _fts, _ftf;

}
