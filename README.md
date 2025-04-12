# Changelog Library

A lightweight Android library to manage changelogs on GitHub repositories.

## Features
- Upload new changelog entries to a GitHub repository.
- Fetch existing changelogs from a GitHub repository.


## Installation

Add the library to your `build.gradle`:

```gradle

dependencies {
    implementation 'com.changelog.mdevz:changelog:1.0.0'
}

##Usage
#Initialize the Client
GithubApiClient client = new GithubApiClient(
    "your-github-token",
    "repository-owner",
    "repository-name",
    true, // isPrivate
    "changelog.json",
    "main"
);
