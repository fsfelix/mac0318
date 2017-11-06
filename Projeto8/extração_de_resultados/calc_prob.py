import numpy as np
import pandas as pd

INTERVALO = [25, 30]

df_longe = pd.read_csv('nossa_longe.csv')
df_med = pd.read_csv('nossa_med.csv')
df_perto = pd.read_csv('nossa_perto.csv')

# Leituras do Giuliano
df_1 = pd.read_csv('leitura_1.csv')
df_2 = pd.read_csv('leitura_2.csv')

def create_hash(df):
    exp_read_list = {}
    for read, exp in zip(df['read'], df['expected']):
        if float(exp) in exp_read_list.keys():
            exp_read_list[float(exp)].append(float(read))
        else:
            exp_read_list[float(exp)] = []
            exp_read_list[float(exp)].append(float(read))
    return exp_read_list

frames = [df_longe, df_med, df_perto, df_1, df_2]
all_df = pd.concat(frames)
hash_all = create_hash(all_df)

def measures_and_probabilities(expected_interval, hash_all):
    keys = hash_all.keys()
    min_dist = np.inf
    reads = []
    for k in keys:
        if k >= expected_interval[0] and k <= expected_interval[1]:
            reads += hash_all[k]
                
    
    value_p = {}
    N = len(reads)
    
    for r in reads:
        if r not in value_p.keys():
            value_p[r] = reads.count(r)/N
            
    return sorted(value_p.items(), key=lambda x:x[1])

m_and_p = measures_and_probabilities(INTERVALO, hash_all)
print(m_and_p)
