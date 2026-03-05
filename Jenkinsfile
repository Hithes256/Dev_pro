pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    stages {

        stage('Clone Repository') {
            steps {
                git 'https://github.com/YOUR_USERNAME/hospital-management-system.git'
            }
        }

        stage('Build with Maven') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t hospital-app .'
            }
        }

        stage('Stop Old Container') {
            steps {
                bat 'docker stop hospital-container || exit 0'
                bat 'docker rm hospital-container || exit 0'
            }
        }

        stage('Run Docker Container') {
            steps {
                bat 'docker run -d -p 8085:8085 --name hospital-container hospital-app'
            }
        }
    }
}