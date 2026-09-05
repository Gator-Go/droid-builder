# Droid Builder

Prototype software factory that generates Android apps from a shared template,
metadata, and Groovy extenders.

Part of the SW-Builder factories for rapid Android app development.

Catalog: https://sw-builder.com/appstore/builders/apps/droid-builder.html

## What it does

`template/` is a base Android app framework.

`build/` holds reusable Java fragments (codes, functions, procedures)
declared in XML.

`options/` (per app) selects which fragments to apply.

Think of a 3D printer: small pieces of code are fused onto the template
to produce a complete app.

## Layout

```text
droid-builder/
├── DroidBuilder.groovy      # factory entry point
├── build/
│   ├── APP_CODES.xml
│   ├── APP_FUNCS.xml
│   └── APP_PROCS.xml
├── template/                # base Android project
└── options/                 # per-app metadata (not always in this repo)
```
## How a build runs
Build scripts live in each generated app repo, not in this builder.
Example:
```text
~/android/android-booklet-app/android-booklet-build.sh
```
## That script typically:

1. Pulls this builder

2. Overlays template/ and build/ onto the app

3. Runs DroidBuilder and an app-specific extender
(e.g. BookletExtender)
