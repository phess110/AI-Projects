#!/usr/bin/env python3
import sys
import random as rand
import NeuralNet as NN
import NeuralNetIO as NNio
'''
# For graph-making only: must uncomment to get graphs
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
'''
# Read argv
filename = ""
numIter = 100
alpha = 0.1
decay = False
crossValidation = False
showGraph = False 

for i in range(0, len(sys.argv)):
	if(sys.argv[i] == '-f'):
		filename = sys.argv[i+1].lower()
		i += 1
	elif(sys.argv[i] == '-n'):
		numIter = int(sys.argv[i+1])
	elif(sys.argv[i] == '-d'):
		decay = True
	elif(sys.argv[i] == '-a'):
		alpha = float(sys.argv[i+1])
		i += 1
	elif(sys.argv[i] == '-k'):
		crossValidation = True
	elif(sys.argv[i] == '-g'):
		showGraph = True

# Generates num examples for xor NN.
def xorExamples(num):
	exs = []
	outs = []
	for i in range(num):
		e1 = rand.randint(0,1)
		e2 = rand.randint(0,1)
		exs.append([float(e1), float(e2)])
		outs.append([1.0, 0.0] if (e1 == e2) else [0.0, 1.0])
	return (exs, outs)

# returns a list of example/output pairs for the majority NN. 
# num = num of generated examples
# arity = arity of example
def majorityExamples(arity, num):
	exs = []
	outs = []
	for i in range(num):
		count = 0 
		ex = []
		for j in range(arity):
			val = rand.randint(0,1)
			count += val
			ex.append(float(val))
		exs.append(ex)
		outs.append([1.0, 0.0] if (2*count < arity) else [0.0, 1.0])
	return (exs, outs)

# Cross validation, k = 10. output the results of each trial and the overall average accuracy
def runNetwork(file):
	if("xor" in file.lower()):
		nn = NN.NeuralNet([2,2,2])
		(exs, out) = xorExamples(100)
		if(crossValidation):
			nn.trainWithCrossValidation(exs, out, numIter, alpha, decay)
		else:
			run(nn, exs, out, numIter, title = "XOR Neural Net")

	elif("majority" in file.lower()):
		nn = NN.NeuralNet([11,2])
		(exs, out) = majorityExamples(11, 200)
		if(crossValidation):
			nn.trainWithCrossValidation(exs, out, numIter, alpha, decay)
		else:
			run(nn, exs, out, numIter, title = "Majority Neural Net")

	elif("iris" in file.lower()):
		nn = NN.NeuralNet([4,7,3])
		file = NNio.DataReader("iris.data.txt")
		(exs, outputs) = file.readDataFile()
		out = []
		for y in outputs:
			if(y == "Iris-setosa"):
				out.append([1.,0.,0.])
			elif(y == "Iris-versicolor"):
				out.append([0.,1.,0.])
			else:
				out.append([0.,0.,1.])
		if(crossValidation):
			nn.trainWithCrossValidation(exs, out, numIter, alpha, decay)
		else:
			run(nn, exs, out, numIter, title = "Iris Neural Net")

	elif("images" in file.lower()):
		nn = NN.NeuralNet([784,32,10])
		file1 = NNio.ImageReader("train-images-idx3-ubyte")
		file2 = NNio.LabelReader("train-labels-idx1-ubyte")
		# train here
		user_input = int(input("Enter number of training samples to run (1-" + str(file1.count) + ") or 0 to start testing:"))
		accuracy = [] 
		while(user_input > 0):
			for i in range(0, user_input, 100):
				(trainExs, trainOuts) = file1.readImageFile(file2, min(100, user_input)) 
				accuracy.extend(nn.train(trainExs, trainOuts, 1, alpha, decay))
			user_input = int(input("Enter number of training samples to run (1-" + str(file1.count) + ") or 0 to start testing:"))
		if(showGraph):
			errors = list(map(lambda x: 1-x, accuracy))
			plotOverTime(accuracy, scale = 100, title = "MNIST NN", yAxisLabel = "% Correct",  xAxisLabel = "Samples Trained")
			plotOverTime(errors, scale = 100, title = "MNIST NN", yAxisLabel="Error Rate", xAxisLabel = "Samples Trained")
			nn.toFile("Trained-MNIST-Network.txt")
		print("Accuracy: " + str(accuracy))
		print("Final Accuracy: " + str(accuracy[-1]))
		file1.close()
		file2.close()
		# test data
		file3 = NNio.ImageReader("t10k-images-idx3-ubyte")
		file4 = NNio.LabelReader("t10k-labels-idx1-ubyte")
		user_input = int(input("Enter number of test samples to run (1-"+ str(file3.count) + ") or 0 to stop:"))
		while(user_input > 0):
			(testExs, testOuts) = file3.readImageFile(file4, user_input)
			print("Test Accuracy: " + str(nn.backPropLearn(testExs, testOuts)))
			user_input = int(input("Enter number of test samples to run (1-"+ str(file3.count) + ") or 0 to stop:"))
		file3.close()
		file4.close()

		'''
		# Prints example to stdout
		for e, o in zip(trainExs,trainOutputs):
			for i in range(0,28**2, 28):
				st = ""
				for j in range(i, i+28):
					if(e[j] == 0.0):
						st += " "
					else:
						st += "."
				print(st)
			print(o)
		'''

# epochs = number of backPropagations to be performed on examples x outputs
# scale = 
# Records accuracy after each era.
def run(nn, examples, outputs, epochs, scale = None, title = "Neural Net Learning", xAxisLabel = "Epochs"):
	if(scale == None):
		scale = 1
	accuracy = nn.train(examples, outputs, epochs, alpha, decay)
	errors = list(map(lambda x: 1-x, accuracy))
	if(showGraph):
		plotOverTime(accuracy, scale, title, xAxisLabel, yAxisLabel = "% Correct")
		plotOverTime(errors, scale, title, xAxisLabel, yAxisLabel = "Error Rate")
	print("Accuracy: " + str(accuracy))
	print("Final Accuracy: " + str(accuracy[-1]))

# Scatter plot with points (data[t], scale*t)
def plotOverTime(data, scale, title, xAxisLabel, yAxisLabel):
	df=pd.DataFrame({'x': range(scale, scale*(len(data)+1), scale) , 'y': data })
	plt.title(title, size = 14)
	plt.xlabel(xAxisLabel)
	plt.ylabel(yAxisLabel)
	plt.plot('x', 'y', data=df, color='blue')
	plt.show()

runNetwork(filename)
