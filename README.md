# Changelog Library

A lightweight Android library to manage changelogs on GitHub repositories.

## Features
- Upload new changelog entries to a GitHub repository.
- Fetch existing changelogs from a GitHub repository.
- Supports public and private repositories.

## Installation

Add the library to your `build.gradle`:

```gradle
repositories {
    maven {
        url "https://maven.pkg.github.com/YOUR_GITHUB_USERNAME/YOUR_REPOSITORY_NAME"
        credentials {
            username = "your-github-username"
            password = "your-github-token"
        }
    }
}

dependencies {
    implementation 'com.changelog.mdevz:changelog:1.0.0'
}
