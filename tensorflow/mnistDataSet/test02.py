import tensorflow as tf

from tensorflow.core.example.tutorials.mnist import input_data

#tf.disable_v2_behavior()


def hidden_layer(input_tensor, weight1, bias1, weight2, bias2, lay_name):
    mnist = input_data.read_data_sets("F:\mnist", one_hot=True)
    # 训练的Bathc大小
    batch_size = 100
    # 初始学习率
    learning_rate = 0.8
    # 学习率的衰减
    learning_rate_decay = 0.99
    # 最大训练步数
    max_steps = 3000

    training_steps = tf.Variable(0,trainable=False)

    layer1 = tf.nn.relu(tf.matmul(input_tensor, weight1) + bias1)
    return tf.matmul(layer1, weight2) + bias2

if __name__ == '__main__':
    tf.compat.v1.disable_eager_execution()
    x = tf.compat.v1.placeholder(tf.float32, [None, 784], name="x-input")
    y_ = tf.compat.v1.placeholder(tf.float32, [None, 10], name="y-output")

    # 生成隐藏层参数
    weight1 = tf.compat.v1.Variable(tf.compat.v1.truncated_normal([784,500],stddev=0.1))
    biase1 = tf.compat.v1.Variable(tf.compat.v1.constant(0.1,shape=[500]))

    # 生成输出层参数，其中weight2包含500个
    weight2 = tf.compat.v1.Variable(tf.compat.v1.truncated_normal([500,10],stddev=0.1))
    biase2 = tf.compat.v1.Variable(tf.compat.v1.constant(0.1,shape=[10]))
    # 计算得到的y值
    y = hidden_layer(x, weight1, biase1, weight2,biase2,"y")
    # test
    print(y)
