# iObserve Monitoring and Analysis

<a href="https://travis-ci.org/research-iobserve/iobserve-analysis"><img src="https://travis-ci.org/research-iobserve/iobserve-analysis.svg?branch=master" alt="Build Status"></a>

This repository contains the sources for the iobserve analysis including
- common for the monitoring records (event types)
- monitoring for the monitoring probes
- analysis for the analysis plugins on basis of teetime
- analysis.cli command line version of the analysis
- analysis.service the service version of the analysis
- splitter separates monitoring data streams by host
- rac.creator this tool allows to create a RAC mapping based on
  monitoring data, a PCM model and a supplementary mapping file.
- test.setup test tooling

The code is in a prototype stage and not fully functional, especially, as
some parts are still located in other SVN repositories.

# Setting up iObserve gradle build

## Prerequisites 

- We use Java 8. you need an Java 8 JDK installed on your machine.
- You need additional packages which are compiled in iobserve-repository.
  Checkout the `iobserve-repository` git repository alongside 
  `iobserve-analysis`
  `git clone git@github.com:research-iobserve/iobserve-repository.git`
- You need Kieker example data, a RAC and a corresponding palladio model

'Note: This is outdated, we switched to gradle'

## Setup Build Environment

In the `iobserve-analysis` directory, create a file `gradle.properties`.
Add the following variable declaration to that file and save it.

`api.baseline=PATH_TO_IOBSERVE_REPOSITORY/mvn-repo/`

Substitute PATH_TO_IOBSERVE_REPOSITORY with the actual absolute path on
your computer to the `iobserve-repository` directory.

## Compile

Depending on your local setup you may use:
- `./gradlew build`  (linux, mac, etc.)
- `gradlew.bat build` (windows)
- `gradle build` (in case you have gradle 3.2.1 installed on your system)

## Execution

After compilation, you may find in various folders packages containing executables, including the `analysis`, the `replayer`, the session `reconstructor`, etc. The process to use them is quite similar. In the following we introduce the most common tools.

Alongside all executables we have example configurations placed in the respective sub project folder with the suffix `config`.
All executables support an environment variable `project_OPTS` where `project` must be substituted by the executable name. for `analysis` it is `ANALYSIS_OPTS`. Through this mechanisms further parameter can be specified which is quite helpful.

To activate logging use: `export ANALYSIS_OPTS="-Dlog4j.configuration=file:///full/qualified/pathname/log4j.cfg"`
Please note that you have to adjust path and file name to your setup.

### Analysis

The `analysis` is located in `analysis/build/distributions/`
You may find two archives containing executables.
- `analysis-0.0.3-SNAPSHOT.tar`
- `analysis-0.0.3-SNAPSHOT.zip`
Choose your preferred kind of archive and extract it in our analysis
directory (preferably outside of the directory of the git repository).
After extraction you can start the analysis with:
- `analysis.cli-0.0.3-SNAPSHOT/bin/analysis`  (linux, mac, etc.)
- `analysis.cli-0.0.3-SNAPSHOT/bin/analysis.bat` (windows)

usage: iobserve-analysis
 -h,--help                            show usage information
 -c <configuration file>              configuration file

For a minimal analysis, type:
`analysis-0.0.3-SNAPSHOT/bin/analysis -c analysis.config`

### Collector

Usage: <main class> [options]
  Options:
  * -c, --configuration   Configuration file.

### Log-Replayer

### Session Reconstructor

### Service Privacy Violation






