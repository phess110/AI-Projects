import sys 
import math
import random as rand
import Unit as NNnode

# Activation Functions
logistic = lambda x : 1.0/(1.0 + math.exp(-x))
logisticDerivative = lambda x : logistic(x) * (1.0 - logistic(x))

# Global Vars
time = 0
alpha = 0.1
decay = False
numIter = 0

# this is constant if decay is false. Otherwise, it decays at a rate of O(1/t)
def stepSize(n):
	return alpha*numIter/(numIter+n**2) if decay else alpha

# Returns a random selection of N/k example/output pairs from data set
def partition(examples, outputs, k):
	n = len(examples)//k
	testExs = []
	testOuts = []
	for i in range(n):
		r = rand.randint(0, len(examples)-1)
		testExs.append(examples.pop(r))
		testOuts.append(outputs.pop(r))
	return (testExs, testOuts)

def merge(trainSet, testSet):
	for val in testSet:
		trainSet.append(val)

class NeuralNet:
	# layerSize[i] = number of units in layer i
	# layerSize MUST have at least two entries (the input/output layer sizes)
	def __init__(self, layerSize, activationFunction = logistic, derivative = logisticDerivative):
		self.L = 0 					# number of layers	
		self.layers = {}
		for l in layerSize:
			self.addLayer(l)		# layers : mapping from 1..L -> list of units in layer
		self.g = activationFunction
		self.gPrime = derivative

	# Adds a layer of size numUnits to rightmost layer of the given NN
	# Builds a full-connected network (each node in layer i is connected to every node in layer i+1)
	def addLayer(self, numUnits):
		self.L = self.L + 1
		newLayer = []
		for i in range(numUnits):
			n = NNnode.unit()
			newLayer.append(n)
			if(self.L > 1):
				dummy = NNnode.unit()
				dummy.activate(1.0)
				dummy.addEdge(n)
				for parent in self.layers[self.L-1]:
					parent.addEdge(n)
		self.layers[self.L] = newLayer

	def inputLayer(self):
		return self.layers[1]

	def outputLayer(self):
		return self.layers[self.L]

	def getAccuracy(self, testExs, testOuts):
		correctCount = 0
		for example, output in zip(testExs, testOuts):
			self.forwardProp(example)
			if(output[self.decision()] > 0.99): # test if the decision node is the one that was supposed to be activated
				correctCount += 1
		return correctCount/float(len(testExs))
	
	# Backpropagation algorithm for feed-forward NNs.
	# 	|examples| should equal |outputs| 
	# 	Number of output nodes in NN = m, number of input nodes in NN = n 
	# 	Each example should be in {0,1}^n
	# 	Each output should be in {0,1}^m
	def backPropLearn(self, examples, outputs):
		correctCount = 0
		for example, output in zip(examples, outputs):
			self.forwardProp(example)
			if(output[self.decision()] > 0.99):
				correctCount += 1

			for node, expectedOutput in zip(self.outputLayer(), output): # back prop - calculate output errors
				self.outputError(node, expectedOutput)
				node.updateWeights(stepSize(time))
			for l in range(self.L-1,1,-1): # push error backwards to earliest hidden layer (layer 2)
				for node in self.layers[l]:
					self.hiddenError(node)
					node.updateWeights(stepSize(time))
			#
			#for l in range(2,self.L+1): # update weights (including dummy weights!!)
			#	for j in self.layers[l]:
			#		for i in j.parents:
			#			i.updateWeight(j, stepSize(time))
		return correctCount/float(len(outputs))

	def forwardProp(self, example):
		inputNodes = self.inputLayer() # set activation inputs from example
		for i in range(len(example)):
			inputNodes[i].activate(example[i]) 
		for l in range(2,self.L+1):
			for node in self.layers[l]:
				node.computeInput()
				node.activate(self.g(node.input))

	# Compute error at output nodes according to gradient descent rule
	# 	Delta_j = g'(in_j) * (y_j - a_j)
	def outputError(self, node, y):
		node.setError( self.gPrime(node.input) * (y - node.activation) )

	# Compute error at hidden nodes according to gradient descent rule
	# 	Delta_i = g'(in_i) * SUM_{children j} (w_{i,j} * Delta_j)
	# Does not apply to input or dummy nodes
	def hiddenError(self, node):
		delta = 0.0
		for child in node.outNeighbors:
			delta += node.outWeights[child] * child.error
		node.setError(delta * self.gPrime(node.input))

	# returns the output node with largest activation, i.e. the "decision". 
	# For this to work, there must be one output node corresponding to each 'value' of possible output
	def decision(self):
		label = 0
		maxNode = 0
		maxVal = -1.0
		for node in self.outputLayer():
			if(node.activation > maxVal):
				maxVal = node.activation
				maxNode = label
			label += 1
		return maxNode


	# Run back propagation of (examples, outputs) for given number of epochs.
	def train(self, examples, outputs, epochs, stepSize, toDecay):
		global alpha, decay, time, numIter
		alpha = stepSize
		decay = toDecay
		numIter = epochs
		accuracy = []
		while (time < epochs):
			accuracy.append(self.backPropLearn(examples, outputs))
			time += 1
		time = 0
		return accuracy

	def trainWithCrossValidation(self, examples, outputs, epochs, stepSize, toDecay, k = 10):
		errors = []
		t = k
		# Select 1/k out example + output pairs to test on. Train on rest of data.
		while (t > 0):
			(testExs, testOuts) = partition(examples, outputs, k)
			self.train(examples, outputs, epochs, stepSize, toDecay)
			errors.append(self.getAccuracy(testExs, testOuts))
			merge(examples, testExs)
			merge(outputs, testOuts)
			t -= 1
		print("Test Set Scores: " + str(errors))
		print("Averaged: " + str(sum(errors)/len(errors)))

	def toString(self):
		string = ""
		for i in range(1,self.L+1):
			j = 1
			st = ""
			for node in self.layers[i]:
				for val in node.inWeights.values():
					st += '%.5f' % val + " "
				string += "Layer " + str(i) + ", node " + str(j) + " in-weights: " + st + "\n"
				j += 1
		return string

	def toFile(self, filename = "network.txt"):
		f = open(filename, "w")
		f.write(self.toString())
		f.close()
