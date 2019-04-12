import random

class unit:

	def __init__(self):
		self.parents = []
		self.outNeighbors = []
		self.inWeights = {} # dict mapping parent i to w_{i,j}, forwardprop
		self.outWeights = {} # dict mapping outNeighbor j to w_{i,j}, backprop
		self.activation = 0
		self.input = 0.0
		self.error = 0.0

	def addEdge(self, neighbor, weight = random.random()):
		self.outNeighbor.append(neighbor)
		self.outWeights[neighbor] = weight
		neighbor.parents.append(self)
		neighbor.inWeights[self] = weight

	def setInput(in_j):
		self.input = in_j

	def activate(self, energy):
		self.activation = energy

	def computeInput():
		s = 0.0
		for parent in parents:
			s += parent.activation * inWeights[parent]
		setInput(s)

	def setError(self, delta):
		self.error = delta
