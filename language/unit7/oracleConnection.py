import cx_Oracle

# 设置连接参数
connection = cx_Oracle.connect(user="GY", password="Ymlc@2022qaz",
                               dsn="10.131.0.168:1521/hr")

cursor = connection.cursor()
cursor.execute("select * from  hr_job_info")
data = cursor.fetchall()

for i in data[:10]:
    print(i)

cursor.close()
connection.close()
