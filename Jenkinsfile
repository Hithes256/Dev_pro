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
                    // FIX: testNG step requires named parameter 'testResultsPattern'
                    // not a plain string like testNG '...'
                    testNG testResultsPattern: '**/target/surefire-reports/testng-results.xml'
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
                bat "timeout /t 15 /nobreak"
                bat "curl -f http://localhost:${HOST_PORT}/api/doctors"
            }
        }
    }

    post {
        success {
            echo "SUCCESS: All tests passed. App running at http://localhost:${HOST_PORT}"
        }
        failure {
            echo "FAILED: Pipeline failed. Check the stage logs above."
        }
        always {
            echo 'Pipeline complete.'
        }
    }
}
