package weq;

import edu.mines.jtk.dsp.*;
import weq.AcstcWfldFD.*;

public class TestProp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double[] test = new double[1000];
		
		int nt = 1000;
		double dt =0.004;
		double ft = 0.0;
		
		double f = 30.0;
		
		Sampling st = new Sampling(nt,dt,ft);
		Sampling sx = new Sampling(nt,dt,ft);
		Sampling sz = new Sampling(nt,dt,ft);

		AcstcWfldFD wf = new AcstcWfldFD(sx,sz,st);
		
		//test = wf.rickerSource(st,f);

	}

}
