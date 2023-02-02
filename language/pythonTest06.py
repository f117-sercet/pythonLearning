# 列表元素查找

test = [1, 2, 3, 4, 5, 6, 7, 9]
# in
print(10 in test)

# 切片读取
print(test[1:4])

# 列表元素删除 clear ，pop，remove del

del(test[2])
print(test)

# 元素合并
# 采用直接 + 合并，并赋值给列表，会导致列表在内存中的地址号的改变，不是合并前的对象。
# extend方法，不会导致列表的对象内存地址的变化。

# 列表元素排序 sort
# 若不想改变原有列表，则可以采用sorted直接排序。sorted函数也可以元组，字典等进行排序

test.append(3)
print(sorted(test))







