# Kirikir

Universal visual novel player for Android.

## MVP goals

- Add a game from Android Storage Access Framework.
- Detect basic KiriKiri game layouts (XP3 archives).
- Keep file access behind a virtual file system abstraction.
- Prepare the architecture for local files, cache, and future HTTP Range streaming.
- Build a clean Android shell first, then add the runtime incrementally.

## Planned architecture

```
app/
  ui/            Android screens
  games/         library + scanner
  vfs/           virtual file system interfaces
  runtime/       engine/runtime integration
  compatibility/ per-game compatibility rules
```

## First milestone

Open in Android Studio, build an APK, add a folder, detect XP3 files, and show the game in the library.

> Runtime execution is intentionally not implemented in the first commit. The project starts with the game-library and VFS foundation so streaming support can be added without rewriting file access later.
