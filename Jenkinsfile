pipeline {
    agent any

    environment {
//         DOCKER_CREDENTIALS_ID = 'docker-creds' // Jenkins credentials ID for Docker Hub
        DOCKER_CREDENTIALS_ID = 'dockerhub-creds' // Jenkins credentials ID for Docker Hub
        DOCKER_IMAGE_NAME = 'maxfine22/blog-app:3.5' // Docker Hub image name
        IMAGE_TAG = "4.0"
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
                sh '''
                    if [ -d "BlogAndAngular" ]; then
                        echo "Removing existing BlogAndAngular directory..."
                        rm -rf BlogAndAngular
                    fi
                '''
            }
        }

        stage('Verify Build Tools') {
            steps {
                sh 'docker --version'
                sh 'git --version'
                sh 'java --version'
                sh 'mvn -v'
            }
        }

        stage('Checkout Project') {
            steps {
                sh '''
                    git clone https://github.com/MaxiFine/BlogAndAngular.git
                '''
            }
        }

        stage('Build Project') {
            steps {
                dir('BlogAndAngular') {
                    sh '''
                        if [ ! -f "pom.xml" ]; then
                            echo "ERROR: pom.xml is missing"
                            exit 1
                        fi
                        mvn clean package
                    '''
                }
            }
        }

//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     docker.build("maxfine22/blog-app:3.5")
//                 }
//             }
//         }

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
                    docker.withRegistry('https://registry.hub.docker.com' DOCKER_CREDENTIALS_ID) {
                        echo 'Logged in to Docker Hub>>>>>>>>>>>>>>>>>'
                        echo 'Logged in to Docker $DOCKER_CREDENTIALS_ID>>>>>>>>>>>>>>>>>>'
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry("'https://registry.hub.docker.com'", DOCKER_CREDENTIALS_ID) {
                        docker.image("${DOCKER_IMAGE_NAME}:${IMAGE_TAG}").push()
                    }
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                sh '''
                    if [ "$(docker ps -aq -f name=jenkins-built-container)" ]; then
                        docker stop jenkins-built-container || true
                        docker rm jenkins-built-container || true
                    fi
                    docker run -d --name jenkins-built-container -p 8027:8027 ${DOCKER_IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }

        stage('Run Project') {
            steps {
                echo "Test the application at http://<server-ip>:8027"
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
    }
}
