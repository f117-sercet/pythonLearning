import tensorflow as tf

if __name__ == '__main__':
    # 定义常量，用作输入，但要注意x是一个1*2的矩阵。
    #  x = tf.compat.v1.constant([[1.0, 2.0]])
    #
    # 定义变量,当作参数
    # # 通过seed参数设定了随机种子，这样可以保证每次运行得到的结果一致
    # w1 = tf.compat.v1.Variable(tf.compat.v1.random_normal([2, 3], stddev=1, seed=1))
    # w2 = tf.compat.v1.Variable(tf.compat.v1.random_normal([3, 1], stddev=1, seed=1))
    # # 执行矩阵操作，
    # a = tf.compat.v1.matmul(x, w1)
    # y = tf.compat.v1.matmul(a, w2)
    # # 初始化变量
    # init_op = tf.compat.v1.glorot_normal_initializer()
    # sess = tf.compat.v1.Session()
    # sess.run(init_op)
    # value = sess.run(y)
    # print(value)

    tf.compat.v1.disable_eager_execution()
    x = tf.compat.v1.constant([[1.0, 2.0]])
    w1 = tf.compat.v1.Variable(tf.compat.v1.random_normal([2, 3], stddev=1, seed=1))
    w2 = tf.compat.v1.Variable(tf.compat.v1.random_normal([3, 1], stddev=1, seed=1))
    a = tf.compat.v1.matmul(x, w1)
    y = tf.compat.v1.matmul(a, w2)
    tf.compat.v1.assign(w1,w2,validate_shape=False)
    init_op = tf.compat.v1.initialize_all_variables()
    with tf.compat.v1.Session() as sess:
        sess.run(init_op)
        value = sess.run(y)
        print(value)
