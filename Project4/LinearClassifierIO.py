import sys
from os import path
'''
# For graph-making only:
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
'''
def readFile(file):
	examples = []
	outputs = []
	with open(path.relpath("LCexamples/" + file)) as f:
		for line in f:
			data = line.split(',')
			ex = [1.0]
			for entry in data:
				ex.append(float(entry))
			outputs.append(int(ex.pop(len(ex)-1)))
			examples.append(ex)
	return (examples, outputs)
'''
# Error plot - only makes sence if input data is 2-dimensional
def showError(error, samplingSize):
	df=pd.DataFrame({'x': range(0, samplingSize*len(error), samplingSize) , 'y': error })
	plt.title('LC Error vs. Time', size = 14)
	plt.xlabel(r'$t$')
	plt.ylabel("Average squared error/example")
	plt.plot('x', 'y', data=df, color='blue')
	plt.show()

	# For scatter plot of error instead of line graph
	t = [] # time steps
	for i in range(len(error)):
		t.append(samplingSize*i)
	plt.scatter(t, error, c = "b", s = 1)
	plt.title('LC Error vs. Time', size = 14)
	plt.xlabel(r'$t$')
	plt.ylabel('% Correct')
	plt.show()

# Scatter plot
def showScatter(examples, outputs, weights):
	x1 = []
	y1 = []
	x2 = []
	y2 = []
	for i in range(len(examples)):
		if(outputs[i] == 1):
			x1.append( examples[i][1] )
			y1.append( examples[i][2] )
		else:
			x2.append( examples[i][1] )
			y2.append( examples[i][2] )
	#plt.style.use('C:/Users/Ray/AppData/Local/Programs/Python/Python37/Lib/site-packages/matplotlib/style/pres.mplstyle')
	s1 = plt.scatter(x1,y1, c ="b")
	s2 = plt.scatter(x2,y2, c ="r")
	plt.title('Linear Separator Scatter Plot', size = 14)
	plt.xlabel(r'$x_1$')
	plt.ylabel(r'$x_2$')
	minxrange = sys.maxsize
	maxxrange = -sys.maxsize
	for ex in examples:
		minxrange = min(minxrange, ex[1])
		maxxrange = max(maxxrange, ex[1])
	x = np.linspace(minxrange - 1, maxxrange + 1)
	plt.plot(x, -(weights[0] + weights[1] * x)/weights[2]) 
	plt.show()
'''
