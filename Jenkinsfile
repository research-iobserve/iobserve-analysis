#!groovy

node {
    stage ('Checkout') {
        checkout scm
    }

    stage ('1-compile logs') {
          sh 'gradle -S compileJava compileTestJava'
    }

    stage ('2-unit-test logs') {
            sh './gradlew -S test'
            junit '**/build/test-results/test/*.xml'
    }

    stage ('3-static-analysis logs') {
        sh  './gradlew -S check'
   } 
}
