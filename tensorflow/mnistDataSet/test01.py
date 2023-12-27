import tensorflow.compat.v1 as tf
from tensorflow.core.example.tutorials.mnist import input_data
if __name__ == '__main__':
    mnist = input_data.read_data_sets(  "F:\mnist", one_hot=True)
   ### 数据集测试
    print("Training data and label size: ")
    print(mnist.train.images.shape,mnist.train.labels.shape)
    print("Testing data and label size: ")
    print(mnist.train.images.shape, mnist.train.labels.shape)
    print("Validating data and label size: ")
    print(mnist.validation.images.shape, mnist.validation.labels.shape)
