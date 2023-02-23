# values()可获取字典值;
d2 = {'Tom': 10, 'Jim': 5, 'Jack': "啥"}
for get_v in d2.values():
    print(get_v)

# 字典的其他操作方法:
# in:操作成员:
for get_v in d2.values():
    print(get_v)
# clear()方法:
d5 = {"Token": "123", "Mike": "456", "Jim": 123}
print(d5)
d6 = d5.copy()
d6.clear()
print(d6)
# copy()方法: copy后显示的地址不一样,直接赋值会显示一样的地址值
d7 = d5.copy()
i = id(d7)
print(i)
j = id(d5)
print(j)
# fromKeys():
d8 = {}.fromkeys(d7)
print(d8)
