pipeline {
    agent {
        label 'agent1'
    }

    environment {
        DOCKER_CREDENTIALS_ID = "docker-hub-creds"
        DOCKER_IMAGE_NAME = "maxfine22/blog-app"
        IMAGE_TAG = "4.0"
        PROJECT_URL = "http://localhost:8027/api/v1/blog/all-posts"
        SSH_KEY_ID = "blog-lab-ssh"
        AWS_ACCESS_KEY_ID = credentials("blog-lab-accesskeys")
        AWS_DEFAULT_REGION = "us-east-2"
        APP_NAME = "jenkins-built-container"
         BLOG_ENV = credentials('blogEnv')
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
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
                sh 'git clone https://github.com/MaxiFine/BlogAndAngular.git'
            }
        }

        stage('Build CLEAN') {
            steps {
                dir('BlogAndAngular') {
                    sh '''
                        if [ ! -f "pom.xml" ]; then
                            echo "ERROR: pom.xml is missing"
                            exit 1
                        fi
                        mvn clean
                    '''
                }
            }
        }

        stage('Build PACKAGE') {
            steps {
                dir('BlogAndAngular') {
                    sh '''
                        if [ ! -f "pom.xml" ]; then
                            echo "ERROR: pom.xml is missing"
                            exit 1
                        fi
                        mvn package
                    '''
                }
            }
        }

        stage('Verify Files') {
            steps {
                dir('BlogAndAngular') {
                    sh 'ls -la'
                }
            }
        }

//
//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     sh '''
//                         docker build -t $DOCKER_IMAGE_NAME:$IMAGE_TAG .
//                     '''
//                 }
//             }
//         }


        stage('Build Docker Image') {
            steps {
                dir('BlogAndAngular') { // Ensure you are in the correct directory
                    script {
                        sh '''
                            echo "<<<<<<<<<<<<<<<<<Building Docker image...>>>>>>>>>>>>>>>>>>>"
                            docker build -t $DOCKER_IMAGE_NAME:$IMAGE_TAG -f Dockerfile .
                            echo "<<<<<<<<<<<<<<<<<<<<<<<IMAGE BUILT>>>>>>>>>>>>>>>>>>>>>>>>>"
                            echo "IMAGE BUILT WITH TAG $IMAGE_TAG>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                        '''
                    }
                }
            }
        }




        stage('Login to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                            echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
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
                    docker run -d --name jenkins-built-container -p 8027:8027 $DOCKER_IMAGE_NAME:$IMAGE_TAG
                '''
            }
        }

        stage('Accessing the App Project') {
            steps {
                echo "Test the application at $PROJECT_URL"
            }
        }
//
//          stage('Deployment On EC2') {
//                     steps {
//                         sshagent(['blog-lab-ssh']) {
//                             sh '''
//                                 ssh -o StrictHostKeyChecking=no ubuntu@13.42.38.132 "docker pull ${DOCKER_IMAGE_NAME}:${IMAGE_TAG} || true && docker run -d -p 8027:8027 --name ${APP_NAME} ${DOCKER_IMAGE_NAME}:${IMAGE_TAG}"
//                             '''
//                         }
//                     }
//                 }


            stage('Deployment On EC2') {
                steps {
                    sshagent(['blog-lab-ssh']) {
                        sh '''
                            # Stop and remove existing container if it exists
                            ssh -o StrictHostKeyChecking=no ubuntu@13.42.38.132 "docker stop ${APP_NAME} || true"
                            ssh -o StrictHostKeyChecking=no ubuntu@13.42.38.132 "docker rm ${APP_NAME} || true"

                            # Pull and run the new container with env file
                            ssh -o StrictHostKeyChecking=no ubuntu@13.42.38.132 "docker pull ${DOCKER_IMAGE_NAME}:${IMAGE_TAG} && \
                            docker run -d \
                            --env-file /home/ubuntu/.env \
                            -p 8027:8027 \
                            --name ${APP_NAME} \
                            ${DOCKER_IMAGE_NAME}:${IMAGE_TAG}"
                        '''
                    }
                }
            }






      stage('Backup Jenkins Server to S3') {
          steps {
              script {
                  try {
                      def s3Bucket = 'blog-lab-bucket'
                      def backupDir = '/home/jenkins_home'
                      def timestamp = new Date().format("yyyyMMddHHmmss")
                      def backupFile = "jenkins_backup_${timestamp}.tar.gz"

                      // Check if backup directory exists before creating tar
                      sh "test -d ${backupDir}"

                      sh "tar -czvf ${backupFile} -C ${backupDir} ."
                      withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                          sh "aws s3 cp ${backupFile} s3://$s3Bucket/$backupFile"
                      }
                      sh "rm -f ${backupFile}"

                      echo "Backup successful: ${backupFile}"
                  } catch (Exception e) {
                      echo "Backup failed: ${e.message}"
                      // Optionally, you can choose to fail the pipeline or just log the error
                      // currentBuild.result = 'FAILURE'
                  }
              }
          }
      }

    post {
        success {
            echo 'Pipeline finished successfully.'
            sh "docker rmi -f $DOCKER_IMAGE_NAME:$IMAGE_TAG || true"
            sh 'docker system prune -f'
        }
        failure {
            echo 'Pipeline failed.'
        }
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
