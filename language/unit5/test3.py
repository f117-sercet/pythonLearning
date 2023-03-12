# 静态类类内部没有self关键字，不能被实例化
# 静态类不能通过类名传递参数
# 静态类不支持__init()__初始化参数
# 静太类不能被真正实例化，但他可以集成变量或函数，是一个带结构的数据类型


def a1():
  i = 0
  i += 1


class StaticC:
    name = 'Tom'
    age = 20
    address = 'China'
    call = 151598857


print(StaticC.name)
