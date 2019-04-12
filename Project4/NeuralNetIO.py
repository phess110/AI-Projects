from os import path

class FileReader:
	def __init__(self, fileName, mode = "rt"):
		self.file = open(path.relpath("NNexamples/" + fileName), mode)

class DataReader(FileReader):
	def __init__(self, fileName):
		FileReader.__init__(fileName)

	def readDataFile(self):
		examples = []
		outputs = []
		with self.file as f:
			for line in f:
				data = line.split(',')
				outputs.append(data.pop(len(data)-1).rstrip())
				ex = [1.0]
				for entry in data:
						ex.append(float(entry))
				examples.append(ex)
		return (examples, outputs)

class LabelReader(FileReader):
	def __init__(self, fileName):
		FileReader.__init__(self, fileName, mode = "rb")
		(magic, numSamples) = self.readHeader()
		if(magic != 2049):
			raise Exception("Invalid input.")

	def readHeader(self):
		t = []
		for i in range(0, 2):
			t.append(int.from_bytes(self.file.read(4), "big"))
		return tuple(t)

	def nextLabel(self):
		return int.from_bytes(self.file.read(1), "big")

class ImageReader(FileReader):
	def __init__(self, fileName):
		FileReader.__init__(self, fileName = fileName, mode = "rb")
		(magic, numSamples, nrows, ncols) = self.readHeader()
		if(magic != 2051):
			raise Exception("Invalid input.")
		self.ncols = ncols
		self.nrows = nrows
		self.count = numSamples

	def readHeader(self):
		t = []
		for i in range(0, 4):
			t.append(int.from_bytes(self.file.read(4), "big"))
		return tuple(t)

	def readImageFile(self, labelFile):
		n = self.count
		examples = []
		outputs = []
		if(n != labelFile.count):
			raise Exception("Invalid input.")
		with self.file as f:
			for i in range(n):
				examples.append(nextImage())
				y = [0.0] * 10
				y[labelFile.nextLabel()] = 1.0
				outputs.append(y)
		return (examples, outputs)

	def nextImage(self):
		data = []
		for i in range(self.nrows * self.ncols):
			data.append(float(int.from_bytes(self.file.read(1), "big")))
		return data

