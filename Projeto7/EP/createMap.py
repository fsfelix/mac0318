import math

def polarToEuclidian(pose, points):
    euclidianPoints = []

    x0 = pose[0]
    y0 = pose[1]
    teta = pose[2]

    for i in range(len(points)):
        x_tmp = x0 + points[i]*math.cos(math.radians(teta + (-90 + i*2)))
        y_tmp = y0 + points[i]*math.sin(math.radians(teta + (-90 + i*2)))

        x = x_tmp*math.cos(math.radians(teta)) + y_tmp*math.sin(math.radians(teta))
        y = -x_tmp*math.sin(math.radians(teta)) + y_tmp*math.cos(math.radians(teta))

        print("{}, {}".format(x, y))

        euclidianPoints.append((x, y))

    return euclidianPoints

def main():
    FILE_DIR = "primeiro_teste.txt"

    f = open(FILE_DIR)

    while True:
        poseLine = f.readline()
        pointsLine = f.readline()


        if not poseLine:
            break

        poseSplit = poseLine.split(' ')
        pointsSplit = pointsLine.split(' ')

        pose = [float(poseSplit[i]) for i in ( range(len(poseSplit) - 1))]
        points = [float(pointsSplit[i]) for i in (range(len(pointsSplit) - 1))]
        euc = polarToEuclidian(pose, points)



main()
