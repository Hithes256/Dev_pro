pipeline {
    agent any

    tools {
        // Must match the Maven installation name in:
        // Jenkins → Manage Jenkins → Tools → Maven installations
        maven 'Maven-3'
    }

    environment {
        IMAGE_NAME     = 'hospital-app'
        CONTAINER_NAME = 'hospital-container'
        APP_PORT       = '8080'   // Spring Boot internal port (inside container)
        HOST_PORT      = '8089'   // Port you access from browser
        GITHUB_REPO    = 'https://github.com/Hithes256/Dev_pro.git'
        BRANCH         = 'main'
    }

    stages {

        // ── 1. Pull latest code ───────────────────────────────────────────────
        stage('Clone Repository') {
            steps {
                echo "Cloning ${BRANCH} from ${GITHUB_REPO}"
                git branch: "${BRANCH}",
                    url: "${GITHUB_REPO}"
                    // Private repo? Add: credentialsId: 'github-credentials'
            }
        }

        // ── 2. Run TestNG tests ───────────────────────────────────────────────
        // FIX: use 'bat' not 'sh' because your Jenkins runs on Windows
        // FIX: removed -Dsurefire.suiteXmlFiles flag — pom.xml already
        //      configures testng.xml via <suiteXmlFiles>, so mvn test is enough
        stage('Run TestNG Tests') {
            steps {
                echo 'Running TestNG test suite...'
                bat 'mvn test'
            }
            post {
                always {
                    // Publish TestNG results in Jenkins UI
                    // FIX: correct report path for surefire-testng 3.1.2
                    // Requires "TestNG Results" plugin in Jenkins
                    testNG '**/target/surefire-reports/testng-results.xml'
                }
            }
        }

        // ── 3. Build jar ──────────────────────────────────────────────────────
        stage('Build with Maven') {
            steps {
                echo 'Building jar...'
                bat 'mvn clean package -DskipTests'
            }
        }

        // ── 4. Build Docker image ─────────────────────────────────────────────
        stage('Build Docker Image') {
            steps {
                echo "Building Docker image: ${IMAGE_NAME}:latest"
                bat "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        // ── 5. Stop and remove old container ─────────────────────────────────
        stage('Remove Old Container') {
            steps {
                echo 'Removing old container if running...'
                // FIX: Windows uses '|| exit 0' not '|| true'
                bat "docker stop ${CONTAINER_NAME} || exit 0"
                bat "docker rm   ${CONTAINER_NAME} || exit 0"
            }
        }

        // ── 6. Start new container ────────────────────────────────────────────
        stage('Run Docker Container') {
            steps {
                echo "Starting ${CONTAINER_NAME} on port ${HOST_PORT}..."
                bat "docker run -d --name ${CONTAINER_NAME} -p ${HOST_PORT}:${APP_PORT} --restart unless-stopped ${IMAGE_NAME}:latest"
            }
        }

        // ── 7. Health check ───────────────────────────────────────────────────
        stage('Health Check') {
            steps {
                echo 'Waiting for Spring Boot to start...'
                // FIX: Windows uses 'timeout' not 'sleep', and curl syntax differs
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