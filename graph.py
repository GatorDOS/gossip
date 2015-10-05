import pylab
import csv
import math

line_time = []
line_totalNoOfNodes = []
f = open("line.csv", 'rt')
try:
    reader = csv.reader(f)
    for row in reader:
        line_totalNoOfNodes.append(math.log(float(row[0]), 10))
        line_time.append(math.log(math.log(float(row[1]), 10)))
finally:
    f.close()

fullNetwork_time = []
fullNetwork_totalNoOfNodes = []
f = open("fullNetwork.csv", 'rt')
try:
    reader = csv.reader(f)
    for row in reader:
        fullNetwork_totalNoOfNodes.append(math.log(float(row[0]), 10))
        fullNetwork_time.append(math.log(float(row[1]), 10))
finally:
    f.close()

threeDGrid_time = []
threeDGrid_totalNoOfNodes = []
f = open("3dGrid.csv", 'rt')
try:
    reader = csv.reader(f)
    for row in reader:
        threeDGrid_totalNoOfNodes.append(math.log(float(row[0]), 10))
        threeDGrid_time.append(math.log(float(row[1]), 10))
finally:
    f.close()

imperfect3DGrid_time = []
imperfect3DGrid_totalNoOfNodes = []
f = open("Imp3dGrid.csv", 'rt')
try:
    reader = csv.reader(f)
    for row in reader:
        imperfect3DGrid_totalNoOfNodes.append(math.log(float(row[0]), 10))
        imperfect3DGrid_time.append(math.log(float(row[1]), 10))
finally:
    f.close()

# pylab.plot(line_time, line_totalNoOfNodes)
pylab.plot(line_totalNoOfNodes,line_time )
# pylab.plot(fullNetwork_time, fullNetwork_totalNoOfNodes)
pylab.plot(fullNetwork_totalNoOfNodes, fullNetwork_time)
# pylab.plot(threeDGrid_time, threeDGrid_totalNoOfNodes)
pylab.plot(threeDGrid_totalNoOfNodes, threeDGrid_time)
# pylab.plot(imperfect3DGrid_time, imperfect3DGrid_totalNoOfNodes)
pylab.plot(imperfect3DGrid_totalNoOfNodes, imperfect3DGrid_time)
pylab.legend(['line', 'Full Network', '3D Grid', 'Imp. 3D Grid'], loc='upper left')
pylab.show()
