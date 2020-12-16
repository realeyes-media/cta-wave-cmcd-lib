# Common Media Client Data

[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)

[Proposal](https://github.com/cta-wave/common-media-client-data)

CTA-5004
[Specification](https://github.com/cta-wave/common-media-client-data)

## Client SDK

The SDK exports a single CMCDManager class that is instantiated through a factory function.

```kotlin
CMCDManagerFactory.createCMCDManager(config: CMCDConfig): CMCDManager
```

The library works by updating CMCDManager properties as stream state progresses. Each property is wrapped by a CMCDPayload data class.

### TypeScript Support

Until [KotlinJs IR Compiler](https://kotlinlang.org/docs/reference/js-ir-compiler.html) and [JsExport](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-js-export/) works for enum classes, we must maintain our own definitions.

Currently, can be found in hlsjsApp/src/types/cmcd.d.ts

Please follow guidelines described by TypeScript team:

[TypeScript Declaration Files](https://www.typescriptlang.org/docs/handbook/declaration-files/introduction.html)

Once library is open sourced and available through NPM provide definitions through [Definitely Typed](https://definitelytyped.org/)

## Hls.js Example

Example Hls.js implementation is found under `/hlsjsApp`. CMCD library is imported using npm linking. See `/package.json` link script.

THe CMCDManager implementation can be found in `<fill_me_in>`.

## Exoplayer Example

Example Exoplayer implementation is found under `/exoApp`. CMCD library is imported using Gradle's project implementation.

```kotlin
// cmcd library
implementation(project(":cmcd"))
```

The CMCDManager implementation can be found in `CMCDAppManager.kt`.

## Development Setup

After cloning the repository:

```bash
npm install
npm run link
```

This will install all dependencies and perform proper linking between the SDK and the Example Apps.
