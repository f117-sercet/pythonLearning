# 字典遍历
# 利用item遍历所有值
d1 = {"Tom": 10, "JIM": 5, "mike": 11, "jack": 12}

for l in d1.items():
    print(l)

# 遍历所有键:
for get in d1:
    print(get)
    print(d1[get])
    print(d1.values())

