#!groovy

node {
  try {
    stage ('Checkout') {
        checkout scm
      sh 'java -version'
        sh '.jenkinsfile/add_jenkins_dependencies.sh'

    }

    stage ('1-compile logs') {
          sh './gradlew -S compileJava compileTestJava'
    }

    stage ('2-unit-test logs') {
            sh './gradlew -S test'
            junit '**/build/test-results/test/*.xml'
    }

    stage ('3-static-analysis logs') {
        sh  './gradlew -S check'
   } 
 }
 finally {
    deleteDir()
 }
}
