pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    environment {
        IMAGE_NAME     = 'hospital-app'
        CONTAINER_NAME = 'hospital-container'
        APP_PORT       = '8089'
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
                // Wait for Spring Boot to start then verify it's up
                bat "ping -n 31 127.0.0.1 > nul"
                script {
                    def status = bat(
                        script: "docker inspect --format={{.State.Running}} ${CONTAINER_NAME}",
                        returnStdout: true
                    ).trim()
                    if (!status.contains('true')) {
                        bat "docker logs ${CONTAINER_NAME}"
                        error "Container crashed on startup! See logs above."
                    } else {
                        echo "Container is running successfully!"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "SUCCESS: Pipeline complete. App at http://localhost:${HOST_PORT}/login.html"
        }
        failure {
            echo "FAILED: Check logs. Run 'docker logs ${CONTAINER_NAME}' for container errors."
        }
        always {
            echo 'Pipeline complete.'
        }
    }
}
