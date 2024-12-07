def gitSha() {
    return sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
//     gitSha = sh(script: 'git log -n 1 --pretty=format:"%H"', returnStdout: true).trim()
}

pipeline {
    agent {
        label 'agent1'
    }

    environment {
        DOCKER_CREDENTIALS_ID = "docker-hub-creds"
        DOCKER_IMAGE_NAME = "maxfine22/blog-app"
        IMAGE_TAG = gitSha()
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

        stage('Build Docker Image') {
            steps {
                dir('BlogAndAngular') {
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

      stage('Deployment On EC2') {
          steps {
              sshagent(['blog-lab-ssh']) {
                  script {
                      def ec2Host = '13.42.38.132'
                      def deployUser = 'ubuntu'
                      def repoName = 'BlogAndAngular'

                      // Copy docker-compose file to EC2 instance
                      sh """
                          scp -o StrictHostKeyChecking=no docker-compose.yml ${deployUser}@${ec2Host}:~/${repoName}/docker-compose.yml
                      """

                      // SSH into the instance and run Docker Compose
                      sh """
                          ssh -o StrictHostKeyChecking=no ${deployUser}@${ec2Host} '
                              cd ~/${repoName}
                              docker-compose down || true
                              docker-compose pull
                              docker-compose up -d
                          '
                      """
                  }
              }
          }
      }

    stage('Backup Jenkins Server to S3') {
        steps {
            script {
                def s3Bucket = 'blog-lab-bucket'
                def backupDir = '/home/jenkins/backups'
                def jenkinsHome = '/home/jenkins'
                def timestamp = new Date().format("yyyyMMddHHmmss")
                def backupFile = "jenkins_backup_${timestamp}.tar.gz"

                // Ensure backup directory exists
                sh "mkdir -p ${backupDir}"

                // Create a temporary backup folder
                sh "cp -r ${jenkinsHome}/workspace ${backupDir} || exit 0"

                // Create a backup archive from the backup directory
                sh "tar -czvf ${backupDir}/${backupFile} -C ${backupDir} ."

                echo "Backup file>>>>>>>>>>>>>>: ${backupFile}"

                withAWS(credentials: 'blog-lab-accesskeys', region: 'us-east-2') {
                    echo "Uploading file to S3..."
                    s3Upload(bucket: s3Bucket, file: "${backupDir}/${backupFile}")
                }

                // Cleanup
                sh "rm -f ${backupDir}/${backupFile}"

                echo "Backup successful: ${backupFile}"
                sh "docker rmi -f $DOCKER_IMAGE_NAME:$IMAGE_TAG || true"
                sh 'docker system prune -f'
            }
        }
    }
   }

    post {
        success {
            echo 'Pipeline finished successfully.'

        }
        failure {
            echo 'Pipeline failed.'
        }
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
}