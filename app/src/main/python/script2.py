import cv2
import numpy as np

def main(p1,p2):
    # Load the two images
    img1 = cv2.imread(p1)
    img2 = cv2.imread(p2)

    # Convert the images to grayscale
    gray1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    gray2 = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)

    # Create ORB object
    orb = cv2.ORB_create()

    # Detect and compute keypoints and descriptors for both images
    kp1, des1 = orb.detectAndCompute(gray1, None)
    kp2, des2 = orb.detectAndCompute(gray2, None)

    # Create Brute-Force Matcher object
    bf = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=True)

    # Match the descriptors
    matches = bf.match(des1, des2)

    # Calculate the distances between the matched descriptors
    distances = [match.distance for match in matches]

    # Calculate the similarity score for each match
    max_distance = max(distances)
    similarity_scores = [100 - (distance * 100 / max_distance) for distance in distances]

    # Normalize the similarity scores to a range from 0 to 100
    similarity_scores = np.interp(similarity_scores, (min(similarity_scores), max(similarity_scores)), (0, 100))

    # Calculate the average similarity score
    average_score = sum(similarity_scores) / len(similarity_scores)

    print("average_score: ",average_score)
    # Print the similarity percentage as the average similarity score
    print("The similarity percentage is: {:.2f}%".format(average_score))
    return average_score
    # Define a threshold for similarity score
    threshold = 60

    # Check if the images are similar or different based on the threshold
    if average_score > threshold:
        print("The two signatures are similar")
    else:
        print("The two signatures are different")
