import cv2
import numpy as np

def main(p1,p2):
    # Load the images
    img1 = cv2.imread(p1, cv2.IMREAD_GRAYSCALE)
    img2 = cv2.imread(p2, cv2.IMREAD_GRAYSCALE)

    # Create an ORB object
    orb = cv2.ORB_create()

    # Find the keypoints and descriptors
    kp1, des1 = orb.detectAndCompute(img1, None)
    kp2, des2 = orb.detectAndCompute(img2, None)

    # Create a Brute-Force Matcher object
    bf = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=True)

    # Match the descriptors
    matches = bf.match(des1, des2)

    # Sort the matches by distance
    matches = sorted(matches, key=lambda x: x.distance)

    # Calculate the similarity score
    similarity = len(matches) / len(kp1) * 100
    if similarity > 25:
        print("Images are similar ({}% similarity)".format(similarity))
    else:
        print("Images are not similar ({}% similarity)".format(similarity))

    return similarity