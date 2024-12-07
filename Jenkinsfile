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
        AWS_DEFAULT_REGION = "eu-east-2"
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

        stage('Clean and Package Build') {
            steps {
                script {
                    def cleanAndPackage = { projectDir ->
                        dir(projectDir) {
                            sh '''
                                if [ ! -f "pom.xml" ]; then
                                    echo "ERROR: pom.xml is missing"
                                    exit 1
                                fi
                                echo "NOW CLENAING>>>>>>>??????????"
                                mvn clean
                                echo "NOW Packaging////////////~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
                                mvn package
                            '''
                            echo "CLENAING and Packaging done#########################3"
                        }
                    }

                    cleanAndPackage('BlogAndAngular')
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


        stage('Login and Push Docker Image') {
            steps {
                script {
                    def loginAndPushDockerImage = { credentialsId, imageName, imageTag ->
                        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            sh '''
                                echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                            '''
                        }

                        docker.withRegistry('https://index.docker.io/v1/', credentialsId) {
                            docker.image("${imageName}:${imageTag}").push()
                        }
                    }

                    loginAndPushDockerImage(DOCKER_CREDENTIALS_ID, DOCKER_IMAGE_NAME, IMAGE_TAG)
                }
            }
        }

        stage('Update Docker Compose for Blog-App') {
            steps {
                script {
                    def updateComposeFile = { composeFile, serviceName, imageName, imageTag ->
                        sh """
                            echo "Updating docker-compose file for service: ${serviceName}..."
                            sed -i '/^  ${serviceName}:/,/image:/s|image: ${imageName}:.*|image: ${imageName}:${imageTag}|' ${composeFile}
                            echo "Updated ${serviceName} image to ${imageName}:${imageTag} in ${composeFile}"
                        """
                    }

                    echo "UPDATING COMPSE FILE?????????????????????//"

                    // Define the path to the docker-compose file
                    def composeFilePath = '/home/jenkins/workspace/lab-blog-pipe/BlogAndAngular/docker-compose.yml'

                    // Call the function to update the `blog-app` service's image
                    updateComposeFile(composeFilePath, 'blog-app', DOCKER_IMAGE_NAME, IMAGE_TAG)
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
                   echo "Test the application at $PROJECT_URL"
            }
        }



     stage('Deployment On EC2') {
         steps {
             sshagent(['blog-lab-ssh']) {
                 script {
                     def ec2Host = '13.42.38.132'
                     def deployUser = 'ubuntu'
                     def localRepoPath = '/home/jenkins/workspace/lab-blog-pipe/BlogAndAngular'
                     def remotePath = "/home/${deployUser}"

                     echo "Deploying to EC2 Host: ${ec2Host}"
                     echo "Local Repo Path: ${localRepoPath}"
                     echo "Remote Path: ${remotePath}"

                     // Ensure the local file exists before copying
                     sh "ls -l ${localRepoPath}/docker-compose.yml"

                     // Copy the file to the EC2 instance
                     sh """
                         scp -o StrictHostKeyChecking=no ${localRepoPath}/docker-compose.yml ${deployUser}@${ec2Host}:${remotePath}/docker-compose.yml
                         echo "Docker Compose file copied successfully."
                     """

                     // SSH into the EC2 instance and run commands
                     sh """
                         ssh -o StrictHostKeyChecking=no ${deployUser}@${ec2Host} '
                             cd ${remotePath}
                             echo "Stopping existing containers..."
                             docker-compose down || true
                             echo "Pulling latest images..."
                             docker-compose pull
                             echo "Starting containers..."
                             docker-compose up -d
                             echo "Deployment successful."
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
                def tempBackupDir = '/home/jenkins/temp_backup'

                // Ensure backup directory exists
                sh "mkdir -p ${backupDir}"
                sh "mkdir -p ${tempBackupDir}"

                // Copy workspace to a temporary directory
                sh "cp -r ${jenkinsHome}/workspace/* ${tempBackupDir}/"

                echo "DONE COPYING FILES<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

                // Create a backup archive from the backup directory
                sh "tar --ignore-failed-read  -czvf ${backupDir}/${backupFile} -C ${tempBackupDir} ."

                echo "Backup file>>>>>>>>>>>>>>: ${backupFile}"

                withAWS(credentials:  'blog-lab-accesskeys', region: 'eu-west-2') {
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