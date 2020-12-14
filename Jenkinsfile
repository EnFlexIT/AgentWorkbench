pipeline {
  agent any
  stages {
    stage('Clone Git') {
      steps {
        archiveArtifacts(artifacts: 'org.agentgui*', defaultExcludes: true)
      }
    }

  }
}