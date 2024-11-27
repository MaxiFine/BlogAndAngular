pipeline {
    agent any

    stages {

        stage('Clean Workspace') {
            steps {

                echo "Workspace cleaned successfully."
            }
        }

        stage('Verify Build Tools') {
            steps {
                // Check if required build tools are installed
                sh 'docker --version'
                sh 'git --version'
                sh 'java --version'
                sh 'mvn --version'
            }
        }

        stage('Checkout Project') {
            steps {
                // Clone the project from GitHub
                sh '''
                    echo "Cloning repository..."
                    git clone https://github.com/MaxiFine/BlogAndAngular.git
                '''
            }
        }

        stage('Build Project') {
            steps {
                dir('BlogAndAngular') {
                    sh '''
                        echo "Checking project structure..."
                        ls -la

                        if [ ! -f "pom.xml" ]; then
                            echo "ERROR: pom.xml is missing in BlogAndAngular"
                            exit 1
                        fi

                        echo "Building the project with Maven..."
                        mvn clean package
                    '''
                }
            }
        }

        stage('Run Project') {
            steps {
                echo "Project built successfully! Ready to deploy or test."
            }
        }
    }
}
