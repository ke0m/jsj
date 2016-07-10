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
    int nts = 250; int nt  = 1000;
    
    //TODO: How to best set these?
    double fpek = 15.0f; float fmax = 50.0f;
    int xsrc = 50; int zsrc = 0;
    
    /*      Intervals and first         */
    float dx  = 1;       float dz = 1;
    float dts = 0.0018f; float dt = 0.00045f;
    float fts = -0.3f;   float ft = 0.0f;
    
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
    
    fill(1500.f, vel);
    
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
   
  }
  
  private static void plotSinp(float[][][] sinp,float dt,int nt,int xsrc,int zsrc) {
    SimpleFloat3 s3 = new SimpleFloat3(sinp);
    Sampling stp = new Sampling(nt,dt,0.0);
    float[] slc = zerofloat(nt);
    s3.get3(nt, xsrc, zsrc, 0, slc);
    dump(slc);
    SimplePlot.asSequence(stp, slc);
  }
}
