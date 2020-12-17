pipeline {
  agent any
  stages {
    stage('Snapshot Build & Deploy') {
      tools {
        jdk 'jdk8'
      }
      environment {
        JAVA_HOME = '/usr/lib/jvm/java-1.8.0-openjdk-amd64'
      }
      steps {
        echo 'Start Snapshot Build and Deployment of Agent.Workbench ...'
        sh 'mvn --version'
        sh 'mvn clean install -P p2Deploy -f eclipseProjects/org.agentgui'
        echo 'Build & Deployment of Agent.Workbench Snapshot is done!'
      }
    }

    stage('Extract Products') {
      steps {
        echo 'Start extracting AWB Products ...'
        archiveArtifacts 'eclipseProjects/org.agentgui/releng/org.agentgui.product/target/products/org.agentgui-*'
      }
    }

  }
}