#import matplotlib.pyplot as plt
#import numpy as np

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

# calculate fraction of points correctly identified by hypothesis (hard threshold)
def portionCorrect():
	n = len(examples)
	c = 0
	for i in range(n):
		if(outputs[i] == hypothesis(examples[i])):
			c += 1
	return c/n

# calculate averaged squared error 1/n * SUM [y-hw(x)]^2 (soft threshold)
def sqrdError():
	n = len(examples)
	c = 0.0
	for i in range(n):
			c += (outputs[i] - hypothesis(examples[i]))**2
	return c/n


'''
# Error plot
t = []
for i in range(len(error)):
	t.append(errorSampleSize*i)
s1 = plt.scatter(t,error, c ="b", s = 1)
plt.title(r'$\mathrm{Error}$', size = 14)
plt.show()

# Scatter plot
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
plt.legend((s1,s2),
           (r'$Earthquake$', r'$Fracking$'),
           scatterpoints=1,
           fontsize=12)
plt.title(r'$\mathrm{Earthquake\ Separator}$', size = 14)
x = np.linspace(4, 7, 100)
plt.plot(x, -(weights[0] + weights[1]*x)/weights[2]) 
plt.rc('font', family='serif')
plt.show()
'''