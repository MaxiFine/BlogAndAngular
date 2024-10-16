pipeline {
    agent {
        node {
            label 'agentTwo'
        }
    }

    triggers {
        pollSCM('* * * * *')  // Poll SCM every minute
    }

    environment {
        MAVEN_VERSION = '3.8.8'  // Specify the Maven version to install
        MAVEN_HOME = "${env.WORKSPACE}/maven"  // Define where Maven will be installed
    }

    stages {

        stage('Install Maven') {
            steps {
                echo "Installing Maven version ${MAVEN_VERSION}..."
                sh '''
                    # Download Maven
                    wget https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -O maven.tar.gz
                
                    tar -xzf maven.tar.gz
                    
                    mv apache-maven-${MAVEN_VERSION} ${MAVEN_HOME}
                 
                    rm maven.tar.gz
                    
                    export PATH=${MAVEN_HOME}/bin:$PATH
                    
                    mvn --version
                '''
            }
        }

        stage('Clean') {
            steps {
                echo "Cleaning and compiling the project..."
                sh '''
                    # Ensure Maven binary is in the PATH
                    export PATH=${MAVEN_HOME}/bin:$PATH
                    
                    # Run Maven clean and compile, skipping tests
                    mvn clean compile -DskipTests=true
                '''
            }
        }

        stage('Test') {
            steps {
                echo "Running tests..."
                sh '''
                    # Ensure Maven binary is in the PATH
                    export PATH=${MAVEN_HOME}/bin:$PATH
                    
                    # Run Maven tests
                    mvn test
                '''
            }
        }

        stage('Deliver') {
            steps {
                echo "Delivering application..."
                sh '''
                    echo "Delivery process placeholder."
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline complete."
        }

        success {
            echo "Build and delivery successful!"
        }

        failure {
            echo "Build failed."
        }
    }
}
