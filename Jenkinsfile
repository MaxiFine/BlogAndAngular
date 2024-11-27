pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-creds' // Jenkins credentials ID for Docker Hub
        DOCKER_IMAGE_NAME = 'maxfine22/blog-app:3.5' // Docker Hub image name
    }

    stages {

        stage('Load Environment Variables from Credentials') {
                    steps {
                        withCredentials([file(credentialsId: 'blogEnv', variable: 'ENV_FILE')]) {
                            sh '''
                                echo "Loading environment variables from credentials..."
                                set -a
                                . $ENV_FILE
                                set +a
                                echo "SAMPLE_ENV is $SAMPLE_ENV"
                            '''
                        }
                    }
                }


        stage('Clean Workspace') {
            steps {
                echo "Cleaning up workspace..."
                sh '''
                    if [ -d "BlogAndAngular" ]; then
                        echo "Removing existing BlogAndAngular directory..."
                        rm -rf BlogAndAngular
                    fi
                '''
                echo "Workspace cleaned successfully."
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
                dir('BlogAndAngular') {
                    sh '''
                        echo "Checking project structure..."
                        ls -la

                        if [ ! -f "pom.xml" ]; then
                            echo "ERROR: pom.xml is missing>>>>>>>>>>>>>>>>>>>>>"
                            exit 1
                        fi

                        echo "Building the project with Maven... TEST Maven>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                        mvn clean package
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh '''
                        echo "Building Docker image..."
                        docker build -t maxfine22/blog-app:4.0 .
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
                    // Stop and remove any existing container with the same name
                    sh '''
                        if [ "$(docker ps -aq -f name=jenkins-built-container)" ]; then
                            echo "Stopping and removing existing container..."
                            docker stop jenkins-built-container || true
                            docker rm jenkins-built-container || true
                        fi

                        echo "Running Docker container..."
                        docker run -d --name jenkins-built-container -p 8027:8027 maxfine22/blog-app:4.0
                    '''
                }
            }
        }

        stage('Run Project') {
            steps {
                echo "Project built and container started successfully."
                echo "Test the application at http://<server-ip>:8027"
            }
        }
    }
}
