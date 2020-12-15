pipeline {
  agent any
  stages {
    stage('Build') {
      tools {
        jdk 'JDK-1.8'
      }
      steps {
        echo 'Start building Agent.Workbench ...'
        sh 'java -version'
        sh 'mvn clean install -e -f eclipseProjects/org.agentgui'
        echo 'Finalizing Agent.Workbench build ...'
      }
    }

  }
}