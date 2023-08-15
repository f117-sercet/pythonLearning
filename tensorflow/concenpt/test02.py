import tensorflow as tf


# 张量——不同维度的数组
# 张量只是引用了程序中的运算结果而不是一个真正的数组。
# 张量保存的是运算结果的属性，而不是真正的数字。

def test():
    a = tf.constant([1.0, 2.0], name="a")
    b = tf.constant([3.0, 4.0], name="b")

    result = a + b
    print(result)



if __name__ == '__main__':

    test()
