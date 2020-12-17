# Common Media Client Data

[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)

[Proposal](https://github.com/cta-wave/common-media-client-data)

CTA-5004
[Specification](https://github.com/cta-wave/common-media-client-data)

## Client SDK

The SDK exports a single CMCDManager class that is instantiated through a factory function.

### Installation


#### Android/Kotlin

Include Maven Repo

```kotlin/dsl

```

Import into project

```kotlin/dsl
implementation("tech.ctawave.cmcdlib:cmcd:0.0.1")
```

#### TypeScript/JavaScript

```shell
npm i --save "cmcdlib-cmcd"
```

### Usage

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

The library works by updating CMCDManager properties as stream state progresses.

When it's time to make a network request to your CDN, use the manager to append the CMCD query paramters to your URI by calling `appendQueryParamsToUrl` function providing the current url, the object request type, and whether or not this object is needed from startup.

```kotlin
val uriWithCMCDQueryParams = manager.appendQueryParamsToUrl(uri: string, objectType: string , startup: boolean)
```

```TypeScript
const uriWithCMCDQueryParmas = manager.appendQueryParamsToUrl(uri: string, objectType: string , startup: boolean)
```

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
