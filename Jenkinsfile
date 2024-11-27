// pipeline {
//     agent any
//
//     stages {
//
//         stage('Clean Workspace') {
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
//             }
//         }
//
//         stage('Checkout Project') {
//             steps {
//                 sh 'git clone https://github.com/MaxiFine/BlogAndAngular.git'
//             }
//         }
//
//         stage('Build Project') {
//             agent {
//                 docker {
//                     image 'maven:3-eclipse-temurin-23-alpine'
//                     // args '-v /var/jenkins_home/.m2:/root/.m2' // Bind writable .m2 directory
//                     // args '-v $WORKSPACE/.m2:/root/.m2'
//                     args '--user root -v /var/jenkins_home/.m2:/root/.m2'
//
//                 }
//             }
//
//             steps {
//                 dir('BlogAndAngular') {
//                     // Check user and set permissions
//                     sh '''
//
//             id
//             echo "Home Directory:"
//             echo $HOME
//             echo "Setting Maven Local Repository to a User-Accessible Path..."
//             mkdir -p $HOME/.m2/repository
//             chmod -R 777 $HOME/.m2
//             mvn -Dmaven.repo.local=$HOME/.m2/repository clean package
//                     '''
//                 }
//             }
//         }
//
//         stage('Run Project') {
//             steps {
//                 echo "Project built successfully!"
//             }
//         }
//     }
// }
//

// new fix

pipeline {
    agent any

    stages {

        stage('Clean Workspace') {
            steps {

                sh 'echo "DONE CLEANING"'
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
                // Remove old directory
                sh 'rm -rf BlogAndAngular || true'

                // Clone the repository
                sh 'git clone https://github.com/MaxiFine/BlogAndAngular.git'
            }
        }

        stage('Build Project') {
            agent {
                docker {
                    image 'maven:3-eclipse-temurin-23-alpine'
                    args '--user root -v /var/jenkins_home/.m2:/root/.m2'
                }
            }

            steps {
                sh '''
                    echo "Workspace contents:"
                    ls -la

                    if [ ! -f "BlogAndAngular/pom.xml" ]; then
                        echo "ERROR: pom.xml is missing in BlogAndAngular"
                        exit 1
                    fi

                    echo "Home Directory:"
                    echo $HOME
                    echo "Setting Maven Local Repository to a User-Accessible Path..."
                    mkdir -p $HOME/.m2/repository
                    chmod -R 777 $HOME/.m2
                    mvn -Dmaven.repo.local=$HOME/.m2/repository clean package
                '''
            }
        }

        stage('Run Project') {
            steps {
                echo "Project built successfully!"
            }
        }
    }
}

