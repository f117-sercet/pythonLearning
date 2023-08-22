import tensorflow as tf

sess = tf.compat.v1.Session()
with sess.as_default():
    # eval()函数原型eval(self,feed_dict,session)
    result = sess.run()
    print(result.eval())
# 如果sess不是默认的会话，那么在执行计算时，需要对会话加以明确指定，比如下面相同的两句：
# print(sess.run(result))
print(result.eval(session=sess))

#
config = tf.compat.v1.ConfigProto(log_device_placement=True, allow_soft_lacenment=True)

sees1 = tf.compat.v1.Session(config=config)

# 用于创建默认会话的时候
sess2 = tf.compat.v1.InteractiveSession(config=config)
