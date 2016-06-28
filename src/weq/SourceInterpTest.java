package weq;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.dsp.*;

import static edu.mines.jtk.util.ArrayMath.*;

public class SourceInterpTest extends TestCase{
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(SourceInterpTest.class);
    junit.textui.TestRunner.run(suite);
  }
  
  public void testDp() {
    int nx = 100; int nz = 100; 
    int nts = 250; int ntf = 1000;
    double dts = 0.0018; double dtf = 0.00045;
    double fts = 0.0; double ftf = 0.0;
    Sampling ss = new Sampling(nts,dts,fts);
    Sampling sf = new Sampling(ntf,dtf,ftf);
    SourceInterp sint = new SourceInterp(ss,sf);
    float[][][] m = sub(randfloat(nx,nz,nts),0.5f);
    float[][][] d = sub(randfloat(nx,nz,ntf),0.5f);
    float[][][] ms = zerofloat(nx,nz,nts);
    float[][][] ds = zerofloat(nx,nz,ntf);
    sint.forward(m, ds);
    sint.adjoint(d, ms);
    float dotm = dot(m,ms);
    float dotd = dot(d,ds);
    assertEquals(dotm,dotd,0.0001f); //NOTE: needs decreased precision
  }
  
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }

}
