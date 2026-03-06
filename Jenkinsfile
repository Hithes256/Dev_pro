pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    environment {
        IMAGE_NAME = 'hospital-app'
        CONTAINER_NAME = 'hospital-container'
        APP_PORT = '8089'
        HOST_PORT = '8089'
        BRANCH = 'main'
    }

    stages {

        stage('Clone Repository') {
            steps {
                echo "Cloning repository..."
                git branch: "${BRANCH}",
                url: "https://github.com/Hithes256/Dev_pro.git"
            }
        }

        stage('Run Tests') {
            steps {
                echo "Running tests..."
                bat 'mvn test'
            }
        }

        stage('Build Maven Project') {
            steps {
                echo "Building jar..."
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                bat 'docker build -t hospital-app .'
            }
        }

        stage('Stop Old Container') {
            steps {
                echo "Stopping old container..."
                bat 'docker stop hospital-container || exit 0'
                bat 'docker rm hospital-container || exit 0'
            }
        }

        stage('Run Docker Container') {
            steps {
                echo "Running new container..."
                bat 'docker run -d -p 8089:8089 --name hospital-container hospital-app'
            }
        }

    }

    post {
        success {
            echo "Application deployed successfully!"
            echo "Open in browser: http://localhost:8089/login.html"
        }
        failure {
            echo "Pipeline failed. Check logs."
        }
    }
}
