# !!! Quick reminder, due to optimization
- app/src/main/java/com/hasan/jetfasthub/screens/login
- app/src/main/java/com/hasan/jetfasthub/screens/main/home - This module is built with the same architecture used in [Tangem](https://github.com/tangem/tangem-app-android) open source
 
packages include real clean code base, other packages will also be optimized later ...


![jethub_compose](https://github.com/HasanAnorov/JetHub/assets/61424161/fc3eb6da-ff25-40a6-a8e9-93dccafaec43)

# JetHub

Another open-source GitHub client app but unlike any other app, JetHub was built from scratch and in Compose.

## Features  
- **App**
  - Three login types (Basic Auth), (Access Token) or via (OAuth)
  - Themes mode
  - Offline-mode
  - Markdown and code view
  - Notifications overview and "Mark all as read"
  - Search Users/Orgs, Repos, Issues/PRs & Code.

- **Repositories**
  - Edit, Create & Delete files (commit)
  - Search Repos
  - Browse and search Repos
  - See your public, private and forked Repos
  - Filter Branches and Commits
  - Watch, star and fork Repos
  - Download releases, files and branches
- **Issues and Pull Requests**
  - Search Issues/PRs
  - Filter Issues/PRs
  - Open/close Issues/PRs
  - Comment on Issues/PRs
  - Manage Issue/PR comments
  - Lock/unlock conversation in Issues/PRs
- **Commits and Gists**
  - Search Code/Gists
  - View Gists and their files
  - Comment on Commits/Gists
  - Manage Commit/Gist comments
  - Create/Delete Gists
  - Edit Gist & Gist Files
- **Organisations**
    - Overview
    - Feeds
    - Teams & Teams repos
    - Repos
- **Users**
  - Follow/Unfollow users
  - View user feeds
  - Contribution graph.
  - Search Users, Repos, Issues,Pull Requests and Code
- _**Much more...**_
  - JetHub is actively developed by myself. More features will come!

## Specs / Open-source libraries:

- Minimum **SDK 24**
- [**Jetpack Compose**](https://github.com/JetBrains/kotlin) Android’s recommended modern toolkit for building native UI.
- [**OAuth2**](https://developers.google.com/identity/protocols/oauth2/native-app) provides a single value, called an auth token, that represents both the user's identity and the application's authorization to act on the user's behalf.
- [**Paging3**](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db) Pagination from remote API & Local cache using paging3.
- [**Room**](https://developer.android.com/jetpack/androidx/releases/room) to enable online & offline support and for cache purposes.
- [**Koin**](https://insert-koin.io/docs/quickstart/android/) a pragmatic and lightweight dependency injection framework for Kotlin developers.
- [**Kotlin flows**](https://developer.android.com/kotlin/flow) & [**Kotlin coroutines**](https://kotlinlang.org/docs/coroutines-overview.html) for Retrofit & background threads
- [**MVVM**](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) an architectural pattern.
- [**Retrofit**](https://github.com/square/retrofit) for constructing the REST API
- [**Landscapist**](https://github.com/skydoves/landscapist) for loading images
- [**MarkdownView-Android**](https://github.com/mukeshsolanki/MarkdownView-Android) for _Markdown_ view
- Navigation Component, Material Design 3, Download Manager, and many Android support libraries :)

## Video Presentation Gif

https://github.com/HasanAnorov/JetHub/assets/61424161/95302a9b-7401-470f-a130-e71d83979908


[Or click to watch the quality video version here](https://drive.google.com/file/d/1553vHgXeId_iqja7Z1HslNSpnY3flV92/view?usp=drive_link)

## Screenshots

| OAuth login | OAuth login |OAuth login |
|:-:|:-:|:-:|
| ![OAuth login](https://github.com/ahi3646/JetHub/assets/143841121/9e0a06bd-dbef-428e-8ba7-68533097e27d) | ![OAuth login](https://github.com/ahi3646/JetHub/assets/143841121/08114b25-c6b2-465b-b4c9-5dbc922cfa79) | ![OAuth login](https://github.com/ahi3646/JetHub/assets/143841121/1b6c3b51-e00c-46dd-ba89-f38e3ba86a53) 


| Feeds | Drawer |
|:-:|:-:|
| ![First](https://github.com/ahi3646/JetHub/blob/main/app/src/main/res/drawable/first.png) | ![Sec](https://github.com/ahi3646/JetHub/blob/main/app/src/main/res/drawable/sec.png) |

| Repo | Profile |
|:-:|:-:|
| ![Third](https://github.com/ahi3646/JetHub/blob/main/app/src/main/res/drawable/third.png) | ![Fourth](https://github.com/ahi3646/JetHub/blob/main/app/src/main/res/drawable/fourth.png) |
