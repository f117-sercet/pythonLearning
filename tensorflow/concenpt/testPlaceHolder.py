import tensorflow as tf


def test():
    tf.compat.v1.disable_eager_execution()
    a = tf.compat.v1.placeholder(dtype=tf.float32, shape=(2), name="input")
    b = tf.compat.v1.placeholder(dtype=tf.float32, shape=(4, 2), name="input")
    result = a + b

    sess = tf.compat.v1.Session()
    sess.run(result, feed_dict={a: [1.0, 2.0], b: [[2.0, 4.0], [5.0, 6.0], [7.0, 8.0], [9.0, 10.0]]})
    print("结果是", result)


if __name__ == '__main__':
    test()
