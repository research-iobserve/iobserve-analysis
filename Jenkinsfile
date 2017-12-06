#!groovy

node {
  try {
    stage ('Checkout') {
      sh 'rm -rf *'
      checkout scm
      sh 'java -version'
      sh '.jenkinsfile/add_jenkins_dependencies.sh'
      sh 'ls -al'

    }

    stage ('1-compile logs') {
          sh './gradlew --refresh-dependencies'
          sh './gradlew build'
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
