## Changelog Library

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![GitHub Release](https://img.shields.io/github/v/release/YOUR_GITHUB_USERNAME/changelog-lib)](https://github.com/YOUR_GITHUB_USERNAME/changelog-lib/releases)
[![GitHub Issues](https://img.shields.io/github/issues/YOUR_GITHUB_USERNAME/changelog-lib)](https://github.com/YOUR_GITHUB_USERNAME/changelog-lib/issues)

**Changelog Library** is a lightweight Android library designed to manage changelogs on GitHub repositories. It simplifies uploading new changelog entries and fetching existing ones, supporting both public and private repositories with secure GitHub authentication.

## Features

- **Upload Changelogs**: Add entries with version, date, message, and developer details to a JSON file in your repository.
- **Fetch Changelogs**: Retrieve all changelog entries for display in your app.
- **Secure Authentication**: Uses GitHub Personal Access Tokens for safe access to repositories.
- **Asynchronous Processing**: Handles network operations in the background to keep your UI responsive.
- **Minimal Dependencies**: Built with OkHttp and Gson for efficiency.

## Installation

1. **Add GitHub Packages Repository**  
   Add the following to your project-level `build.gradle` or `settings.gradle`:

   ```gradle
   dependencyResolutionManagement {
       repositories {
           maven {
               url "https://maven.pkg.github.com/YOUR_GITHUB_USERNAME/changelog-lib"
               credentials {
                   username = "your-github-username"
                   password = "your-github-token"
               }
           }
       }
   }

   
# Usage

 **Initialize the Client**
 
  Create an instance of GithubApiClient with your GitHub credentials:
  ```java
GithubApiClient client = new GithubApiClient(
    "your-github-token",
    "repository-owner",
    "repository-name",
    true, // isPrivate
    "changelog.json",
    "main"
);
