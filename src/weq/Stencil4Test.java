package weq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.ArrayMath.*;

import java.util.Random;

public class Stencil4Test extends TestCase {

  public static void main(String[] args) {
    TestSuite suite = new TestSuite(Stencil4Test.class);
    junit.textui.TestRunner.run(suite);
  }
  
  public void testDp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    int nx = 100; int nz = 100; int nt = 100;
    Sampling sx = new Sampling(nx,1.0,0.0);
    Sampling sz = new Sampling(nz,1.0,0.0);
    Sampling st = new Sampling(nt,0.5,1.0);
    Random r = new Random(1992);
    float f = 0.00001f;
    float [][] m  = sub(randfloat(r,nx,nz),0.5f);
    float [][] d  = sub(randfloat(r,nx,nz),0.5f);
    float [][] ms = zerofloat(nx,nz);
    float [][] ds = zerofloat(nx,nz);
    AcstcWfldFD prp = new AcstcWfldFD(sx,sz,st,abs(m),f);
    Class[] parameterTypes = new Class[2];
    Class[] ptype = new Class[1];
    ptype[0] = java.lang.Float.class;
    parameterTypes[0] = java.lang.Float[][].class;
    parameterTypes[1] = java.lang.Float[][].class;
    Method[] methods = prp.getClass().getDeclaredMethods();
    Method mf = null; Method ma = null;
    for(int i = 0; i < methods.length; ++i) {
      if(methods[i].getName() == "forward4Stencil") {
        mf = methods[i];
      }
      else if(methods[i].getName() == "adjoint4Stencil") {
        ma = methods[i];
      }
    }
    mf.setAccessible(true); ma.setAccessible(true);
    Object[] parametersf = new Object[2];
    parametersf[0] = m;
    parametersf[1] = ds;
    mf.invoke(prp, parametersf);
    Object[] parametersa = new Object[2];
    parametersa[0] = d;
    parametersa[1] = ms;
    ma.invoke(prp, parametersa);
    float dotm = dot(m,ms);
    float dotd = dot(d,ds);
    assertEquals(dotm,dotd,0.00001f);
  }
  private static float dot(float[][] x, float[][] y) {
    return sum(mul(x,y));
  }
}
