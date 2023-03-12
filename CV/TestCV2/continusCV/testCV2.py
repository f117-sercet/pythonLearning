import cv2
import utils

########################################

webcam = False
path = 'F:/Anaconda/images/1.jpg'
cap = cv2.VideoCapture(0)
cap.set(10, 160)  # 改变亮度
cap.set(3, 680)  # 改变宽度
cap.set(4, 1080)  # 改变高度
scale = 2
wP = 220 * scale
hP = 300 * scale

while True:
    if webcam:
        success, img = cap.read()  # 如果webCam为True，那就打开摄像头
    else:
        img = cv2.imread(path)  # 否则就读图片

    img = cv2.resize(img, (0, 0), None, 0.18, 0.18)
    img, conts = utils.getContours(img, minArea=8000, filter=4)
    if len(conts) != 0:
        biggest = conts[0][2]  # 最大轮廓的拐点位置
        # print(biggest)
        imgWrap = utils.warpImg(img, biggest, wP, hP)

        imgContours2, conts2 = utils.getContours(imgWrap, minArea=2000, filter=4, cThr=[50, 50])

        if len(conts) != 0:
            for obj in conts2:
                cv2.polylines(imgContours2, [obj[2]], True, (0, 255, 0), 2)
                nPoints = utils.reorder(obj[2])
                nW = round((utils.findDis(nPoints[0][0] // scale, nPoints[1][0] // scale) / 10), 1)
                nH = round((utils.findDis(nPoints[0][0] // scale, nPoints[2][0] // scale) / 10), 1)
                # 创建箭头
                cv2.arrowedLine(imgContours2, (nPoints[0][0][0], nPoints[0][0][1]),
                                (nPoints[1][0][0], nPoints[1][0][1]),
                                (255, 0, 255), 3, 8, 0, 0.05)
                cv2.arrowedLine(imgContours2, (nPoints[0][0][0], nPoints[0][0][1]),
                                (nPoints[2][0][0], nPoints[2][0][1]),
                                (255, 0, 255), 3, 8, 0, 0.05)
                x, y, w, h = obj[3]
                cv2.putText(imgContours2, '{}cm'.format(nW), (x + 30, y - 10), cv2.FONT_HERSHEY_COMPLEX_SMALL, 1,
                            (255, 0, 255), 2)
                cv2.putText(imgContours2, '{}cm'.format(nH), (x - 70, y + h // 2), cv2.FONT_HERSHEY_COMPLEX_SMALL, 1,
                            (255, 0, 255), 2)

        cv2.imshow('background', imgContours2)

    cv2.imshow('Original', img)
    cv2.waitKey(1)