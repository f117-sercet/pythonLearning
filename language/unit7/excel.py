from random import random
import openpyxl
import os

## 获取文件路径
path =r"C:\Users\LENOVO\Desktop"
os.chdir(path)  # 修改工作路径

workbook = openpyxl.load_workbook('jobInfo.xlsx')  # 返回一个workbook数据类型的值
sheet = workbook.active

print(sheet.dimensions)


## 获取相对应的列名

dataColumn={}

## 获取指定的列的数据

## 连接数据库，然后更新数据

# 获取指定列的数据
dataMapLists =list(sheet.iter_rows(min_row=2,max_row=2,min_col=2))

print(dataMapLists)




