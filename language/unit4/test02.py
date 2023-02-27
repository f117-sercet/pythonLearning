# 函数模块
def find_factor(nums):
    if type(nums) != int:
        print("输入值类型出错，必须是整数")
        return;
    if nums <= 0:
        print("输入值范围出错，必须是正整数!")
        return
    i = 1
    str1 = ""
    while i <= nums:
        if nums % i == 0:
            str1 = str1 + '' + str(i)
        i += 1
        return str1
