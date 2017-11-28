#!groovy

node {
   stage ('Checkout') {
      steps{
        checkout scm
      }
    }

    stage ('1-compile logs') {
        steps{
          sh './gradlew -S compileJava compileTestJava'
        }
    }

    stage ('2-unit-test logs') {
        steps{
            sh './gradlew -S test'
            junit '**/build/test-results/test/*.xml'
        }
    }

    stage ('3-static-analysis logs') {
      steps{  
        sh  './gradlew -S check'
      }
   } 
}
