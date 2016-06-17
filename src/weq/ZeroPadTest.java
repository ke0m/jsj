package weq;

import static edu.mines.jtk.util.ArrayMath.*;

import junit.framework.TestCase;

/**
 * Tests {@link weq.ZeroPad}.
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Dave Hale, Colorado School of Mines
 * @version 2016.06.16
 */
public class ZeroPadTest extends TestCase {

  public void testDp() {
    int nx = 100; int nz = 100;
    int xs = 50; int zs = 0;
    int nts = 250;
    float[] m  = sub(randfloat(nts),0.5f);
    float[][][] d  = sub(randfloat(nx,nz,nts),0.5f);
    float[] ms = zerofloat(nts);
    float[][][] ds = zerofloat(nx,nz,nts);
    ZeroPad zp = new ZeroPad(xs,zs);
    zp.forward(m, ds);
    zp.adjoint(d, ms);
    float dotm = dot(m,ms);
    float dotd = dot(d,ds);
    assertEquals(dotm,dotd,0.00001f);
  }
  
  private static float dot(float[] x, float[] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }


}
