#!/usr/bin/env python3
import sys 
import math
import NeuralNetIO
import Unit
import struct
from os import path

# Activation Functions
logistic = lambda x : 1.0/(1.0 + math.exp(-x))
hard = lambda x : 1 if x >= 0.0 else 0
logisticDerivative = lambda x : logistic(x) * (1 - logistic(x))

# Global Vars
time = 0
decay = False
alpha = 0.00125

class NeuralNet:
	# pass a list of inputNodes to the neural net
	def __init__(self, inputNodes, activationFunction, L):
		self.inputs = inputNodes
		self.L = 1 	# number of layers	
		self.layers = {1:inputNodes} # layers : mapping from 1..L -> units in layer
		self.g = activationFunction

	def addLayer(self, numUnits):
		self.L = self.L + 1
		newLayer = []
		for i in range(numUnits):
			n = unit()
			newLayer.append(n)
			for parent in self.layers[self.L-1]:
				parent.addEdge(n)
		self.layers[self.L] = newLayer
	
	def backPropLearn(self, examples):
		for example in examples:
			# forward prop
			inputNodes = self.layers[1] # set activation inputs from example
			for i in range(len(example)):
				inputNodes[i].activate(example[i])
			for l in range(2,L+1):
				for node in self.layers[l]:
					node.computeInput()
					node.activate(self.g(node.input))
			# back propagation - update errors
			for node in self.layers[self.L]:
				node.outputError() # TODO
			for l in range(1,L,-1):
				for node in self.layers[i]:
					node.hiddenError() # TODO
			for l in range(1,L): # update weights
				for i in self.layers[l]:
					for j in self.layers[l+1]:
						i.updateWeight(j,alpha) # TODO
		return self

# Read argv
activationFunction = hard
for i in range(0, len(sys.argv)):
	if(sys.argv[i] == '-f'):
		filename = sys.argv[i+1].lower()
		i += 1
	elif(sys.argv[i] == '-d'):
		decay = True
	elif(sys.argv[i] == '-a'):
		alpha = float(sys.argv[i+1])
		i += 1
	elif(sys.argv[i] == '-l'):
		activationFunction = logistic

file1 = NeuralNetIO.ImageReader("t10k-images-idx3-ubyte")
file2 = NeuralNetIO.LabelReader("t10k-labels-idx1-ubyte")
#file1.readImageFile(file2)

#file = NeuralNetIO.DataReader(filename)
#(examples, outputs) = file.readDataFile()
#for i in range(len(examples)):
#	print(str(examples[i]) + outputs[i])

# Build neural net 
#nn = NeuralNet([], activationFunction)