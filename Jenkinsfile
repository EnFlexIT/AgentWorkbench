pipeline {
  agent any
  stages {
    stage('Snapshot Build & Deploy for Java 21') {
      steps {
        echo 'Start Snapshot Build and Deployment of Agent.Workbench ...'
        sh 'mvn --version'
        sh 'mvn clean install -P p2Deploy -f eclipseProjects/de.enflexit.awb -Dtycho.localArtifacts=ignore -Dtycho.p2.transport.min-cache-minutes=0'
        echo 'Build & Deployment of Agent.Workbench Snapshot is done!'
      }
    }

    stage('Extract Products') {
      steps {
        echo 'Start extracting AWB Products ...'
        archiveArtifacts 'eclipseProjects/de.enflexit.awb/releng/de.enflexit.awb.product/target/products/de.enflexit.awb-*'
        archiveArtifacts 'eclipseProjects/de.enflexit.awb/releng/de.enflexit.awb.ws.product/target/products/de.enflexit.awb.ws-*'
      }
    }

  }
}