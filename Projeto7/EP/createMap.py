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

    for p in points:

        dist = distTo(line, p[0], p[1])
        
        if (dist > max_dist):
            max_dist = dist
            p_max = p

    if max_dist > thres:
        
        line1 = Line(line.x0, line.y0, p_max[0], p_max[1])
        line2 = Line(p_max[0], p_max[1], line.x1, line.y1)

        points.remove(p_max)
        extract_lines(line1, points, thres, res)
        extract_lines(line2, points, thres, res)
    
    else:
        res += [line]
    
    return

def extract_lines_with_labels(line, points, thres, res, start, end):
    
    if (len(points) == 0):
        # quando não tem mais nenhum ponto, essa reta tem que entrar
        res += [line]
        return
    
    max_dist = -1
    mid = 0

    # Checa se o ponto está no conjunto permitido de rotulos, se sim calcula distancia
    for p in points:
        if start <= p[2] and p[2] < end:
            dist = distTo(line, p[0], p[1])
            if (dist > max_dist):
                max_dist = dist
                p_max = p
                mid = p[2]

    if max_dist > thres:
        
        line1 = Line(line.x0, line.y0, p_max[0], p_max[1])
        line2 = Line(p_max[0], p_max[1], line.x1, line.y1)
        points.remove(p_max)

        print("Chamada para mid: ", mid )
        extract_lines_with_labels(line, points, thres, res, 0, mid)
        extract_lines_with_labels(line, points, thres, res, mid, len(points))
        
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

def init_extract2(points, thres):
    # comeca a extrair linhas comecando com os dois pontos mais distantes
    p0, pn = mostDistantPoint(points)
    line = Line(p0[0], p0[1], pn[0], pn[1])
    res = []
    extract_lines(line, points, thres, res)
    return res

def init_extract_with_labels(points, thres):
    p0, pn = mostDistantPoint(points)
    line = Line(p0[0], p0[1], pn[0], pn[1])
    res = []
    extract_lines_with_labels(line, points, thres, res, 0, len(points))
    return res

def euclideanDist(p1, p2):
    return math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]))

def mostDistantPoint(points):
    
    x = 0
    p0 = points[0]
    points.remove(p0)

    for point in points:
        if euclideanDist(p0, point) > x:
            x = euclideanDist(p0, point)
            mostDistant = point

    points.remove(mostDistant)
    return p0, mostDistant

def polarToEuclidian(pose, points, cont):
    euclidianPoints = []

    x0 = pose[0]
    y0 = pose[1]
    teta = pose[2]

    for i in range(len(points)):
        x_tmp = x0 + points[i]*math.cos(math.radians(teta + (-90 + i*2)))
        y_tmp = y0 + points[i]*math.sin(math.radians(teta + (-90 + i*2)))

        x = x_tmp*math.cos(math.radians(teta)) + y_tmp*math.sin(math.radians(teta))
        y = -x_tmp*math.sin(math.radians(teta)) + y_tmp*math.cos(math.radians(teta))

        euclidianPoints.append((x, y, (cont * 90 + i)))  # um numero adicionado para cada ponto lido, testando isso

    return euclidianPoints

def plotting(points):
    # plotar uma nuvem de pontos
    import matplotlib.pyplot as plt

    x = []
    y = []

    for point in points:
        x.append(point[0])
        y.append(point[1])

    plt.scatter(x, y)
    plt.show()

def plottingPositive(points):
    # plotar uma nuvem de pontos do primeiro quadrante do plano cartesiano
    import matplotlib.pyplot as plt
    x = []
    y = []
    for point in points:
        if point[0] > 0 and point[1] > 0:
            x.append(point[0])
            y.append(point[1])
    plt.scatter(x, y)
    plt.show()

def plotALine(line):
    # colocar uma linha no plot
    import matplotlib.pyplot as plt
    plt.plot([line.x0, line.x1], [line.y0, line.y1])

def plotLines(lines):
    # plotar varias linhas
    import matplotlib.pyplot as plt
    for line in lines:
        plotALine(line)
    plt.show()

def positivePoints(points):
    # retorna apenas os pontos que tem x e y positivos
    positive = []
    for point in points:
        if point[0] > 0 and point[1] > 0:
            positive.append(point)
    return positive

def main():

    FILE_DIR = "sonar_labirinto.txt"
    FIXED_DIR = "sonar_fixado.txt"
    FIRST_DIR = "sonar_primeiro_scan.txt"
    SECOND_DIR = "sonar_segundo_scan.txt"

    f = open(FIRST_DIR)

    all_points = []
    lines_res = []
    thres = 50

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
    

    # lines_res = init_extract_with_labels(all_points, thres)
    
    # all_points = [(1.0, 1.0), (2.0, 1.0), (1.0, 2.0), (2.0, 2.0)]
    # for i in range(0, 100):
    #     all_points.append(( 1 + (i / 100) , 2 + (-i / 100) ))

    # all_points = [(1.0, 5.0), (2.0, 5.0), (3.0, 5.0), (4.0, 4.0), 
    #               (5.0, 3.0), (6.0, 5.0), (6.0, 2.0), (6.0, 1.0)]

    # all_points = [(1.0, 5.0), 
    #               (6.0, 5.0),
    #               (6.0, 1.0)]
    # lines_res = init_extract2(all_points, 3)

    # all_points = [(1.0, 5.0),
    #               (2.0, 5.0), 
    #               (6.0, 5.0),
    #               (6.0, 2.0),
    #               (6.0, 1.0)]
    # lines_res = init_extract2(all_points, 3)
    
    # plottingPositive(all_points)
    
    # all_points = [(1.0, 2.0), (2.0, 4.0), (3.0, 1.0), (2.5, 10.0), (5.0, 5.0)]
    # lines_res = init_extract2(all_points, 3)
    
    # plotLines (lines_res)

    toJava(lines_res)


main()
