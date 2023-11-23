import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns
from sklearn import datasets
from sklearn import manifold
from sklearn import tree

clf = tree.DecisionTreeClassifier(max_depth=3)

cols = ['fixed acidity', 'volatile acidity',
        'citric acid', 'residual sugar', 'chlorides',
        'free sulfur dioxide', 'total sulfur dioxide', 'density', 'pH',
        'sulphates',
        'alcohol']

clf.fit(df_train[cols],df_test.quality)
