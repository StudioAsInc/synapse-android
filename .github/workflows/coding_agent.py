import os
import requests
from github import Github
from git import Repo

# ==============================
# Load environment variables
# ==============================
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
PAT = os.getenv("PAT")  # Secret name is PAT
GITHUB_USERNAME = os.getenv("GITHUB_USERNAME")
GITHUB_REPO = os.getenv("GITHUB_REPO")

# Check PAT
if not PAT:
    raise ValueError("PAT is not set! Make sure 'PAT' secret exists in GitHub Actions.")

# ==============================
# GitHub setup
# ==============================
g = Github(PAT)
repo = g.get_user(GITHUB_USERNAME).get_repo(GITHUB_REPO)

LOCAL_REPO_PATH = f"/github/workspace/{GITHUB_REPO}"  # GitHub Actions context path

# ==============================
# Clone or pull repo
# ==============================
if not os.path.exists(LOCAL_REPO_PATH):
    print("[*] Cloning repo...")
    Repo.clone_from(
        repo.clone_url.replace("https://", f"https://{PAT}@"), 
        LOCAL_REPO_PATH
    )
else:
    print("[*] Pulling latest changes...")
    local_repo = Repo(LOCAL_REPO_PATH)
    local_repo.remotes.origin.pull()

# ==============================
# Fetch open issues
# ==============================
def get_open_issues():
    issues = repo.get_issues(state="open")
    return [issue for issue in issues if issue.pull_request is None]

# ==============================
# Fix issue via Gemini
# ==============================
def fix_issue(issue):
    prompt = f"""
Fix the following feature/bug in Synapse Android:
Title: {issue.title}
Description: {issue.body}

Provide ready-to-commit Android/Java/Kotlin code only.
"""
    headers = {
        "Authorization": f"Bearer {GEMINI_API_KEY}",
        "Content-Type": "application/json"
    }
    data = {
        "model": "gemini-2.5-pro",
        "prompt": prompt,
        "max_output_tokens": 1500
    }

    try:
        response = requests.post("https://api.gemini.ai/v1/complete", headers=headers, json=data)
        response.raise_for_status()
        code = response.json().get("completion", {}).get("content", "// No code received")
    except Exception as e:
        print(f"[!] Gemini API error: {e}")
        code = "// Failed to get code from Gemini"

    # Write code to file
    file_path = f"{LOCAL_REPO_PATH}/auto_fix.java"
    with open(file_path, "w") as f:
        f.write(code)
    print(f"[*] Code written to {file_path}")

    # Commit & push
    try:
        repo_local = Repo(LOCAL_REPO_PATH)
        repo_local.git.add(all=True)
        repo_local.index.commit(f"Auto fix: {issue.title}")
        repo_local.remotes.origin.push()
        print(f"[*] Issue '{issue.title}' fixed and pushed!")
    except Exception as e:
        print(f"[!] Git push error: {e}")

# ==============================
# Main
# ==============================
issues = get_open_issues()
if not issues:
    print("[*] No open issues found!")
else:
    for issue in issues:
        fix_issue(issue)