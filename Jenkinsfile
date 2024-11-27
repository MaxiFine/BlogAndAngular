// // pipeline {
// //     agent {
// //         node {
// //             label 'dev'
// //         }
// //     }
// //
// //     stages {
// //         stage("Code") {
// //             steps {
// //                 git url: "https://github.com/pankajpc15/banking-service-app.git", branch: "master"
// //             }
// //         }
// //         stage("Build") {
// //             steps {
// //                 sh "whoami"
// //                 sh "docker build -t banking-service-api:jenkins ."
// //             }
// //         }
// //         stage("Docker push") {
// //             steps {
// //                 withCredentials([usernamePassword(credentialsId: 'DockerCreds', usernameVariable: 'DockerUsername', passwordVariable: 'DockerPassword')]) {
// //                     sh "docker image tag banking-service-api:jenkins $DockerUsername/banking-service-api:jenkins"
// //                     sh "docker login -u $DockerUsername -p $DockerPassword"
// //                     sh "docker push $DockerUsername/banking-service-api:jenkins"
// //                 }
// //             }
// //         }
// //         stage("Docker Compose") {
// //             steps {
// //                 sh "docker compose down && docker compose up -d"
// //             }
// //         }
// //     }
// // }
//
// // stage('Clean Workspace') {
// //     steps {
// //         deleteDir() // Deletes all files in the workspace
// //     }
// // }
//
//  stage('Build Project') {
//             steps {
//                 sh 'cd /var/lib/jenkins/workspace/test-docker-pipe/BlogAndAngular'
//                 sh 'pwd'
//                 sh 'mvn clean package'
//             }
//         }

// pipeline {
//     agent any
//
// //     environment {
// //         JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
// //         MAVEN_HOME = '/usr/share/maven'
// //     }
//
//     stages {
//
//          stage('Clean Workspace') {
//             steps {
//                 deleteDir()
//             }
//         }
//
//         stage('Verify Build Tools') {
//             steps {
//                 sh 'docker --version'
//                 sh 'git --version'
//                 sh 'java --version'
//
//
//             }
//         }
//
//         stage('Checkout Project') {
//             steps {
//                 sh 'git clone https://github.com/MaxiFine/BlogAndAngular.git'
//             }
//         }
//
//         // stage('Build Project') {
//         //     steps {
//         //         sh 'cd /var/lib/jenkins/workspace/test-docker-pipe/BlogAndAngular'
//         //         sh 'pwd'
//         //         sh 'mvn clean package'
//         //     }
//         // }
//
//                 stage('Build Project') {
//
//                     agent {
//
//                         docker {
//                             image 'maven:3-eclipse-temurin-23-alpine'
//                         }
//
//                     }
//
//                     steps {
//                         dir('BlogAndAngular') { // Navigate into the BlogAndAngular directory
// //                             sh 'ls -l /var/lib/jenkins/workspace/test-docker-pipe'
//                             sh 'pwd' // Confirm the current directory
//                             sh 'mvn clean package' // Build the project
//                 }
//             }
//         }
//
//
//         stage('Run Project') {
//             steps {
//                 echo "DONE BILDING PROJECT..."
//                 echo "DONE BILDING PROJECT..."
//             }
//         }
//     }
// }


pipeline {
    agent any

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Verify Build Tools') {
            steps {
                sh 'docker --version'
                sh 'git --version'
                sh 'java --version'
            }
        }

        stage('Checkout Project') {
            steps {
                sh 'git clone https://github.com/MaxiFine/BlogAndAngular.git'
            }
        }

        stage('Build Project') {
            agent {
                docker {
                    image 'maven:3-eclipse-temurin-23-alpine'
                    args '-v $HOME/.m2:/root/.m2' // Bind local .m2 directory
                }
            }

            steps {
                dir('BlogAndAngular') { // Navigate into the BlogAndAngular directory
                    sh 'pwd' // Confirm the current directory
                    sh 'mvn clean package' // Build the project
                }
            }
        }

        stage('Run Project') {
            steps {
                echo "DONE BUILDING PROJECT..."
            }
        }
    }
}
