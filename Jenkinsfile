#!groovy

pipeline {

  try {    
    stage ('Checkout') {
        checkout scm
    }

    stage ('1-compile logs') {
        sh ./gradlew -S compileJava compileTestJava
    }

    stage ('2-unit-test logs') {
        sh ./gradlew -S test
        junit '**/build/test-results/test/*.xml'
    }

    stage ('3-static-analysis logs') {
        sh  ./gradlew -S check   
    }

    stage ('4-release-check-short logs') {

        checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**\\build\\reports\\checkstyle\\*.xml', unHealthy: ''

    }
  }
  finally {
    deleteDir()
  }
}
