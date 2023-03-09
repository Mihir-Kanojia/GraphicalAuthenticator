# import numpy
import cv2
from skimage.metrics import structural_similarity as ssim

def match(path1, path2):
    # read the images
    img1 = cv2.imread(str(path1))
    img2 = cv2.imread(str(path2))
    # turn images to grayscale
    img1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    img2 = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)
    # resize images for comparison
    img1 = cv2.resize(img1, (300, 300))
    img2 = cv2.resize(img2, (300, 300))

    similarity_value = "{:.2f}".format(ssim(img1, img2)*100)

    return float(similarity_value)

def main(p1,p2):
    simVal = match(p1,p2)
    return "Sim val is " + str(simVal)

# def main(number1, number2):
#     num1 = int(number1)
#     num2 = int(number2)
#     sum = num1 + num2
#     return "sum is " + str(sum)
