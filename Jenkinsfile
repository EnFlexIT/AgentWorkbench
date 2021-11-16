pipeline {
  agent any
  stages {
    stage('Snapshot Build & Deploy for Java 11') {
      steps {
        echo 'Start Snapshot Build and Deployment of Agent.Workbench ...'
        sh 'mvn --version'
        sh 'mvn clean install -P p2DeployClean -f eclipseProjects/org.agentgui'
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