import numpy as np

# Boolean decision trees - to generalize this we just need to generalize the entropy function

class decisionNode:
	def __init__(self, attributeName):
		self.children = {}
		self.attribute = attributeName

	def addBranch(self, value, child):
		self.children[value] = child

class decision:
	def __init__(self, decision):
		self.decision = decision

class decisionTree:
	def __init__(self):
		self.root = decisionNode()

	def getDecision(self, example):


def decisionTreeLearn(examples, attributes, parentExamples):
	if not examples: # examples = empty
		return pluralityVal(parentExamples)
	elif sameClass(examples):
		return decisionFunction[next(iter(examples))]
	elif not attributes:
		return pluralityVal(examples)
	else:
		A = None
		Aval = 0.0
		for a in attributes:
			i = importance(a, examples)
			if( i >= Aval):
				Aval = i
				A = a
		tree = decisionTree()
		for value in attributeFunction[A]:
			exs = {e for e in examples if e[A] = value}
			attr = attributes.copy()
			attr.remove(A)
			subtree = decisionTreeLearn(exs, attr, examples)
			tree.root.children[value] = subtree
		return tree

# Check if all elements of S map to same classification
def sameClass(S):
	for s in S:
		# todo

# Returns most common classification value amongst the examples in set S
def pluralityVal(S):
	p = Counter()
	for s in S:
		p[decisionFunction[s]] += 1
	return p.most_common(1)[0][0]

# Calculates the importance of attribute a on the given set of examples
def importance(a, examples):
	remainder = 0.0
	sample = next(iter(examples)) # get some example
	total = len(examples)
	p = Counter()
	for example in examples:
		p[decisionFunction[example]] += 1
	for value in attributeFunction[a]:
		c = Counter()
		s = None
		for example in examples:
			if(example[a] = value):
				s = example
				c[decisionFunction[example]] += 1
		if s == None:
			continue
		subcatSize = sum(c.values())
		remainder += subcatSize/total * entropy( c[decisionFunction[s]] / subcatSize )
	return entropy(p[decisionFunction[sample]]/total) - remainder

# Returns the entropy of the distribution: Bernoulli(p)
def entropy(p):
	if(p == 0 or p == 1):
		return 0.0
	return -p*np.log2(p) - (1-p)*np.log2(1-p)


# input -> decisionFunction : maps examples to decisions
# 		-> attributeFunction : maps attributes to a list of their values

'''
# Generalized entropy function
def entropy(*args):
	e = 0.0
	for p in args:
		e += p * np.log2(p)
	return e
'''
