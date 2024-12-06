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

        stage('Build Docker Image') {
            steps {
                script {
                    sh '''
                        docker build -t $DOCKER_IMAGE_NAME:$IMAGE_TAG .
                    '''
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

        stage('Deploy Application to EC2 instance') {
            steps {
                script {
                    def ec2Instance = 'ubuntu@13.42.38.132'
                    def deploymentDir = "BlogAndAngular"

                    withCredentials([sshUserPrivateKey(credentialsId: 'blog-lab-ssh', keyVariable: 'SSH_KEY_ID')]) {
                        sh """
                        ssh -o StrictHostKeyChecking=no -i \$SSH_KEY_ID $ec2Instance << 'ENDSSH'
                            cd $deploymentDir
                            docker-compose down
                            docker-compose pull
                            docker-compose up -d
                        ENDSSH
                        """
                    }
                }
            }
        }

        stage('Backup Jenkins Server to S3') {
            steps {
                script {
                    def s3Bucket = 'blog-lab-bucket'
                    def backupDir = '/var/jenkins_home'
                    def timestamp = new Date().format("yyyyMMddHHmmss")
                    def backupFile = "jenkins_backup_${timestamp}.tar.gz"

                    sh "tar -czvf ${backupFile} -C ${backupDir} ."
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh "aws s3 cp ${backupFile} s3://$s3Bucket/$backupFile"
                    }
                    sh "rm -f ${backupFile}"
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
