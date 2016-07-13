package weq;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.io.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

public final class TestProp {

  public static void main(String[] args) {
    
    float[][][] myw = TestProp.computeWavefield();
  }
  
  public static float[][][] computeWavefield() {
    /*          Sizes          */
    int nx  = 100; int nz  = 100;
    int nxp = 300; int nzp = 300;
    int nts = 625; int nt  = 2500;
    
    //TODO: How to best set these?
    float fpek = 15.0f; float fmax = 50.0f;
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
    float [][] vel     = zerofloat(nzp,nxp);
    float [][][] psrc  = zerofloat(nzp,nxp,nts);
    float [][][] spsrc = zerofloat(nzp,nxp,nts);
    float [][][] sinp  = zerofloat(nzp,nxp,nt);
    float [][][] wfld  = zerofloat(nzp,nxp,nt);
    
    fill(3000.f, vel);
    
    /*        Wave equation objects         */
    Source rck      = new Source(sts);
    ZeroPad zps     = new ZeroPad(zsrc,xsrc);
    SourceScale ss  = new SourceScale(dt,vel);
    SourceInterp si = new SourceInterp(sts,st);
    AcstcWfldFD prp = new AcstcWfldFD(sxp, szp, st, vel, fmax);
    
    src = rck.ricker(fpek);
    
    zps.forward(src, psrc);
    ss.forward(psrc, spsrc);
    si.forward(spsrc, sinp);
    prp.forward(sinp, wfld);
   
    /* View the wavefield */
    //for(int i=0; i < 300; i+=20)
    //  viewSlice(wfld,i,sx,sz);
    return wfld;
  }
  
  private static void plotSinp(float[][][] sinp,float dt,int nt,int xsrc,int zsrc) {
    SimpleFloat3 s3 = new SimpleFloat3(sinp);
    Sampling stp = new Sampling(nt,dt,0.0);
    float[] slc = zerofloat(nt);
    s3.get3(nt, xsrc, zsrc, 0, slc);
    dump(slc);
    SimplePlot.asSequence(stp, slc);
  }
  
  private static void viewSlice
  (float[][][] wfld, int slcnum, Sampling sx, Sampling sz) {
    SimpleFloat3 w3 = new SimpleFloat3(wfld);
    int nx = sx.getCount(); int nz = sz.getCount(); 
    float [][] tslc = zerofloat(nz,nx);
    w3.get12(nz, nx, 100, 100, slcnum, tslc);
    PlotPanel pp = new PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT);
    pp.addPixels(sz,sx,tslc);
    pp.addColorBar();
    PlotFrame pf = new PlotFrame(pp);
    pf.setVisible(true);
    pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
  }
  
  private static void sliceSource(
      float[][][] sinp, Sampling sx, Sampling sz, Sampling st, int xs, int zs) {
    SimpleFloat3 s3 = new SimpleFloat3(sinp);
    int nx = sx.getCount(); int nz = sz.getCount(); int nt = st.getCount();
    float[][] sslc = zerofloat(nz,nx);
    float[] src = zerofloat(nt);
    for(int it=0; it<nt-1; ++it){
      s3.get12(nz, nx, 0, 0, it, sslc);
      if(it%2 == 0) sinp[it+1][xs][zs] *= -1;
      src[it] = sslc[xs][zs];
    }
    SimplePlot.asSequence(st,src);
  }
}
