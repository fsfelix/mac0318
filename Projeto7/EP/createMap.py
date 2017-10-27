import math

class Line:
    def __init__(self, x0, y0, x1, y1):
        # print("x0 = {}, y0 = {}, x1 = {}, y1 = {}".format(x0, y0, x1, y1))
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


def distTo(line, x1, y1):
    a = line.a
    b = line.b
    c = line.c
    num = abs(a*x1 + b*y1 + c)
    den = math.sqrt(a**2 + b**2)
    return num/den

def extract_lines(line, points, thres, res):
    if (len(points) == 0):
        res += [line]
        # tem que pensar melhor neste caso: quando não tem mais nenhum ponto...
        return

    max_dist = -1

    for p in points:
        dist = distTo(line, p[0], p[1])
        #print(dist)
        if (dist > max_dist):
            max_dist = dist
            p_max = p

    if max_dist > thres:
        #print("x0 = {}, y0 = {}, x1 = {}, y1 = {}".format(line.x0, line.y0, p_max[0], p_max[1]))
        line1 = Line(line.x0, line.y0, p_max[0], p_max[1])
        line2 = Line(p_max[0], p_max[1], line.x1, line.y1)

        points.remove(p_max)
        extract_lines(line1, points, thres, res)
        extract_lines(line2, points, thres, res)
    else:
        res += [line]
    return

# Gera o código em java já compilável pelo nxjpcc

def toJava(lines):
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


def init_extract(points, thres):
    p0 = points[0]
    pn = points[len(points) - 1]

    points.remove(p0)
    points.remove(pn)

    line = Line(p0[0], p0[1], pn[0], pn[1])

    res = []
    extract_lines(line, points, thres, res)
    return res

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

        # print("{}, {}".format(x, y))

        euclidianPoints.append((x, y))

    return euclidianPoints

def main():
    FILE_DIR = "sonar_labirinto.txt"

    f = open(FILE_DIR)

    all_points = []
    lines_res = []
    thres = 5

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
        lines_res += init_extract(euc, thres)
        all_points += euc

    # aqui eu tentava gerar as linhas a partir de todos os pontos
    # mas ficava pior que o gerado para cada leitura de sonar..
    #res = init_extract(all_points, 1)

    toJava(lines_res)

main()
