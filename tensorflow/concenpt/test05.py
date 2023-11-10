import tensorflow as tf

# 在变量空间外部创建变量 a
if __name__ == '__main__':
    a = tf.compat.v1.get_variable("a", [1], initializer=tf.constant_initializer(1.0))
    print(a.name, a.shape)

    with tf.compat.v1.variable_scope("one"):
        a2 = tf.compat.v1.get_variable("a", [1], initializer=tf.constant_initializer(1.0))
    print(a2.name)

