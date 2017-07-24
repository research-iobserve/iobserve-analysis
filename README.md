# iObserve Monitoring and Analysis

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

After compilation, you may find in `analysis.cli/build/distributions/`
Archives containing executables.
- `analysis.cli-0.0.2-SNAPSHOT.tar`
- `analysis.cli-0.0.2-SNAPSHOT.zip`
Choose your preferred kind of archive and extract it in our analysis
directory (preferably outside of the directory of the git repository).
After extraction you can start the analysis with:
- `analysis.cli-0.0.2-SNAPSHOT/bin/analysis.cli`  (linux, mac, etc.)
- `analysis.cli-0.0.2-SNAPSHOT/bin/analysis.cli.bat` (windows)

usage: iobserve-analysis
 -h,--help                            show usage information
 -i,--input <arg>                     a Kieker logfile directory
 -p,--pcm <arg>                       directory containing all PCM models
 -t,--think-time <arg>                Variance of user groups for the
                                      clustering
 -V,--variance-of-user-groups <arg>   Variance of user groups for the
                                      clustering
 -w,--closed-workload                 Closed workload

For a minimal analysis, type:
`analysis.cli-0.0.2-SNAPSHOT/bin/analysis.cli -i example-kieker-data -p palladio-directory -t 1 -V 2 -w`








