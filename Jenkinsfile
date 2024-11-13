pipeline {
    agent any
    tools {
        jdk 'jdk21'         // Use the JDK version you configured in Jenkins
        maven 'maven'       // Ensure 'maven' is the name of your Maven installation in Jenkins
    }

    stages {
        stage('Build') {
            steps {
                // Checkout code
                git branch: 'main', url: 'https://github.com/YourRepo/YourSpringBootApp.git', credentialsId: 'your-credential-id'

                // Compile the code
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Test') {
            steps {
                // Run tests
                sh 'mvn test'
            }
        }
    }

    post {
        always {
            // Archive test results if needed
            archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Build and tests succeeded!'
        }
        failure {
            echo 'Build or tests failed.'
        }
    }
}
