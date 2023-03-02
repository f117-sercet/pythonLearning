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
