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
        MAVEN_VERSION = '3.8.5'  // Specify the Maven version to install
        MAVEN_HOME = "${env.WORKSPACE}/maven"  // Define where Maven will be installed
    }

    stages {

        stage('Install Maven') {
            steps {
                echo "Installing Maven version ${MAVEN_VERSION}..."
                sh '''
                    // # Download Maven
                    // wget https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -O maven.tar.gz
                    
                    // # Extract Maven
                    // tar -xzf maven.tar.gz
                    
                    // # Move Maven to a workspace folder
                    // mv apache-maven-${MAVEN_VERSION} ${MAVEN_HOME}
                    
                    // # Clean up the tar file
                    // rm maven.tar.gz
                    
                    // # Set Maven executable path
                    // export PATH=${MAVEN_HOME}/bin:$PATH
                    
                    // # Check Maven version
                    // mvn --version

                    # Download Maven using curl
            curl -o maven.tar.gz https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
            
            # Extract Maven
            tar -xzf maven.tar.gz
            
            # Move Maven to a workspace folder
            mv apache-maven-${MAVEN_VERSION} ${MAVEN_HOME}
            
            # Clean up the tar file
            rm maven.tar.gz
            
            # Set Maven executable path
            export PATH=${MAVEN_HOME}/bin:$PATH
            
            # Check Maven version
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
