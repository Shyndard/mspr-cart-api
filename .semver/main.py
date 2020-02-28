#!/usr/bin/env python3
import os
import re
import sys
import semver
import subprocess

MAJOR_BUMP = [
    'breakingchange',
]

MINOR_BUMP = [
    'feature',
]

PATCH_BUMP = [
    'fix',
    'ci',
    'doc'
]

def git(*args):
    return subprocess.check_output(["git"] + list(args))


def tag_repo(tag):
    url = git("remote", "get-url", "--push", "origin").decode().replace('\n', '')
    regex = r'^http(s)?://(([a-zA-Z0-9-_]+\.)*[a-zA-Z0-9][a-zA-Z0-9-_]+\.[a-zA-Z]{2,11})/(.+)'
    push_url = re.sub(regex, r'git@\2:\4', url).replace('\n', '')
    git("remote", "set-url", "--push", "origin", push_url)
    git("tag", f'v{tag}')
    git("push", "origin", f'v{tag}')

def get_branches_on_last_commit():
    last_commit = git('log', '--format="%H"', '-n', '1').decode().replace('\"', '').strip()
    stdout_branches = git('branch', '--contains', last_commit).decode().strip().split('\n')
    with_master = [line.replace('origin/', '') for line in stdout_branches]
    return list(filter(lambda x : x != '* master', with_master))
    
def get_prefix(branches):
    res = []
    [res.append(branch.split('/')[0]) for branch in branches if branch.split('/')[0] not in res]
    return res


def bump(latest):
    if any(elem in get_prefix(get_branches_on_last_commit())  for elem in MAJOR_BUMP):
        return semver.bump_major(latest[:1])
    if any(elem in get_prefix(get_branches_on_last_commit())  for elem in MINOR_BUMP):
        return semver.bump_minor(latest[:1])
    if any(elem in get_prefix(get_branches_on_last_commit())  for elem in PATCH_BUMP):
        return semver.bump_patch(latest[:1])


def main():
    try:
        latest = git("describe", "--tags").decode().strip()
    except subprocess.CalledProcessError:
        # No tags in the repository
        version = "0.0.1"
    else:
        # Skip already tagged commits
        if '-' not in latest:
            print(latest)
            return 0

        version = bump(latest)

    tag_repo(version)
    print(version)

    return 0


if __name__ == "__main__":
    sys.exit(main())
