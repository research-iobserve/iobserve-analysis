pipeline {
    agent any

    stages {
        stage('Prepare') {
            steps {
//                deleteDir()
//		sh 'git clone https://github.com/research-iobserve/iobserve-analysis.git'
		sh 'rm -rf iobserve-repository'
                sh 'git clone https://github.com/research-iobserve/iobserve-repository.git'
//		sh 'cd iobserve-analysis'
		sh 'PWD=`pwd` ; echo "api.baseline=$PWD/iobserve-repository/mvn-repo/" > gradle.properties'
		sh 'cat gradle.properties.template >> gradle.properties'
            }
        }
        stage('Build') {
            steps {
		sh './gradlew clean'
                sh './gradlew build --refresh-dependencies'
            }
        }
	stage('Static Analysis') {
          steps {
            sh './gradlew check'
          }
          post {
            always {
              // Report results of static analysis tools
              recordIssues(
                enabledForFailure: true,
                tools: [
                  java(),
                  javaDoc(),
                  checkStyle(
                    pattern: '**/build/reports/checkstyle/*.xml'
                  ),
                  pmdParser(
                    pattern: '**/build/reports/pmd/*.xml'
                  ),
                  //spotBugs(
                  //  pattern: '**/build/reports/spotbugs/*.xml'
                  //)
                ]
              )
            }
          }
        }
    }
}
