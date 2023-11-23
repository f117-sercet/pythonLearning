import tensorflow as tf

if __name__ == '__main__':
    tf.compat.v1.disable_eager_execution()
    a = tf.constant([1.0, 2.0, 3.0, 4.0])
    b = tf.constant([6.0, 5.0, 4.0, 3.0])
with tf.compat.v1.Session() as sess:
    print(tf.greater(b, a).eval())
    print(tf.where(tf.greater(b,a), b, a).eval())
