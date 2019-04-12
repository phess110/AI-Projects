import sys
import matplotlib.pyplot as plt
import numpy as np

def readFile(file):
	examples = []
	outputs = []
	with open(file,"rt") as f:
		for line in f:
			data = line.split(',')
			ex = [1.0]
			for entry in data:
				ex.append(float(entry))
			outputs.append(int(ex.pop(len(ex)-1)))
			examples.append(ex)
	return (examples, outputs)

# Error plot
def showError(error, samplingSize):
	t = [] # time steps
	for i in range(len(error)):
		t.append(samplingSize*i)
	plt.scatter(t, error, c = "b", s = 1)
	plt.title(r'$\mathrm{Error}$', size = 14)
	plt.xlabel(r'$t$')
	plt.ylabel('error (\% correct or avg. sqrd. error)')
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
	'''plt.legend((s1,s2),
	           (r'$Earthquake$', r'$Fracking$'),
	           scatterpoints=1,
	           fontsize=12)'''
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