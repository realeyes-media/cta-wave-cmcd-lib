# Common Media Client Data

![Gradle Unit Tests](https://github.com/realeyes-media/cmcd/workflows/Gradle%20Unit%20Tests/badge.svg)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](https://commitizen.github.io/cz-cli/)

[Proposal](https://github.com/cta-wave/common-media-client-data)

CTA-5004
[Specification](https://docs.google.com/document/d/1S_Dj_aHVnbWnjeJYMfLU1D6tZoqYdgHT1stfznBvxig/edit)

## Client SDK

The SDK exports a single CMCDManager class that is instantiated through a factory function.

### Installation

#### Android/Kotlin

Import into project

```kotlin/dsl
implementation("tech.ctawave.cmcdlib:cmcd:0.0.1")
```

#### TypeScript/JavaScript

```shell
npm i --save "cmcdlib-cmcd"
```

### Usage

The library works by updating CMCDManager properties as stream state progresses.

```kotlin
import tech.ctawave.cmcd

val manager = CMCDManagerFactory.createCMCDManager(config: CMCDConfig): CMCDManager
manager.encodedBitrate = 123456
```

```TypeScript
import cmcdlib from "cmcdlib-cmcd"

const manager = new cmcdlib.tech.ctawave.cmcd.CMCDManagerFactory.createCMCDManager(config: CMCDConfig): CMCDManager
manager.encodedBitrate = 123456
```

When it's time to make a network request to your CDN, use the manager to append the CMCD query paramters to your URI by calling `appendQueryParamsToUri` function providing the current uri, the object request type, and whether or not this object is needed from startup.

```kotlin
val uriWithCMCDQueryParams = manager.appendQueryParamsToUri(uri: string, objectType: string , startup: boolean)
```

```TypeScript
const uriWithCMCDQueryParmas = manager.appendQueryParamsToUri(uri: string, objectType: string , startup: boolean)
```

### TypeScript Support

Until [KotlinJs IR Compiler](https://kotlinlang.org/docs/reference/js-ir-compiler.html) and [JsExport](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-js-export/) works for enum classes, we must maintain our own definitions.

Currently, can be found in hlsjsApp/src/types/cmcd.d.ts

Please follow guidelines described by TypeScript team:

[TypeScript Declaration Files](https://www.typescriptlang.org/docs/handbook/declaration-files/introduction.html)

Once library is open sourced and available through NPM provide definitions through [Definitely Typed](https://definitelytyped.org/)

## Hls.js Example

Example Hls.js implementation is found under `/hlsjsApp`. CMCD library is imported using npm linking. See `/package.json` link script.

THe CMCDManager implementation can be found in `CMCDAppManager.ts`.

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

### TODO

1. Once approved, migrate repo and open source.
2. Update MAVEN and NPM package details within all gradle and package.json files.
3. Provide TypeScript definition's through [Definitely Typed](https://definitelytyped.org/).
4. Update cmcd library to use ENUMS. Current limitation of Kotlin/Js.
5. Update cmcd library JS compilation to use new IR compiler.
6. Maybe namespace js lib? "@ctawave/cmcd".
