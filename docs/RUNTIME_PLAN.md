# Runtime integration plan

## Goal
Run real KiriKiri games without coupling the Android UI to one engine implementation.

## Strategy
1. Keep Android Storage Access Framework at the app boundary.
2. Stage a selected game into an app-owned runtime directory for engines that require POSIX paths.
3. Expose RuntimeAdapter so engine implementations are replaceable.
4. First runtime target: a Kirikiroid2-compatible native core.
5. Later replace staged files with VFS-backed random access and HTTP Range streaming.

## Why staging first?
Existing native KiriKiri ports expect ordinary filesystem paths. SAF content URIs are not ordinary paths. Copying for the first runtime milestone gives us a debuggable path to native execution; streaming VFS comes after execution works.
