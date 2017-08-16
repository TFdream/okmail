# OkMail
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0) [![Release Version](https://img.shields.io/badge/release-1.0.0-red.svg)](https://github.com/TiFG/okmail/releases) [![Build Status](https://travis-ci.org/TiFG/okmail.svg?branch=master)](https://travis-ci.org/TiFG/okmail)

## Overview
An Email client Java applications. For more information see the website and [the wiki](https://github.com/TiFG/okmail/wiki).

## Requirements
The minimum requirements to run the quick start are:
* JDK 1.7 or above
* A java-based project management software like [Maven](https://maven.apache.org/) or [Gradle](http://gradle.org/)

## Features
* Built on top of the [Java Mail](https://javaee.github.io/javamail/) API, aims to provide a simplify API for sending email.
* Supports text based emails/ HTML formatted emails/ text message with attachments .
* Supports CC/BCC
* Supports HTML Email Template(use Thymeleaf or Velocity as Template Engine)

## Dependency
Download the latest JAR or grab via Maven:
```
<dependency>
  <groupId>com.mindflow</groupId>
  <artifactId>okmail</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle:
```
compile 'com.mindflow:okmail:1.0.0'
```