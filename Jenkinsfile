pipeline {
  agent any
  stages {
    stage('Clone Git') {
      steps {
        archiveArtifacts(artifacts: '*', defaultExcludes: true)
      }
    }

  }
}