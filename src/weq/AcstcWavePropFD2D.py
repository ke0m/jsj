#######################################################################
# Demo for 2D acoustic finite difference wave propagation

'''
Created on June 12, 2016

Author: Joseph Jennings
'''

import os,sys
from java.awt import *
from java.io import *
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *

from weq import *

def main(args):
  goBuildVelModel()
  goBuildRickerSource()
  return

def goBuildVelModel():
  nx,nz = 100,100
  dx,dz = 1.0,1.0
  fx,fz = 0.0,0.0
  sx = Sampling(nx,dx,fx)
  sz = Sampling(nz,dz,fz)
  vel = zerofloat(nx,nz)
  fill(1500.0,vel)
  plotVel(vel,sx,sz,png='velconst')

def goBuildRickerSource():
  nt  = 1000
  dt  = 0.004
  ft  = 0.0
  f   = 30.0
  st  = Sampling(nt,dt,ft)
  src = Source(st)
  rck = src.ricker(f)
  plotWavelet(rck,st,"ricker")

#############################################################################
# plotting

pngDir = "/home/joe/phd/projects/weq/fig/"

backgroundColor = Color(0xfd,0xfe,0xff)

def plotVel(v,sx,sz,cmin=0,cmax=0,png=None):
  pp = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  pp.setBackground(backgroundColor)
  pp.setHLabel("x (m)")
  pp.setVLabel("z (m)")
  pp.setHInterval(25.0)
  pp.setVInterval(25.0)
  pv = pp.addPixels(sx,sz,v)
  pv.setColorModel(ColorMap.JET)
  pv.setInterpolation(PixelsView.Interpolation.LINEAR)
  cb = pp.addColorBar('m/s')
  cb.setWidthMinimum(160)
  pf = PlotFrame(pp)
  pf.setFontSizeForSlide(1.0,0.9)
  pf.setSize(700,700)
  pf.setVisible(True)
  if cmin<cmax:
    pv.setClips(cmin,cmax)
  else:
    pv.setPercentiles(1,99)
  if pngDir and png:
    pf.paintToPng(360,3.3,pngDir+png+".png")

def plotWavelet(w,st,png=None):
  sp = SimplePlot()
  pv = sp.addPoints(st,w)
  sp.setHLabel("Time (s)")
  sp.setVLabel("Pressure (Pascal)")
  sp.setFontSizeForPrint(12,504)
  sp.setSize(800,500)
  sp.setVisible(True)
  if pngDir:
    sp.paintToPng(300,7.0,pngDir+png+".png")

#############################################################################
# Do everything on Swing thread.

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
