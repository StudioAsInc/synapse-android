import os
from PIL import Image
import imagehash

def find_duplicate_images(path):
    hashes = {}
    duplicates = []
    for dirpath, _, filenames in os.walk(path):
        for filename in filenames:
            if filename.lower().endswith(('.png', '.jpg', '.jpeg')):
                filepath = os.path.join(dirpath, filename)
                try:
                    with Image.open(filepath) as img:
                        hash_value = imagehash.phash(img)
                        if hash_value in hashes:
                            hashes[hash_value].append(filepath)
                        else:
                            hashes[hash_value] = [filepath]
                except Exception as e:
                    print(f"Could not process {filepath}: {e}")

    for hash_value in hashes:
        if len(hashes[hash_value]) > 1:
            duplicates.append(hashes[hash_value])

    return duplicates

if __name__ == "__main__":
    duplicates = find_duplicate_images("app/src/main/res")
    if duplicates:
        print("Found duplicate images:")
        for group in duplicates:
            print("---")
            for filepath in group:
                print(filepath)
    else:
        print("No duplicate images found.")
