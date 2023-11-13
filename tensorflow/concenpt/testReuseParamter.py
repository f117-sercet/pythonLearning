import tensorflow as tf


def test():
    with tf.compat.v1.variable_scope("one"):
        a = tf.compat.v1.get_variable("a", [1], initializer=tf.compat.v1.constant_initializer(1.0))

    # 在生成上下文管理器时，将reuse参数设置成True时，这样getVariable()函数将直接获取  已声明的变量且只能获取已经声明的变量
    with tf.compat.v1.variable_scope("two", reuse=True):
        a2 = tf.compat.v1.get_variable("a", [1])
    print(a.name, a2.name)


if __name__ == '__main__':
    test()
