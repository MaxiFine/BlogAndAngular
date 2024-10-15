pipeline {
     agent any
    stages {
        stage('clean') {
            steps {
                echo "Cleaning Package"
                sh '''
                mvn clean package
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
