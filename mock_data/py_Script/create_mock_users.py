import os
import json
import time
import base64
import subprocess

BASE_URL = "http://localhost:8080"
SIGNUP_URL = f"{BASE_URL}/api/v1/public/signup"
TOKEN_URL = f"{BASE_URL}/oauth2/token"
UPLOAD_BASE_URL = f"{BASE_URL}/api/v1/users/{{userId}}/media/profile-image"

CLIENT_ID = "General"
CLIENT_SECRET = "General"
USER_PASSWORD = "Mumbai@123"

IMAGE_DIR = "d:/Personal/AnchorApp_Workspace/backEnd/anchor_backend/mock_data/images"

UNSPLASH_URLS = [
    "https://images.unsplash.com/photo-1544168190-79c17527004f?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1509062522246-3755977927d7?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1523240715639-8c9e46a7888c?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1515187029135-18ee286d815b?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1529333166437-7750a6dd5a70?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?auto=format&fit=crop&q=80&w=1000",
    "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&q=80&w=1000"
]

def download_images():
    print("Downloading additional female images...")
    for i, url in enumerate(UNSPLASH_URLS, start=4):
        path = os.path.join(IMAGE_DIR, f"female_profile_{i}.png")
        if not os.path.exists(path):
            print(f"Downloading {url} to {path}...")
            # Use curl for download
            cmd = ["curl", "-L", "-o", path, url]
            subprocess.run(cmd)
        else:
            print(f"female_profile_{i}.png already exists.")

def get_auth_header_val():
    auth_str = f"{CLIENT_ID}:{CLIENT_SECRET}"
    encoded_auth = base64.b64encode(auth_str.encode()).decode()
    return f"Basic {encoded_auth}"

def create_user(first_name, last_name, email, gender):
    signup_data = {
        "firstName": first_name,
        "lastName": last_name,
        "email": email,
        "password": USER_PASSWORD,
        "userName": email,
        "gender": gender,
        "profileType": "Public"
    }
    
    print(f"Signing up user: {email}")
    cmd = [
        "curl", "-X", "POST", SIGNUP_URL,
        "-H", "Content-Type: application/json",
        "-d", json.dumps(signup_data)
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode == 0:
        try:
            resp = json.loads(result.stdout)
            return resp.get("userId")
        except:
            print(f"Failed to parse signup response: {result.stdout}")
            return None
    else:
        print(f"Signup failed for {email}: {result.stderr}")
        return None

def get_token(email):
    print(f"Requesting token for user: {email}")
    cmd = [
        "curl", "-X", "POST", TOKEN_URL,
        "-H", f"Authorization: {get_auth_header_val()}",
        "-d", "grant_type=password",
        "-d", f"username={email}",
        "-d", f"password={USER_PASSWORD}",
        "-d", "scope=openid"
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode == 0:
        try:
            resp = json.loads(result.stdout)
            return resp.get("access_token")
        except:
            print(f"Failed to parse token response: {result.stdout}")
            return None
    else:
        print(f"Token request failed for {email}: {result.stderr}")
        return None

def upload_image(userId, token, image_path):
    upload_url = UPLOAD_BASE_URL.format(userId=userId)
    print(f"Uploading image for user {userId}: {image_path}")
    cmd = [
        "curl", "-X", "POST", upload_url,
        "-H", f"Authorization: Bearer {token}",
        "-F", f"file=@{image_path}"
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode == 0:
        print(f"Upload successful for user {userId}")
        return True
    else:
        print(f"Upload failed for user {userId}: {result.stderr}")
        return False

def main():
    if not os.path.exists(IMAGE_DIR):
        os.makedirs(IMAGE_DIR)
        
    download_images()
    
    male_names = [
        ("Liam", "Smith"), ("Noah", "Jones"), ("Oliver", "Brown"), ("James", "Taylor"),
        ("Elijah", "Davies"), ("William", "Wilson"), ("Henry", "Evans"), ("Lucas", "Thomas"),
        ("Benjamin", "Roberts"), ("Theodore", "Walker")
    ]
    
    female_names = [
        ("Emma", "Moore"), ("Olivia", "White"), ("Ava", "Harris"), ("Isabella", "Clark"),
        ("Sophia", "Lewis"), ("Charlotte", "Young"), ("Mia", "King"), ("Amelia", "Wright"),
        ("Harper", "Hill"), ("Evelyn", "Scott")
    ]
    
    users_data = []
    for i, (fn, ln) in enumerate(male_names, start=1):
        users_data.append({"fn": fn, "ln": ln, "email": f"maleuser{i}@example.com", "gender": "Male", "image": f"male_profile_{i}.png"})
        
    for i, (fn, ln) in enumerate(female_names, start=1):
        users_data.append({"fn": fn, "ln": ln, "email": f"femaleuser{i}@example.com", "gender": "Female", "image": f"female_profile_{i}.png"})

    for user in users_data:
        userId = create_user(user["fn"], user["ln"], user["email"], user["gender"])
        if userId:
            token = get_token(user["email"])
            if token:
                image_path = os.path.join(IMAGE_DIR, user["image"])
                if os.path.exists(image_path):
                    upload_image(userId, token, image_path)
                else:
                    print(f"Image not found: {image_path}")
            else:
                print(f"Skipping upload for {user['email']} due to missing token")
        time.sleep(0.5)

if __name__ == "__main__":
    main()
