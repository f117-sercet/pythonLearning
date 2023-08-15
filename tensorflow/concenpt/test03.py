import tensorflow as tf

sess = tf.compat.v1.Session()
with sess.as_default():
    # eval()函数原型eval(self,feed_dict,session)
    result = sess.run()
    print(result.eval())
# 如果sess不是默认的会话，那么在执行计算时，需要对会话加以明确指定，比如下面相同的两句：
# print(sess.run(result))
print(result.eval(session=sess))
