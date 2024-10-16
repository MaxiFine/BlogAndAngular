pipeline {
     agent {
          node {
               label 'agentTwo'
          }
     }
     tools {
          jdk 'jdk21'
          maven 'maven'
     }
     triggers {
        pollSCM('* * * * *')
    }
      // stage('Test') {
      //       steps {
      //           echo "Testing.."
      //           sh '''
      //           chmod 
      //           '''
      //       }
      //   }
    stages {
        stage('clean') {
            steps {
                echo "Cleaning Package"
                sh '''
                 mvn clean compile -DskipTests=true
                '''
            }
        }
       
        stage('Deliver') {
            steps {
                echo 'Deliver....'
                sh '''
                echo "doing delivery stuff.."
                '''
            }
        }
    }
}
