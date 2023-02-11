# break 语句

cm = "Tom,It's jack"
for i in range(len(cm)):
    print('for循环%d次' % (i + 1))
    if cm[i:i + 3] == 'Tom':
        print('Tom is %d' % i)
        break
print('for继续循环吗？%d次' % (i + 1))


