package weq;

import edu.mines.jtk.dsp.*;
import weq.AcstcWfldFD.*;
import static edu.mines.jtk.util.ArrayMath.*;

public class TestProp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int nx = 100;
		int nz = 100;
		int nt = 1000;
		double dx = 1.0;
		double dz = 1.0;
		double dt = 0.00045;
		double dtsrc = 0.004;
		double fx = 0.0;
		double fz = 0.0;
		double ft = 0.0;
		double ftsrc = -1.0;
		
		float[][] vel = new float[nx][nz];
		
		fill(1500.f,vel);
		
		double f = 30.0;
		float fmax = 100.f;
		
		Sampling st = new Sampling(nt,dt,ft);
		Sampling sx = new Sampling(nx,dx,fx);
		Sampling sz = new Sampling(nz,dz,fz);
		Sampling stsrc = new Sampling(nt,dtsrc,ftsrc);

		AcstcWfldFD wf = new AcstcWfldFD(sx,sz,st,vel,fmax);
		Source src     = new Source(stsrc);
		
		float[] rck = new float[nt];
		
		rck = src.ricker(f);
		
		float[][][] psrc = new float[nx][nz][nt];
		
		//psrc = zerofloat(nx,nz,nt);
		
		src.zeroPadSrc(rck, psrc, 50, 0);
		System.out.println("Complete");
		
		//test = wf.rickerSource(st,f);

	}

}
