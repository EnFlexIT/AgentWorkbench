pipeline {
  agent any
  stages {
    stage('Build & Deploy Snapshot') {
      tools {
        jdk 'jdk8'
      }
      environment {
        JAVA_HOME = '/usr/lib/jvm/java-1.8.0-openjdk-amd64'
      }
      steps {
        echo 'Start build and snapshot deployment for Agent.Workbench ...'
        sh 'mvn --version'
        sh 'mvn clean install -e -P p2Deploy -f eclipseProjects/org.agentgui'
        echo 'Build & Deployment of Agent.Workbench done ...'
      }
    }

  }
}