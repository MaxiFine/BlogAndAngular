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
        AWS_ACCESS_KEY_ID = credentials("blog-lab-accesskeys") // Use Jenkins credentials store
//         AWS_SECRET_ACCESS_KEY = credentials("blog-lab-secret-keys") // Ensure you also store secret this way
        AWS_DEFAULT_REGION = "us-east-2"
    }

//     stages {
//         stage('Clean Workspace') {
//             steps {
//                 sh '''
//                     if [ -d "BlogAndAngular" ]; then
//                         echo "Removing existing BlogAndAngular directory..."
//                         rm -rf BlogAndAngular
//                     fi
//                 '''
//                 cleanWs()
//             }
//         }

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

//         stage('Verify Git Configuration') {
//             steps {
//                 sh 'git config --global http.postBuffer 524288000' // Increase buffer
//                 sh 'git config --global http.version HTTP/1.1'     // Set HTTP version to 1.1
//             }
//         }

        stage('Checkout Project') { // Keep this stage
            steps {
                sh 'git clone https://github.com/MaxiFine/BlogAndAngular.git' // Change this to SSH if needed
            }
        }

        stage('Build CLEAN') {
            steps {
                dir('BlogAndAngular') {
                    sh '''
                        if [ ! -f "pom.xml" ]; then
                            echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>ERROR: pom.xml is missing<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                            exit 1
                        fi
                        mvn clean
                    '''
                    echo "BUILD CLEAN>>>>>>>>>>>>>NOW >>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                }
            }
        }

        stage('Build PACKAGE') {
            steps {
                dir('BlogAndAngular') {
                    sh '''
                        if [ ! -f "pom.xml" ]; then
                            echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>ERROR: pom.xml is missing<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                            exit 1
                        fi
                        mvn package
                    '''
                    echo "finally packaged>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
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
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
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

        stage('Deploy Application to EC2 instance') {
            steps {
                script {
                    // Define your EC2 instance details
                    def ec2Instance = 'ubuntu@13.42.38.132'
                    def deploymentDir = "BlogAndAngular" // Assuming it's in the working directory

                    withCredentials([sshUserPrivateKey(credentialsId: 'blog-lab-ssh', keyVariable: 'SSH_KEY_ID')]) {
                        sh """
                        ssh -o StrictHostKeyChecking=no -i \$SSH_PRIVATE_KEY $ec2Instance << 'ENDSSH'
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

                    // Upload to S3 using AWS CLI with access keys set in environment variables
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh "aws s3 cp ${backupFile} s3://$s3Bucket/$backupFile"
                    }

                    // Clean up local backup file
                    sh "rm -f ${backupFile}"
                }
            }
        }
    }

    post {
        always {
            echo '````````````````````````````Pipeline finished.``````````````````````````````'
            echo "Docker image $DOCKER_IMAGE_NAME:$IMAGE_TAG is to be removed."
            sh "docker rmi -f $DOCKER_IMAGE_NAME:$IMAGE_TAG || true"
            //deleteDir()
            sh 'docker system prune -f'
        }
    }
}
