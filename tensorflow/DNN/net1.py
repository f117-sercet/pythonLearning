import tensorflow as tf

if __name__ == '__main__':
    # 简单神经网络(RNN)

    tf.compat.v1.disable_eager_execution()
    # 指定形状
    x = tf.constant([0.9, 0.85], shape=[1, 2], name="x")
    # 指定权重
    w1 = tf.constant([[0.2, 0.1, 0.3], [0.2, 0.4, 0.3]], shape=[2, 3], name="w1")
    w2 = tf.constant([0.2, 0.5, 0.25], shape=[3, 1], name="w2")

    # 编制值
    b1 = tf.constant([-0.3, 0.1, 0.2], shape=[1, 3], name="b1")
    b2 = tf.constant([-0.3], shape=[1], name="b2")
    # 初始化变量

    init_op = tf.compat.v1.global_variables_initializer()
    a = tf.matmul(x, w1) + b1
    y = tf.matmul(a, w2) + b2

    with tf.compat.v1.Session() as sess:
        sess.run(init_op)
        print(sess.run(y))
