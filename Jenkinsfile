pipeline {
     agent any
     triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage('clean') {
            steps {
                echo "Cleaning Package"
                sh '''
               ls -al
                '''
            }
        }
        stage('Test') {
            steps {
                echo "Testing.."
                sh '''
                echo "Testing"
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
