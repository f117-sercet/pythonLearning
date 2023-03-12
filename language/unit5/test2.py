class Color:
    def __init__(self, index=0):
        self.set_color = ['white', 'red', 'black', 'green']

        self.index = index

    def SetColor(self):
        return self.set_color[self.index]


class Box2:
    def __init__(self, length1, width1, height1, c1=0):
        self.length = length1
        self.width = width1
        self.height = height1
        self.color0 = Color(c1).SetColor()

    def volume(self):
        return self.length * self.height * self.width


my_box = Box2(10, 10, 10, 1)
print(my_box.color0)


