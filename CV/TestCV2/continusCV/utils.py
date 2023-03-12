import cv2
import numpy as np


def getContours(img, cThr=[100, 100], showCanny=False, minArea=1000, filter=0, draw=False):
    imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 1)
    imgCanny = cv2.Canny(imgBlur, cThr[0], cThr[1])
    kernel = np.ones((5, 5))
    imgDial = cv2.dilate(imgCanny, kernel, iterations=3)
    imgThre = cv2.erode(imgDial, kernel, iterations=2)
    if showCanny: cv2.imshow('Canny', imgThre)

    # 寻找所有的外轮廓
    contours, hiearchy = cv2.findContours(imgThre, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    finalCountours = []
    # 遍历找到的轮廓
    for i in contours:
        area = cv2.contourArea(i)  # 轮廓的面积
        if area > minArea:  # 如果大于设置的最小轮廓值，就往下走
            peri = cv2.arcLength(i, True)  # 封闭的轮廓的长度
            approx = cv2.approxPolyDP(i, 0.02 * peri, True)  # 封闭轮廓的拐点
            bbox = cv2.boundingRect(approx)  # 找到边界框
            if filter > 0:  # 需不需要根据拐点个数进行过滤轮廓
                if len(approx) == filter:  # 拐点个数，面积，拐点位置，边界框，轮廓
                    finalCountours.append([len(approx), area, approx, bbox, i])
            else:
                finalCountours.append([len(approx), area, approx, bbox, i])
    finalCountours = sorted(finalCountours, key=lambda x: x[1], reverse=True)  # 根据轮廓大小进行从大到小的排序
    if draw:  # 是否要画出来轮廓
        for con in finalCountours:
            cv2.drawContours(img, con[4], -1, (0, 0, 255), 3)
    return img, finalCountours


# 四个点是随机的，于是重新排序
def reorder(myPoints):
    myPointsNew = np.zeros_like(myPoints)
    myPoints = myPoints.reshape((4, 2))
    add = myPoints.sum(1)
    myPointsNew[0] = myPoints[np.argmin(add)]
    myPointsNew[3] = myPoints[np.argmax(add)]
    diff = np.diff(myPoints, axis=1)
    myPointsNew[1] = myPoints[np.argmin(diff)]
    myPointsNew[2] = myPoints[np.argmax(diff)]

    return myPointsNew


def warpImg(img, points, w, h, pad=20):
    # print(points)
    points = reorder(points)
    pts1 = np.float32(points)
    pts2 = np.float32([[0, 0], [w, 0], [0, h], [w, h]])
    matrix = cv2.getPerspectiveTransform(pts1, pts2)
    imgWrap = cv2.warpPerspective(img, matrix, (w, h))
    imgWrap = imgWrap[pad:imgWrap.shape[0] - pad, pad:imgWrap.shape[1] - pad]

    return imgWrap


def findDis(pts1, pts2):
    return ((pts2[0] - pts1[0]) ** 2 + (pts2[1] - pts1[1]) ** 2) ** 0.5