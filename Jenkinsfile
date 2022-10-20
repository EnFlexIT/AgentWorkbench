pipeline {
  agent any
  stages {
    stage('Snapshot Build & Deploy for Java 11') {
      steps {
        echo 'Start Snapshot Build and Deployment of Agent.Workbench ...'
        sh 'mvn --version'
        sh 'mvn clean install -X -P p2Deploy -f eclipseProjects/de.enflexit.awb'
        echo 'Build & Deployment of Agent.Workbench Snapshot is done!'
      }
    }

    stage('Extract Products') {
      steps {
        echo 'Start extracting AWB Products ...'
        archiveArtifacts 'eclipseProjects/de.enflexit.awb/releng/de.enflexit.awb.product/target/products/de.enflexit.awb-*'
      }
    }

  }
}