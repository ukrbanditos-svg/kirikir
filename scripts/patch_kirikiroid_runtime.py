from pathlib import Path

# Patch Renovation KR2Activity.
p = Path("app/src/main/java/org/tvp/kirikiri2/KR2Activity.java")
s = p.read_text()
s = s.replace("System.exit(0);", "")
a = s.index("    static public String getDeviceId() {")
b = s.index("    static public KR2Activity sInstance;", a)
replacement = """    static public String getDeviceId() {
        try {
            String androidId = Secure.getString(GetInstance().getContentResolver(), Secure.ANDROID_ID);
            if (androidId != null && !androidId.isEmpty()) return "AndroidID:" + androidId;
        } catch (Throwable ignored) {}
        return "AndroidID:unknown";
    }

"""
s = s[:a] + replacement + s[b:]
p.write_text(s)

# Patch Cocos activity so it can run as an embedded secondary Activity and leave checkpoints.
p = Path("app/src/main/java/org/cocos2dx/lib/Cocos2dxActivity.java")
s = p.read_text()
if "import com.kirikir.player.runtime.RuntimeTrace;" not in s:
    s = s.replace(
        "package org.cocos2dx.lib;",
        "package org.cocos2dx.lib;\n\nimport com.kirikir.player.runtime.RuntimeTrace;"
    )

task_root = """        if (!isTaskRoot()) {
            // Android launched another instance of the root activity into an existing task
            //  so just quietly finish and go away, dropping the user back into the activity
            //  at the top of the stack (ie: the last state of this task)
            finish();
            Log.w(TAG, "[Workaround] Ignore the activity started from icon!");
            return;
        }

"""
s = s.replace(task_root, "")
s = s.replace(
    "        super.onCreate(savedInstanceState);",
    "        super.onCreate(savedInstanceState);\n"
    "        RuntimeTrace.mark(this, \"cocos-activity:onCreate\");",
    1,
)
s = s.replace(
    "        onLoadNativeLibraries();",
    "        RuntimeTrace.mark(this, \"cocos-activity:before-load-game\");\n"
    "        onLoadNativeLibraries();\n"
    "        RuntimeTrace.mark(this, \"cocos-activity:after-load-game\");",
    1,
)
s = s.replace(
    "        Cocos2dxHelper.init(this);",
    "        RuntimeTrace.mark(this, \"cocos-activity:before-helper-init\");\n"
    "        Cocos2dxHelper.init(this);\n"
    "        RuntimeTrace.mark(this, \"cocos-activity:after-helper-init\");",
    1,
)
s = s.replace(
    "        this.mGLContextAttrs = getGLContextAttrs();",
    "        RuntimeTrace.mark(this, \"cocos-activity:before-getGLContextAttrs\");\n"
    "        this.mGLContextAttrs = getGLContextAttrs();\n"
    "        RuntimeTrace.mark(this, \"cocos-activity:after-getGLContextAttrs\");",
    1,
)
s = s.replace(
    "        this.init();",
    "        RuntimeTrace.mark(this, \"cocos-activity:before-init-view\");\n"
    "        this.init();\n"
    "        RuntimeTrace.mark(this, \"cocos-activity:after-init-view\");",
    1,
)
p.write_text(s)

# Patch renderer around first native init/render.
p = Path("app/src/main/java/org/cocos2dx/lib/Cocos2dxRenderer.java")
s = p.read_text()
if "import com.kirikir.player.runtime.RuntimeTrace;" not in s:
    s = s.replace(
        "package org.cocos2dx.lib;",
        "package org.cocos2dx.lib;\n\nimport com.kirikir.player.runtime.RuntimeTrace;"
    )
if "private boolean mFirstRender" not in s:
    s = s.replace(
        "    private boolean mNativeInitCompleted = false;",
        "    private boolean mNativeInitCompleted = false;\n"
        "    private boolean mFirstRender = true;",
    )
s = s.replace(
    "        Cocos2dxRenderer.nativeInit(this.mScreenWidth, this.mScreenHeight);",
    "        RuntimeTrace.mark(Cocos2dxActivity.getContext(), \"renderer:before-nativeInit\");\n"
    "        Cocos2dxRenderer.nativeInit(this.mScreenWidth, this.mScreenHeight);\n"
    "        RuntimeTrace.mark(Cocos2dxActivity.getContext(), \"renderer:after-nativeInit\");",
    1,
)
needle = "            Cocos2dxRenderer.nativeRender();"
replacement = (
    "            if (mFirstRender) RuntimeTrace.mark(Cocos2dxActivity.getContext(), \"renderer:before-first-render\");\n"
    "            Cocos2dxRenderer.nativeRender();\n"
    "            if (mFirstRender) { RuntimeTrace.mark(Cocos2dxActivity.getContext(), \"renderer:after-first-render\"); mFirstRender = false; }"
)
s = s.replace(needle, replacement)
p.write_text(s)

# The Renovation libgame.so predates this newer Cocos2d JNI hook.
p = Path("app/src/main/java/org/cocos2dx/lib/Cocos2dxHelper.java")
s = p.read_text()
s = s.replace(
    "    private static native void nativeSetAudioDeviceInfo(boolean isSupportLowLatency, int deviceSampleRate, int audioBufferSizeInFames);",
    "    private static void nativeSetAudioDeviceInfo(boolean isSupportLowLatency, int deviceSampleRate, int audioBufferSizeInFames) { }",
)
p.write_text(s)
