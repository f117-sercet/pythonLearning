import os
import openpyxl


# 获取活动表

path =r"C:\Users\LENOVO\Desktop"
os.chdir(path)  # 修改工作路径

workbook = openpyxl.load_workbook('test.xlsx')  # 返回一个workbook数据类型的值
sheet = workbook.active     # 获取活动表
print(sheet)

#获取表格的尺寸
print(sheet.dimensions) #获取表格的尺寸大小