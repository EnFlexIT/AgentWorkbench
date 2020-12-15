pipeline {
  agent any
  stages {
    stage('Build') {
      tools {
        jdk 'jdk8'
      }
      environment {
        JAVA_HOME = '/usr/lib/jvm/java-1.8.0-openjdk-amd64'
      }
      steps {
        echo 'Start building Agent.Workbench ...'
        sh 'mvn --version'
        sh 'mvn clean install -P p2Deploy -f eclipseProjects/org.agentgui'
        echo 'Finalizing Agent.Workbench build ...'
      }
    }

  }
}