pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git 'YOUR_GITHUB_REPO_URL'
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

        stage('Docker Run') {
            steps {
                bat 'docker stop hospital-container || exit 0'
                bat 'docker rm hospital-container || exit 0'
                bat 'docker run -d -p 9090:8080 --name hospital-container hospital-app'
            }
        }
    }
}