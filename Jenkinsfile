pipeline {
  agent any
  stages {
    stage('Clone Git') {
      steps {
        archiveArtifacts(artifacts: 'eclipseProjects/org.agentgui*', defaultExcludes: true)
      }
    }

  }
}