pipeline {
    agent any

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Verify Build Tools') {
            steps {
                sh 'docker --version'
                sh 'git --version'
                sh 'java --version'
            }
        }

        stage('Checkout Project') {
            steps {
                sh 'git clone https://github.com/MaxiFine/BlogAndAngular.git'
            }
        }

        stage('Build Project') {
            agent {
                docker {
                    image 'maven:3-eclipse-temurin-23-alpine'
                    args '-v $WORKSPACE/.m2:/root/.m2' // Bind a writable .m2 directory
                }
            }

            steps {
                dir('BlogAndAngular') { // Navigate into the BlogAndAngular directory
                    sh 'pwd' // Verify current directory
                    sh 'mvn clean package' // Build the project
                }
            }
        }

        stage('Run Project') {
            steps {
                echo "Project built successfully!"
            }
        }
    }
}
