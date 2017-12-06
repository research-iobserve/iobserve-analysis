#!groovy

node {
  try {
    stage ('Checkout') {
        checkout scm
        sh 'git clone https://github.com/research-iobserve/iobserve-repository.git'
        sh 'path="$(pwd -P )"'
        sh 'path=$path"/iobserve-repository/mvn-repo/"'
        sh 'echo "Repo at "$path'
        sh 'cd $old_path'
        sh 'file_entry="api.baseline="$path'
        sh 'echo $file_entry >"gradle.properties"'
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
