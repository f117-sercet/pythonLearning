import pymysql
# 数据库连接
#连接数据库
conn=pymysql.connect(host = '127.0.0.1' # 连接名称，默认127.0.0.1
,user = 'root' # 用户名
,passwd='123123' # 密码
,port= 3306 # 端口，默认为3306 # 数据库名称
,charset='utf8' # 字符编码
,db='atguigudb1'
)
cur = conn.cursor() # 生成游标对象
sql="select * from `student_info` " # SQL语句
cur.executemany(sql)
data =  cur.fetchall()

dataList= list(data[0])
value1 = data[0].__getitem__(2)
data[0].__init__()
print(data[0])
print(dataList)
print(value1)

cur.close() # 关闭游标
conn.close() # 关闭连接


