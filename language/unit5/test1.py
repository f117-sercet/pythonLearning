# 类
class Box1():

    def __init__(self, length1, width1, height1):
        self.length = length1
        self.width = width1
        self.height = height1

    def volume(self):
        return self.length * self.width * self.height


my_box = Box1(10, 10, 10)
v = my_box.volume()

print(v)
