import tensorflow as tf

# tensorflow 测试程序


if __name__ == '__main__':
    print(tf.__version__)

    # 测试程序2

    tf.compat.v1.disable_eager_execution()
    test = tf.constant('hello,world')
    sess = tf.compat.v1.Session()
    print(sess.run(test))