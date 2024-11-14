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


pipeline {
    agent {
        node {
            label 'dev'
        }
    }

    stages {
        stage("Code") {
            steps {
                git url: "https://github.com/pankajpc15/banking-service-app.git", branch: "master"
            }
        }
        stage("Build") {
            steps {
                sh "whoami"
                sh "docker build -t banking-service-api:jenkins ."
            }
        }
        stage("Docker push") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'DockerCreds', usernameVariable: 'DockerUsername', passwordVariable: 'DockerPassword')]) {
                    sh "docker image tag banking-service-api:jenkins $DockerUsername/banking-service-api:jenkins"
                    sh "docker login -u $DockerUsername -p $DockerPassword"
                    sh "docker push $DockerUsername/banking-service-api:jenkins"
                }
            }
        }
        stage("Docker Compose") {
            steps {
                sh "docker compose down && docker compose up -d"
            }
        }
    }
}