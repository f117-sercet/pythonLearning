#  冒泡排序

fish_records = [8, 18, 7, 2, 3, 6, 11, 15, 24, 99]

i = 0

compare = 0

fish_len = len(fish_records)
while i < fish_len:
    j = 1
    while j < fish_len - i:
        if fish_records[j - 1] > fish_records[j]:
            compare = fish_records[j - 1]
            fish_records[j - 1] = fish_records[j]
            fish_records[j] = compare
            j += 1
        i += 1
    print(fish_records)
