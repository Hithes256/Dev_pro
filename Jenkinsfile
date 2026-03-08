pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    environment {
        IMAGE_NAME     = 'hospital-app'
        CONTAINER_NAME = 'hospital-container'
        APP_PORT       = '8080'
        HOST_PORT      = '8089'
        GITHUB_REPO    = 'https://github.com/Hithes256/Dev_pro.git'
        BRANCH         = 'main'
    }

    stages {

        stage('Clone Repository') {
            steps {
                echo "Cloning ${BRANCH} from ${GITHUB_REPO}"
                git branch: "${BRANCH}",
                    url: "${GITHUB_REPO}"
            }
        }

        stage('Run TestNG Tests') {
            steps {
                echo 'Running TestNG test suite...'
                bat 'mvn test'
            }
            post {
                always {
                    testNG reportFilenamePattern: '**/target/surefire-reports/testng-results.xml'
                }
            }
        }

        stage('Build with Maven') {
            steps {
                echo 'Building jar...'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image: ${IMAGE_NAME}:latest"
                bat "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Remove Old Container') {
            steps {
                echo 'Removing old container if running...'
                bat "docker stop ${CONTAINER_NAME} || exit 0"
                bat "docker rm   ${CONTAINER_NAME} || exit 0"
            }
        }

        stage('Run Docker Container') {
            steps {
                echo "Starting ${CONTAINER_NAME} on port ${HOST_PORT}..."
                bat "docker run -d --name ${CONTAINER_NAME} -p ${HOST_PORT}:${APP_PORT} --restart unless-stopped ${IMAGE_NAME}:latest"
            }
        }

        stage('Health Check') {
            steps {
                echo 'Waiting for Spring Boot to start...'
                // Wait 30 seconds for Spring Boot to fully start
                bat "ping -n 31 127.0.0.1 > nul"
                // Retry curl up to 5 times, 10 seconds apart
                // If Spring Boot still not up after 30s, retries give extra time
                retry(5) {
                    script {
                        def result = bat(
                            script: "curl -f http://localhost:${HOST_PORT}/api/doctors",
                            returnStatus: true
                        )
                        if (result != 0) {
                            // Wait 10 more seconds before next retry
                            bat "ping -n 11 127.0.0.1 > nul"
                            error "App not ready yet, retrying..."
                        }
                    }
                }
                echo "Health check passed! App is up at http://localhost:${HOST_PORT}"
            }
        }
    }

    post {
        success {
            echo "SUCCESS: All stages passed. App running at http://localhost:${HOST_PORT}"
        }
        failure {
            echo "FAILED: Pipeline failed. Check the stage logs above."
        }
        always {
            echo 'Pipeline complete.'
        }
    }
}
