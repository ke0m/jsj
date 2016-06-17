package weq;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

import java.util.Random;

/**
 * Tests {@link weq.SourceScale}.
 * @author Joseph Jennings, Stanford University
 * @acknowledgement Dave Hale, Colorado School of Mines
 * @version 2016.06.16
 */

public class SourceScaleTest extends TestCase{
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(SourceScaleTest.class);
    junit.textui.TestRunner.run(suite);
  }
  
  public void testDp() {
    int nx = 100; int nz = 100; int nts = 250;
    float dt = 1.f;
    Random r = new Random(1992);
    float vel[][] = sub(randfloat(r,nx,nz),0.5f);
    SourceScale ssc = new SourceScale(dt,vel);
    float[][][] m = sub(randfloat(r,nx,nz,nts),0.5f);
    float[][][] d = sub(randfloat(r,nx,nz,nts),0.5f);
    float[][][] ms = zerofloat(nx,nz,nts);
    float[][][] ds = zerofloat(nx,nz,nts);
    ssc.forward(m, ds);
    ssc.adjoint(d, ms);
    float dotm = dot(m,ms);
    float dotd = dot(d,ds);
    assertEquals(dotm,dotd,0.00001f);
  }
  
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }
}
