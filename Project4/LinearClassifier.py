#!/usr/bin/env python3
import LinearClassifierIO
import sys
import random
import math

time = 0
decay = False # -d flag 
logisticThreshold = False # -l flag
numIter = 10000	# -n flag
alpha = 0.1 # -a flag
showTrace = True
showGraphs = False

N = 0 # example arity
weights = [] # to be randomly initialized

# Take inner product of example with weight vector
def dotProd(X):
	return sum(i[0] * i[1] for i in zip(weights, X))

# Calculate the hypothesis on given input
def hypothesis(input):
	return threshold(dotProd(input))

# Compute hard or logistic threshold value
def threshold(val):
	if(logisticThreshold):
		return logistic(val)
	else:
		return 1 if val >= 0.0 else 0

def logistic(x):
	return 1/(1+math.exp(-x))

# this is constant if decay is false. Otherwise, it decays at a rate of O(1/t)
def stepSize():
	return 1000./(1000+time) if decay else alpha
	# alternative rule: alpha*numIter/(numIter+time**2) if decay else alpha

# update weights, learns from single example
def update(example, output):
	hw = hypothesis(example)
	ss = stepSize()
	if(logisticThreshold):
		for i in range(0,N):
			weights[i] += ss * (output - hw) * hw * (1-hw) * example[i]
	else:
		for i in range(0,N):
			weights[i] += ss * (output - hw) * example[i]

# iterative update on random samples/collect error measurements
def learn():
	global time
	n = len(examples)-1 # number of training examples - 1
	while(time <= numIter):
		sample = random.randint(0,n)
		update(examples[sample], outputs[sample])
		time += 1
		if(time % errorSampleSize == 0):
			p = measureError()
			error.append(p)
			if(p >= 0.999): # break if separator is found
				break

def measureError():
	return sqrdError() if logisticThreshold else portionCorrect()

# calculate fraction of points correctly identified by hypothesis (hard threshold)
def portionCorrect():
	n = len(examples)
	c = 0
	for i in range(n):
		if(outputs[i] == hypothesis(examples[i])):
			c += 1
	return c/float(n)

# calculate averaged squared error 1/n * SUM [y-hw(x)]^2 (soft threshold)
def sqrdError():
	n = len(examples)
	c = 0.0
	for i in range(n):
			c += (outputs[i] - hypothesis(examples[i]))**2
	return c/n

# Read argv
for i in range(0, len(sys.argv)):
	if(sys.argv[i] == '-f'):
		filename = sys.argv[i+1].lower()
		i += 1
	elif(sys.argv[i] == '-n'):
		numIter = int(sys.argv[i+1])
		i += 1
	elif(sys.argv[i] == '-d'):
		decay = True
	elif(sys.argv[i] == '-a'):
		alpha = float(sys.argv[i+1])
		i += 1
	elif(sys.argv[i] == '-l'):
		logisticThreshold = True
	elif(sys.argv[i] == '-t'):
		showTrace = False
	elif(sys.argv[i] == '-g'):
		showGraphs = True

(examples, outputs) = LinearClassifierIO.readFile(filename) # read example data

#randomly initialize weights
N = len(examples[0]) # arity of sample data
for i in range(N):
	weights.append(random.uniform(-1,1))

errorSampleSize = numIter//min(numIter, 1000) # how often should the error be sampled
error = []

learn()

print("Weights of separator w_0, w_1, ..., w_n:")
print(weights)
if(showTrace):
	print("Error Data:")
	print(error)
'''
if(showGraphs):
	LinearClassifierIO.showScatter(examples, outputs, weights)
	LinearClassifierIO.showError(error, errorSampleSize)
'''
