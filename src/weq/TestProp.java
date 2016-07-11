package weq;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.io.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

public class TestProp {

  public static void main(String[] args) {
    
    /*          Sizes          */
    int nx  = 100; int nz  = 100;
    int nxp = 300; int nzp = 300;
    int nts = 625; int nt  = 2500;
    
    //TODO: How to best set these?
    double fpek = 15.0f; float fmax = 50.0f;
    int xsrc = 150; int zsrc = 100;
    
    /*      Intervals and first         */
    float dx  = 10.0f;    float dz = 10.0f;
    float dts = 0.004f; float dt = 0.001f;
    float fts = -0.1f;   float ft = 0.0f;
    
    /*              Samplings                        */
    Sampling sxp = new Sampling(nxp, (double)dx , 0.0);
    Sampling szp = new Sampling(nzp, (double)dz , 0.0);
    Sampling sx  = new Sampling(nx,  (double)dx , 0.0);
    Sampling sz  = new Sampling(nz,  (double)dz , 0.0);
    Sampling st  = new Sampling(nt,  (double)dt , (double)fts);
    Sampling sts = new Sampling(nts, (double)dts, (double)fts);
    
    float []   src     = zerofloat(nts);
    float [][] vel     = zerofloat(nxp,nzp);
    float [][][] psrc  = zerofloat(nxp,nzp,nts);
    float [][][] spsrc = zerofloat(nxp,nzp,nts);
    float [][][] sinp  = zerofloat(nxp,nzp,nt);
    float [][][] wfld  = zerofloat(nxp,nzp,nt);
    
    fill(3000.f, vel);
    
    /*        Wave equation objects         */
    Source rck      = new Source(sts);
    ZeroPad zps     = new ZeroPad(xsrc,zsrc);
    ZeroPad zpv     = new ZeroPad(100,100,1500.0f);
    SourceScale ss  = new SourceScale(dt,vel);
    SourceInterp si = new SourceInterp(sts,st);
    AcstcWfldFD prp = new AcstcWfldFD(sxp, szp, st, vel, fmax);
    
    src = rck.ricker(fpek);
    
    zps.forward(src, psrc);
    ss.forward(psrc, spsrc);
    si.forward(spsrc, sinp);
    prp.forward(sinp, wfld);
   
    /* View the wavefield */
    for(int i=0; i < 300; i+=20)
      viewSlice(wfld,i,sxp,szp);
  }
  
  private static void plotSinp(float[][][] sinp,float dt,int nt,int xsrc,int zsrc) {
    SimpleFloat3 s3 = new SimpleFloat3(sinp);
    Sampling stp = new Sampling(nt,dt,0.0);
    float[] slc = zerofloat(nt);
    s3.get3(nt, xsrc, zsrc, 0, slc);
    dump(slc);
    SimplePlot.asSequence(stp, slc);
  }
  
  private static void viewSlice(float[][][] wfld, int slcnum, Sampling sxp, Sampling szp) {
    SimpleFloat3 w3 = new SimpleFloat3(wfld);
    int nx = wfld[0].length; int nz = wfld[0][0].length;
    float [][] tslc = zerofloat(nx,nz);
    w3.get12(nx, nz, 0, 0, slcnum, tslc);
    PlotPanel pp = new PlotPanel();
    PixelsView pv = pp.addPixels(sxp, szp, tslc);
    pp.addColorBar();
    PlotFrame pf = new PlotFrame(pp);
    pf.setVisible(true);
    pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
  }
  
  private static void sliceSource(
      float[][][] sinp, Sampling sx, Sampling sz, Sampling st, int xs, int zs) {
    SimpleFloat3 s3 = new SimpleFloat3(sinp);
    int nx = sx.getCount(); int nz = sz.getCount(); int nt = st.getCount();
    float[][] sslc = zerofloat(nx,nz);
    float[] src = zerofloat(nt);
    for(int it=0; it<nt-1; ++it){
      s3.get12(nx, nz, 0, 0, it, sslc);
      if(it%2 == 0) sinp[it+1][zs][xs] *= -1;
      src[it] = sslc[zs][xs];
    }
    SimplePlot.asSequence(st,src);
  }
}
