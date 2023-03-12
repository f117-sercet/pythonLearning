from tkinter import StringVar

import pymysql

connect = pymysql.connect(host='localhost', user='root', password='123123', port=3306)

connect.close()
cur = connect.cursor()
sql = "showdaatabases;"
i = cur.execute(sql)
connect.commit()
curs= StringVar()
print(i)

connect.close()
