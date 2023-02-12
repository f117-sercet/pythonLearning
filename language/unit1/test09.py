# 元组
# 元组不能对其元素进行变动，而列表允许 2.元组用小括号表示，而列表用大括号表示
# 是不可变的序列，也是一种可以存储各种数据类型的集合，元素之间用逗号隔开。

# 元组的基本语法
print(())  # 空元组

test2 = (1, 2, 2, '1', '2')

print(test2)
print(type(test2))

# python 语言为元组提供了一种特殊的默认格式-省略小括号的元组。在变量或常量中间用逗号分隔时，可以把这些看作是元组对象。

name, age = 'tom', 19

print(type(name))

# 元组的基本操作 1.建立元组，删除元组，查找元素，合并元组，统计元组，转换元组

# 基本操作方法 count:统计指定元素个数;index:返回指定元素的下标
# 内置函数:
# len:统计元组元素个数;
# max:返回元组中最大值的元素 min:返回元组中最小值的元素
# tuple:将列表转换为元组
# type：返回对象类型 del:删除整个元组对象； sum:对元组对象的所有元素求和

# 建立元组
select_nums = (1, 2, 3, 4, 5)
if select_nums.__contains__(6):
    print("yes")
else:
    print(select_nums)

# 查找元素
print(select_nums[1])

# 删除元组 对元组的元素删除是不允许的。但可以通过del函数，实现对整个元组对象的删除

select_cnum = select_nums
# del select_nums


# 统计元素  方法一:count; 方法二： len

# 合并元素
t1 = [1, 2, 3]
t2 = ["", ":", "OO"]
t3 = t2 + t1

print(t3)

# 元组转换 list：转换列表 tuple：转为元组

list = ["Tom", "John", "Tim"]
to_tuple = tuple(list)
print(type(to_tuple))
