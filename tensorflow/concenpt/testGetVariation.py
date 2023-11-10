import tensorflow as tf


def test():
    a = tf.compat.v1.Variable(tf.compat.v1.constant(1.0, shape=[1], name="a"))
    # 使用getVariable()创建一个变量
    b = tf.compat.v1.get_variable("a", shape=[1], initializer=tf.compat.v1.constant_initializer(1.0))
    print(b)
    print(a)

    # reuse 参数的默认值False,当variable_scope函数使用reuse=True参数时生成上下文管理器时，这个上下文管理器内所有get_Variable(
    # )函数会直接获取name属性相同的已经创建的变量。如果变量不存在，则get_variable()函数会报错。



if __name__ == '__main__':
    test()
