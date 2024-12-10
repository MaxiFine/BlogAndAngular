def gitSha() {
    return sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
}

pipeline {
    agent {
        label 'agent1'
    }

    environment {
        DOCKER_CREDENTIALS_ID = "dockerhub-creds"
        DOCKER_IMAGE_NAME = "maxfine22/blog-app"
        IMAGE_TAG = gitSha()
        PROJECT_URL = "http://localhost:8027/api/v1/blog/all-posts"
        SSH_KEY_ID = "blog-lab-accesskeys"
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
                                mvn clean
                                mvn package
                            '''
                        }
                    }
                    cleanAndPackage('BlogAndAngular')
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('BlogAndAngular') {
                    script {
                        sh '''
                            docker build -t $DOCKER_IMAGE_NAME:$IMAGE_TAG -f Dockerfile .
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
                            sed -i '/^  ${serviceName}:/,/image:/s|image: ${imageName}:.*|image: ${imageName}:${imageTag}|' ${composeFile}
                        """
                    }
                    def composeFilePath = '/home/jenkins/workspace/lab-blog-pipe/BlogAndAngular/docker-compose.yml'
                    updateComposeFile(composeFilePath, 'blog-app', DOCKER_IMAGE_NAME, IMAGE_TAG)
                }
            }
        }

     stage('Deployment On EC2') {
         steps {
             sshagent(['blog-lab-accesskeys']) {
                 script {
                     def ec2Host = '13.42.38.132'
                     def deployUser = 'ubuntu'
                     def localRepoPath = '/home/jenkins/workspace/lab-blog-pipe/BlogAndAngular'
                     def remotePath = "/home/${deployUser}"
                     sh """
                         scp -o StrictHostKeyChecking=no ${localRepoPath}/docker-compose.yml ${deployUser}@${ec2Host}:${remotePath}/docker-compose.yml
                     """
                     sh """
                         ssh -o StrictHostKeyChecking=no ${deployUser}@${ec2Host} '
                             cd ${remotePath}
                             docker-compose down || true
                             # Remove unused images, networks, and volumes to free space
                             docker system prune -af --volumes || true
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
                def tempBackupDir = '/home/jenkins/temp_backup'

                sh "cp -r ${jenkinsHome}/workspace/* ${tempBackupDir}/"

                sh "tar --ignore-failed-read  -czvf ${backupDir}/${backupFile} -C ${tempBackupDir} ."

                withAWS(credentials:  'blog-lab-accesskeys', region: 'eu-west-2') {
                    echo "Uploading file to S3..."
                    s3Upload(bucket: s3Bucket, file: "${backupDir}/${backupFile}")
                }

                // Cleanup Workspace
                sh "rm -f ${backupDir}/${backupFile}"
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