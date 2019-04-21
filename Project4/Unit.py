import random as rand 

class unit:
	def __init__(self):
		self.parents = []
		self.outNeighbors = []
		self.inWeights = {} # dict mapping parent i to w_{i,j}, forwardprop
		self.outWeights = {} # dict mapping outNeighbor j to w_{i,j}, backprop
		self.activation = 0.0
		self.input = 0.0
		self.error = 0.0

	def addEdge(self, neighbor):
		w = rand.gauss(0,1)
		self.outNeighbors.append(neighbor)
		self.outWeights[neighbor] = w
		neighbor.parents.append(self)
		neighbor.inWeights[self] = w

	def setInput(self, in_j):
		self.input = in_j

	def activate(self, energy):
		self.activation = energy

	def setError(self, delta): 
		self.error = delta

	def computeInput(self):
		s = 0.0
		for parent in self.parents:
			s += parent.activation * self.inWeights[parent]
		self.setInput(s)

	def updateWeight(self, child, stepSize):
		change = stepSize * self.activation * child.error
		newWeight = self.outWeights[child] + change
		self.outWeights[child] = newWeight
		child.inWeights[self] = newWeight

	def updateWeights(self, stepSize):
		for parent in self.parents:
			change = stepSize * parent.activation * self.error
			newWeight = self.inWeights[parent] + change
			self.inWeights[parent] = newWeight
			parent.outWeights[self] = newWeight
