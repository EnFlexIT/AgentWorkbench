pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Start building Agent.WorkBench ...'
        sh 'mvn clean install -f /eclipseProjects'
        echo 'Finalizing Agent.WorkBench build ...'
      }
    }

  }
}