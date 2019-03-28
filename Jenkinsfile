pipeline {
    agent any

    stages {
        stage('Prepare') {
            steps {
                sh 'git clone https://github.com/research-iobserve/iobserve-repository.git'
		sh 'PWD=`pwd` ; echo "api.baseline=$PWD/iobserve-repository/mvn-repo/" > gradle.properties'
		sh 'cat gradle.properties.template >> gradle.properties'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }
    }
}
