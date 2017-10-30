import math
import sys

class Line:
    def __init__(self, x0, y0, x1, y1):

        if (x0 == x1):
            self.m = math.inf
            self.x0 = x0
            self.y0 = y0
            self.x1 = x1
            self.y1 = y1
            self.a = 1
            self.b = 0
            self.c = x0

        else:
            self.m = (y0 - y1)/(x0 - x1)
            self.x0 = x0
            self.y0 = y0
            self.x1 = x1
            self.y1 = y1
            self.a = -self.m
            self.b = 1
            self.c = (self.m*x0) - y0

    def toLineJava(self):
        return "new Line((float) {}, (float) {}, (float) {}, (float) {})".format(self.x0, self.y0, self.x1, self.y1)

    def __str__(self):
        return "{}x + {}y + {} = 0".format(self.a, self.b, self.c)

def toJava(lines):
    # Gera o código em java já compilável pelo nxjpcc
    java = open("drawMap.java", "w+")
    java.write("import java.util.ArrayList;\n")
    java.write("import java.util.Collections;\n")
    java.write("import lejos.geom.*;\n")
    java.write("import lejos.robotics.mapping.LineMap;\n")

    #print("static Line[] lines = {")
    java.write("public class drawMap {\n")
    java.write("static Line[] lines = {\n")

    for i in range(len(lines)-1):
        l = lines[i]
        #print(l.toLineJava() + ',')
        java.write(l.toLineJava() + ',' + '\n')
    l = lines[len(lines) - 1]
    java.write(l.toLineJava() + '\n')
    java.write('}; \n')

    maincode = open('main.txt')
    for line in maincode:
        java.write(line)
    java.write('} \n')

def distTo(line, x1, y1):
    a = line.a
    b = line.b
    c = line.c
    num = abs(a*x1 + b*y1 + c)
    den = math.sqrt(a**2 + b**2)
    return num / den

def extract_lines(line, points, thres, res):
    if (len(points) == 0):
        res += [line]
        return
    max_dist = -1
    for i in range(len(points)):
        p = points[i]
        dist = distTo(line, p[0], p[1])
        if (dist > max_dist):
            max_dist = dist
            p_max = p
            i_max = i

    if max_dist > thres:
        line1 = Line(line.x0, line.y0, p_max[0], p_max[1])
        line2 = Line(p_max[0], p_max[1], line.x1, line.y1)

        points.remove(p_max)
        extract_lines(line1, points[:i_max], thres, res)
        extract_lines(line2, points[i_max + 1:], thres, res)
    else:
        res += [line]
    return

def init_extract(points, thres):
    p0 = points[0]
    pn = points[len(points) - 1]
    points.remove(p0)
    points.remove(pn)
    line = Line(p0[0], p0[1], pn[0], pn[1])
    res = []
    extract_lines(line, points, thres, res)
    return res

def euclideanDist(p1, p2):
    return math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]))

def polarToEuclidian(pose, points, cont):
    euclidianPoints = []

    x0 = pose[0]
    y0 = pose[1]
    teta = pose[2]

    for i in range(len(points)):
        x_tmp = x0 + points[i]*math.cos(math.radians(teta + (-90 + i*2)))
        y_tmp = y0 + points[i]*math.sin(math.radians(teta + (-90 + i*2)))
        # x = x_tmp*math.cos(math.radians(teta)) + y_tmp*math.sin(math.radians(teta))
        # y = -x_tmp*math.sin(math.radians(teta)) + y_2tmp*math.cos(math.radians(teta))

        x = x_tmp*math.cos(math.radians(teta)) - y_tmp*math.sin(math.radians(teta)) + x0
        y = x_tmp*math.sin(math.radians(teta)) + y_tmp*math.cos(math.radians(teta)) + y0
        euclidianPoints.append((x, y, (cont * 90 + i)))  # um numero adicionado para cada ponto lido, testando isso

    return euclidianPoints

def main():
    FILE_DIR = sys.argv[1]
    thres = 20 # threshold do algoritmo

    f = open(FILE_DIR)

    all_points = []
    lines_res = []

    cont = 0

    while True:

        poseLine = f.readline()
        pointsLine = f.readline()

        if not poseLine:
            break

        poseSplit = poseLine.split(' ')
        pointsSplit = pointsLine.split(' ')

        pose = [float(poseSplit[i]) for i in (range(len(poseSplit) - 1))]
        points = [float(pointsSplit[i]) for i in (range(len(pointsSplit) - 1))]

        euc = polarToEuclidian(pose, points, cont)
        cont = cont + 1

        all_points += euc

    # devemos gerar as linhas após todos os pontos, pois temos mais informação sobre o mundo real
    lines_res = init_extract(all_points, thres)

    toJava(lines_res)


if __name__ == "__main__":
    main()
