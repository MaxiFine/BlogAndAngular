pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-creds' // Jenkins credentials ID for Docker Hub
        DOCKER_IMAGE_NAME = 'maxfine22/blog-app:3.5' // Docker Hub image name
    }

    stages {

        stage('Clean Maven') {
            steps {
                echo "Workspace cleaned successfully."
                sh 'mvn clean'
                echo "HAPPY CLEANING>>>>>>>>"

            }
        }

        stage('Verify Build Tools') {
            steps {
                // Check if required build tools are installed
                sh 'docker --version'
                sh 'git --version'
                sh 'java --version'
                sh 'mvn -v'
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

        stage('Build Docker Image') {

            steps {
                script {
                    // Build the Docker image
                    sh '''
                        echo "Building Docker image..."
                        docker build -t maxfine22/blog-app:4.0 $DOCKER_IMAGE_NAME
                    '''
                }
            }
        }

        stage('Login to Docker Hub') {

            steps {
                script {
                    // Login to Docker Hub
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        echo "Logged in to Docker Hub successfully."
                    }
                }
            }
        }

        stage('Push Docker Image') {

            steps {
                script {
                    // Push the Docker image to Docker Hub
                    sh '''
                        echo "Pushing Docker image to Docker Hub..."
                        docker push maxfine22/blog-app:4.0
                    '''
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Run the Docker container
                    sh '''
                        echo "Running Docker container..."
                        docker run -d --name jenkins-built-container -p 8027:8027 maxfine22/blog-app:4.0
                        echo container now running>>>>>>
                    '''
                }
            }
        }

        stage('Run Project') {
            steps {
                echo "Project built and container started successfully and deployed."
                echo "Test at http://localhost:8027"
                echo "Thank you...."
            }
        }
    }
}
