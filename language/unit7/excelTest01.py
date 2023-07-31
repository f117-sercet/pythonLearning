import os
import openpyxl

path = r"C:\Users\LENOVO\Desktop"
os.chdir(path)  # 修改工作路径

workbook = openpyxl.load_workbook('test.xlsx')
print(workbook.sheetnames)

sheet = workbook['123']


# 获取sheet页
print(sheet)




