pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Start building Agent.Workbench ...'
        sh 'mvn clean install -f eclipseProjects/'
        echo 'Finalizing Agent.Workbench build ...'
      }
    }

  }
}