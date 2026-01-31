import os
import subprocess

IMAGE_DIR = "d:/Personal/AnchorApp_Workspace/backEnd/anchor_backend/mock_data/images"

# Rename existing files with timestamps
def rename_files():
    print("Renaming files...")
    files = os.listdir(IMAGE_DIR)
    for f in files:
        if f.startswith("male_profile_"):
            # format: male_profile_1_123456789.png
            parts = f.split("_")
            if len(parts) >= 3:
                new_name = f"{parts[0]}_{parts[1]}_{parts[2]}.png" # male_profile_1.png? No
                # Wait, split by _: ['male', 'profile', '1', 'timestamp.png']
                new_name = f"male_profile_{parts[2]}.png"
                os.rename(os.path.join(IMAGE_DIR, f), os.path.join(IMAGE_DIR, new_name))
                print(f"Renamed {f} to {new_name}")
        elif f.startswith("female_profile_"):
            parts = f.split("_")
            if len(parts) >= 3:
                new_name = f"female_profile_{parts[2]}.png"
                os.rename(os.path.join(IMAGE_DIR, f), os.path.join(IMAGE_DIR, new_name))
                print(f"Renamed {f} to {new_name}")

UNSPLASH_URLS = [
    "https://images.unsplash.com/photo-1544168190-79c17527004f?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1509062522246-3755977927d7?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1523240715639-8c9e46a7888c?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1515187029135-18ee286d815b?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1529333166437-7750a6dd5a70?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&q=80&w=1000"
]

def download_missing():
    print("Downloading missing images...")
    for i, url in enumerate(UNSPLASH_URLS, start=4):
        path = os.path.join(IMAGE_DIR, f"female_profile_{i}.png")
        if not os.path.exists(path):
            print(f"Downloading {url} to {path}")
            cmd = ["curl", "-k", "-L", "-o", path, url]
            subprocess.run(cmd)

if __name__ == "__main__":
    rename_files()
    download_missing()
