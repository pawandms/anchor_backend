import os
import subprocess

IMAGE_DIR = "d:/Personal/AnchorApp_Workspace/backEnd/anchor_backend/mock_data/images"

def final_cleanup():
    print("Standardizing filenames...")
    files = os.listdir(IMAGE_DIR)
    for f in files:
        if f.endswith(".png.png"):
            new_name = f.replace(".png.png", ".png")
            os.rename(os.path.join(IMAGE_DIR, f), os.path.join(IMAGE_DIR, new_name))
            print(f"Fixed {f} -> {new_name}")
            
    # Verify female_profile_6.png
    fp6 = os.path.join(IMAGE_DIR, "female_profile_6.png")
    if os.path.exists(fp6):
        size = os.path.getsize(fp6)
        if size < 100: # 29 bytes is definitely an error page or empty
            print(f"female_profile_6.png is too small ({size} bytes), redownloading...")
            os.remove(fp6)
            url = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&q=80&w=1000" # Alternative reliable URL
            cmd = ["curl", "-k", "-L", "-o", fp6, url]
            subprocess.run(cmd)

if __name__ == "__main__":
    final_cleanup()
