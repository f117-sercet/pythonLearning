# 文件操作
# open 函数属于系统内置函数，支持对字符串或二进制文件的打开操作，返回可操作的文件对象 ‘w’可写方式打开，‘a’，追加写，不覆盖
# write 写方法
# read 读方法
# readline 一次读一行
# tell() 当前文件可以读写的位置
newFile = 'd:\\t1.txt'
b_new_file = open(newFile, 'r')
b_new_file.write("啥几把玩意儿")
tt = b_new_file.readline()
print(tt)
b_new_file.close()
print("建立成功")
