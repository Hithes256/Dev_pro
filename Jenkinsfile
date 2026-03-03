pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/Hithes256/Dev_pro.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t hospital-app .'
            }
        }

        stage('Docker Deploy') {
            steps {
                bat 'docker stop hospital-container || exit 0'
                bat 'docker rm hospital-container || exit 0'
                bat 'docker run -d -p 9090:8085 --name hospital-container hospital-app'
            }
        }
    }
}