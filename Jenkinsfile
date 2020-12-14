pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Start building Agent.Workbench ...'
        sh 'mvn clean install -e -f eclipseProjects/org.agentgui'
        echo 'Finalizing Agent.Workbench build ...'
      }
    }

  }
}