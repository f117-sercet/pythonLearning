# 字典值修改 1.利用赋值修改键对应的值；2.利用update（）方法修改
d1 = {'Tom': 2, "JIM": 3, 'Mike': 4}
d2 = {'Tom': 10, "Mike": 11}

d1["Mike"] = 14
print(d1)

d1["Nike"] = 15

d1.update(d2)
print(d1)

# pop 方法删除 使用格式D.POP(K[,D])
d1.pop('Mike')
print(d1)

# 利用 popitem() 进行删除
k1, v1 = d1.popitem()
print(k1,v1)
