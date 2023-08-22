import tensorflow as tf


# placeholder 机制
## 用于在会话运行时 动态提供 输入数据

# 用placeholder定义一个位置

def test():
    tf.compat.v1.disable_eager_execution()
    a = tf.compat.v1.placeholder(dtype=tf.float32, shape=2, name="input")
    b = tf.compat.v1.placeholder(dtype=tf.float32, shape=2, name="input")
    result = a + b

    sess = tf.compat.v1.Session()
    sess.run(result, feed_dict={a: [1.0, 2.0], b: [3.0, 4.0]})
    print(result)


if __name__ == '__main__':
    test()
