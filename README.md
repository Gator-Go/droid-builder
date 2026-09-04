# Droid Builder

Prototype. Groovy factory that stamps Android apps from metadata and templates.

Android application factory with Groovy templates, metadata-driven generation,
and extenders. Core of the SW-Builder software factories for rapid Android
app development.

Catalog: https://sw-builder.com/appstore/builders/apps/droid-builder.html

## Layout

```text
droid-builder/
├── DroidBuilder.groovy
├── build/
│   ├── APP_CODES.xml
│   ├── APP_FUNCS.xml
│   └── APP_PROCS.xml
└── template/

## Build Scripts

Build scripts live in each generated app repo, not in this builder.
Example:
~/android/android-booklet-app/android-booklet-build.sh
That script pulls this builder, overlays template/ and build/ onto the app,
then runs DroidBuilder / BookletExtender.