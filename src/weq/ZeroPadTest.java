package weq;

import static edu.mines.jtk.util.ArrayMath.*;

import junit.framework.TestCase;

/**
 * Tests {@link weq.ZeroPad}.
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Dave Hale, Colorado School of Mines
 * @version 2016.07.06
 */
public class ZeroPadTest extends TestCase {

  public void testDpSrc() {
    int nx = 100; int nz = 100;
    int xs = 50; int zs = 0;
    int nts = 250;
    float[] m  = sub(randfloat(nts),0.5f);
    float[][][] d  = sub(randfloat(nz,nx,nts),0.5f);
    float[] ms = zerofloat(nts);
    float[][][] ds = zerofloat(nz,nx,nts);
    ZeroPad zp = new ZeroPad(zs,xs);
    zp.forward(m, ds);
    zp.adjoint(d, ms);
    float dotm = dot(m,ms);
    float dotd = dot(d,ds);
    assertEquals(dotm,dotd,0.00001f);
  }
  
  public void testDpImg() {
    int nxm = 100; int nzm = 100;
    int nxd = 200; int nzd = 200;
    int xs = 2; int zs = 2;
    float[][] m = sub(randfloat(nzm,nxm),0.5f);
    float[][] d = sub(randfloat(nzd,nxd),0.5f);
    float[][] ms = zerofloat(nzm,nxm);
    float[][] ds = zerofloat(nzd,nxd);
    ZeroPad zp = new ZeroPad(zs,xs);
    zp.forward(m, ds);
    zp.adjoint(d, ms);
    float dotm = dot(m,ms);
    float dotd = dot(d,ds);
    assertEquals(dotm,dotd,0.00001f);
  }
  
  private static float dot(float[] x, float[] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][] x, float[][] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }


}
