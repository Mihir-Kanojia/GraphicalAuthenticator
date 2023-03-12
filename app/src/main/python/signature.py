import cv2

#Works well with images of different dimensions
def orb_sim(img1, img2):
    # SIFT is no longer available in cv2 so using ORB
    orb = cv2.ORB_create()

    # detect keypoints and descriptors
    kp_a, desc_a = orb.detectAndCompute(img1, None)
    kp_b, desc_b = orb.detectAndCompute(img2, None)

    # define the bruteforce matcher object
    bf = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=True)

    #perform matches.
    matches = bf.match(desc_a, desc_b)
    #Look for similar regions with distance < 50. Goes from 0 to 100 so pick a number between.
    similar_regions = [i for i in matches if i.distance < 30]
    if len(matches) == 0:
        return 0
    return len(similar_regions) / len(matches)





def main(p1,p2):
    img1 = cv2.imread(p1, 0)
    img2 = cv2.imread(p2, 0)

    orb_similarity = orb_sim(img1, img2)  #1.0 means identical. Lower = not similar
    # print("Similarity using ORB is: ", orb_similarity)

    orb_similarity_percentage = "{:.2f}".format(orb_similarity*100)
    # print("Similarity using ORB in percentage is: ", orb_similarity_percentage)
    return orb_similarity_percentage

    #ssim = structural_similarity(img1, img2) #1.0 means identical. Lower = not similar
    #print("Similarity using SSIM is: ", ssim)

    #similarity_value = "{:.2f}".format(ssim*100)
    #print("Similarity using SSIM in percentage is: ", similarity_value)

    # Define a threshold for similarity score
    # threshold = 0.60

    # Check if the images are similar or different based on the threshold
    # if orb_similarity > threshold:
    #     print("The two signatures are similar")
    # else:
    #     print("The two signatures are different")