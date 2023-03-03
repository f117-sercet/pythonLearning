# 传递任意参数
# 使用格式:函数名([param1,param2,param]* paramX)
def watermelon(name, *attributes):
    print(name)
    print(type(attributes))
    description = ""
    for get_t in attributes:
        description += '' + get_t
        print(description)


######
watermelon('西瓜', '甜', '圆形', '绿色')


#### 传递任意数量的键值对
def watermelons(name, **attributes):
    print(name)
    print(type(attributes))
    return attributes

    print(watermelons('西瓜', taste='甜', shape='原型'))

#  函数传递对象总结
# 不可变对象：在函数里进行修改数值，会变成新的对象
# 可变对象，在函数里进行值修改，函数内外还是同一个对象，但是值同步发生变化

