@Library("max-shared-libraries") _

pipeline {
    agent {
        label 'agent1'
    }

    environment {
        DOCKER_CREDENTIALS_ID = "docker-hub-creds"
        DOCKER_IMAGE_NAME = "maxfine22/blog-app"
        PROJECT_URL = "http://localhost:8027/api/v1/blog/all-posts"
        AWS_ACCESS_KEY_ID = credentials("lab-access-key")
        AWS_DEFAULT_REGION = "eu-west-2"
        DOCKERFILE = "Dockerfile"
        ENVFILE =  credentials("blogEnv")
    }

    stages {
        stage('Set Git SHA') {
            steps {
                script {
                    env.IMAGE_TAG = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    hellowWorld()
                }
            }
        }

          stage('Load Environment Variables') {
                    steps {
                        script {
                            // Read .env file and export variables
                            def envFile = readFile "/mnt/c/Users/MaxwellAdomako/IdeaProjects/learnings/blog/.env"
                            envFile.split('\n').each { line ->
                                def keyValue = line.tokenize('=')
                                if (keyValue.size() == 2) {
                                    env."${keyValue[0].trim()}" = keyValue[1].trim()
                                }
                            }
                        }
                    }
                }

        stage('Clean and Package Build') {
            steps {
                script {
                  // USING METHOD AND INVOCATION TOGETHER
                    runMaven("clean")
                    mavenUtils.runMaven("package")
                }
            }
        }

                // run sonarqube ANALYSIS
            stage('Run Sonarqube') {
                      steps {
                              withSonarQubeEnv(credentialsId: 'sonarqube', installationName: 'sonarqube') {
                                sh "mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.java.binaries=target/classes"
                              }
                            }
                        }

            stage('SonarQube Quality Gate') {
                steps {
                    script {
                        timeout(time: 1, unit: 'MINUTES') {
                            def qualityGate = waitForQualityGate()
                            if (qualityGate.status != 'OK') {
                                echo "Quality Gate failed: ${qualityGate.status}. Proceeding with pipeline execution."
//                             currentBuild.result = 'UNSTABLE'
                            } else {
                                echo "Quality Gate passed: ${qualityGate.status}."
                            }
                        }
                    }
                }
            }



        stage('Build Docker Image') {
            steps {
                script {
                    // using simple invocation type
                    buildDockerImage(env.DOCKER_IMAGE_NAME, env.IMAGE_TAG, env.DOCKERFILE)
                }
            }
        }

        stage('Login and Push Docker Image') {
            steps {
                script {
                    // using method call type
                    dockerUtilities.loginAndPushDockerImage(env.DOCKER_CREDENTIALS_ID, env.DOCKER_IMAGE_NAME, env.IMAGE_TAG)
                }
            }
        }

        stage('Update Docker Compose for Blog-App') {
            steps {
                script {
                    def composeFilePath = '/home/jenkins/workspace/lab-blog-pipe/docker-compose.yml'
                    sh """
                        sed -i '/^  blog-app:/,/image:/s|image: $DOCKER_IMAGE_NAME:.*|image: $DOCKER_IMAGE_NAME:$IMAGE_TAG|' $composeFilePath
                    """
                }
            }
        }


        stage('Deploy to EC2') {
            steps {
                sshagent(['blog-lab-ssh']) {
                    script {
                        def ec2Host = '13.42.24.82'
                        def user = 'ubuntu'
                        def localFile = '/home/jenkins/workspace/blog-pipe/docker-compose.yml'
                        def remotePath = '/home/ubuntu/docker-compose.yml'

                       echo'<<<<<>>>>>>>>>>>>>>>>>>>>>>NOW ABOUT TO SSH>>>>>>>>>>>>>>>>>>>>'

                        // SCP Command
                        sh """
                        scp -o StrictHostKeyChecking=no ${localFile} ${user}@${ec2Host}:${remotePath}
                        """
                        echo "SSH DONE >>>>>>>>>>>>>>>FILE COPY DONE>>>>>>>>>>"
                        // SSH Command
                        sh """
                        ssh -o StrictHostKeyChecking=no ${user}@${ec2Host} '
                            cd /home/ubuntu
                            docker-compose down || true
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
                script {  // just testing libraries
//                     def s3Bucket = 'blog-lab-bucket'
//                     def backupDir = '/home/jenkins/backups'
//                     def jenkinsHome = '/home/jenkins'
//                     def timestamp = new Date().format("yyyyMMddHHmmss")
//                     def backupFile = "jenkins_backup_${timestamp}.tar.gz"
//                     def tempBackupDir = '/home/jenkins/temp_backup'
//
//                     sh "cp -r ${jenkinsHome}/workspace/* ${tempBackupDir}/"
//
//                     sh "tar --ignore-failed-read -czvf ${backupDir}/${backupFile} -C ${tempBackupDir} ."
//
//                     withAWS(credentials:  'lab-access-key', region: 'eu-west-2') {
//                         s3Upload(bucket: s3Bucket, file: "${backupDir}/${backupFile}")
//                     }

                    // Cleanup Backup Files
//                     sh "rm -f ${backupDir}/${backupFile}"
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
