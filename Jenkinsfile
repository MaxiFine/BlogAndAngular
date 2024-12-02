pipeline {
    agent {
        label 'agent1'
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-creds'
        DOCKER_IMAGE_NAME = 'maxfine22/blog-app'
        IMAGE_TAG = "4.0"
        PROJECT_URL = "http://localhost:8027/api/v1/blog/all-posts"
    }

    stages {

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
                            echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>ERROR: pom.xml is missing<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                            exit 1
                        fi
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
                                echo "<<<<<<<<<<<<<<<<<Building Docker image...>>>>>>>>>>>>>>>>>>>"
                                docker build -t $DOCKER_IMAGE_NAME:$IMAGE_TAG .
                                echo "<<<<<<<<<<<<<<<<<<<<<<<IMAGE BUILT>>>>>>>>>>>>>>>>>>>>>>>>>"
                                echo "IMAGE BUILT WITH TAG $IMAGE_TAG>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

                            '''
                        }
                    }
                }


            stage('Login to Docker Hub') {
                steps {
                    script {
                        withCredentials([usernamePassword(credentialsId: "$DOCKER_CREDENTIALS_ID", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            sh '''
                                echo "Logging into Docker Hub...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                                echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                                echo "Login successful!<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                            '''
                        }
                    }
                }
            }


        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        docker.image("$DOCKER_IMAGE_NAME:$IMAGE_TAG").push()
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
                    docker run -d --name jenkins-built-container -p 8027:8027 maxfine22/blog-app:4.0
                '''
            }
        }

        stage('Accessing the App Project') {
            steps {
                echo "Test the application at $PROJECT_URL"
            }
        }
    }

    post {
        always {
            echo '````````````````````````````Pipeline finished.``````````````````````````````'
            sh "docker rmi -f $DOCKER_IMAGE_NAME:$IMAGE_TAG || true"
            echo " Docker image $DOCKER_IMAGE_NAME:$IMAGE_TAG has been removed.>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            deleteDir()  // removing workspace files

        }
    }
}
