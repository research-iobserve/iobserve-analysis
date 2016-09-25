# iObserve Monitoring and Analysis

This repository contains the sources for the iobserve analysis including
- org.iobserve.common for the monitoring records (event types)
- org.iobserve.monitoring for the monitoring probes
- org.iobserve.analysis for the analysis plugins on basis of teetime
- org.iobserve.releng for release engineering stuff of the project

The code is in a prototype stage and not fully functional, especially, as
some parts are still located in other SVN repositories.
# Setting up iObserve Maven Build

## Prerequisites 

- You need a local installation of Eclipse. We assume that the
  installation is in `${ECLIPSE}`.
- Your Eclipse Installation requires Palladio installed.

## Setup Build Environment

Normally maven can pull all archives from a remote repository. However,
PCM and Eclipse package might not be available from such repository.
Therefore, you must create a local maven backup repository. Fortunately,
we put an script in the root directory fo the project to create such
repository.

Before running the maven build you have to run

`./make-local-repo.sh ${ECLIPSE}`

located in the root directory of the project. The script creates the
`mvn-repo` directory containing the local maven repository of the
required Eclipse libraries, and a `settings.xml` file. The 
`settings.xml` file must be used to build the project. For Eclipse you
must also copy it to `${HOME}/.m2/settings.xml`. If you already have
such `settings.xml` you must merge both files.

## Compile the Project

Run maven in the release engineering directory called
`org.iobserve.releng`. In this directory execute maven as follows:

`mvn -s ../settings.xml compile`

You may clean the project with

`mvn -s ../settings.xml clean`



