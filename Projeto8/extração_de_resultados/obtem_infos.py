import numpy as np
import pandas as pd

df = pd.read_csv('nossa_longe.csv')
diff = df['read'] - df['expected']
diff_sqrd = np.power(diff, 2)
mean = diff_sqrd.mean()
print ('Nossa média longe, posição: (', df['x'][0], ",", df['y'][0], ",", df['headinng'][0], ")", mean)



df = pd.read_csv('nossa_med.csv')
diff = df['read'] - df['expected']
diff_sqrd = np.power(diff, 2)
mean = diff_sqrd.mean()
print ('Nossa média medio, posição: (', df['x'][0], ",", df['y'][0], ",", df['headinng'][0], ")", mean)



df = pd.read_csv('nossa_perto.csv')
diff = df['read'] - df['expected']
diff_sqrd = np.power(diff, 2)
mean = diff_sqrd.mean()

print ('Nossa média perto, posição: (', df['x'][0], ",", df['y'][0], ",", df['headinng'][0], ")", mean)

