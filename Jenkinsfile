pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'abdellazizell' // Your Docker Hub username/repository
        SMTP_SMARTHOST = 'sandbox.smtp.mailtrap.io:2525'
        SMTP_FROM = 'alertmanager@example.com'
    }

    tools {
        maven 'Maven' // Ensure 'Maven' is configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            parallel {
                stage('Build Backend') {
                    steps {
                        dir('sirh-backend') {
                            echo 'Building and Testing Backend...'
                            bat 'mvn clean package'
                            bat 'mvn test'
                        }
                    }
                }
                stage('Build Frontend') {
                    steps {
                        dir('sirh-frontend') {
                            echo 'Building Frontend...'
                            bat '''
                            npm install --legacy-peer-deps
                            npm run build
                            '''
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Backend Image') {
                    steps {
                        script {
                            env.backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
                            echo "Building Backend Docker Image: ${env.backendImage}"
                            bat "docker build -t ${backendImage} -f sirh-backend/Dockerfile sirh-backend"
                        }
                    }
                }
                stage('Frontend Image') {
                    steps {
                        script {
                            env.frontendImage = "${DOCKERHUB_REPO}/frontend:${env.BUILD_NUMBER}"
                            echo "Building Frontend Docker Image: ${env.frontendImage}"
                            bat "docker build -t ${frontendImage} -f sirh-frontend/Dockerfile sirh-frontend"
                        }
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USR', passwordVariable: 'DOCKERHUB_PSW')]) {
                        echo 'Logging into Docker Hub...'
                        bat "docker login -u %DOCKERHUB_USR% -p %DOCKERHUB_PSW%"

                        echo "Pushing Backend Image: ${env.backendImage}"
                        bat "docker push ${env.backendImage}"

                        echo "Pushing Frontend Image: ${env.frontendImage}"
                        bat "docker push ${env.frontendImage}"
                    }
                }
            }
        }

        // CD Steps
        stage('Deploy to Staging') {
            steps {
                script {
                    echo "Deploying to Staging environment..."
                    bat """
                    docker-compose -f docker-compose.staging.yml down --remove-orphans
                    docker-compose -f docker-compose.staging.yml pull
                    docker-compose -f docker-compose.staging.yml up -d --remove-orphans
                    """
                }
            }
        }

        stage('Verify Staging') {
            steps {
                script {
                    echo "Verifying staging environment..."
                    // Initial wait to allow services to start
                    sleep time: 30, unit: 'SECONDS'
                    // Test connectivity before performing health check
                    powershell """
                        Write-Host "Testing connectivity to 127.0.0.1:8086"
                        Test-NetConnection -ComputerName 127.0.0.1 -Port 8086
                    """
                    // Retry mechanism for health check using PowerShell
                    powershell """
                        \$retries = 5
                        \$delay = 10
                        \$count = 0
                        \$success = \$false
                        while (\$count -lt \$retries -and -not \$success) {
                            \$count++
                            Write-Host "Attempt \$count to verify staging..."
                            try {
                                \$response = Invoke-RestMethod -Uri http://127.0.0.1:8086/actuator/health -Method Get
                                if (\$response.status -eq 'UP') {
                                    \$success = \$true
                                    Write-Host "Health check succeeded."
                                } else {
                                    Write-Host "Health check status:", \$response.status
                                }
                            } catch {
                                Write-Host "Health check failed:", \$_
                            }
                            if (-not \$success -and \$count -lt \$retries) {
                                Write-Host "Health check failed, retrying in \$delay seconds..."
                                Start-Sleep -Seconds \$delay
                            }
                        }
                        if (-not \$success) { exit 1 }
                    """
                }
            }
            post {
                success {
                    echo 'Staging environment is up and healthy!'
                }
                failure {
                    error('Failed to verify staging environment.')
                }
            }
        }

        // Optional manual approval before production
        stage('Approval for Production') {
            steps {
                input message: 'Deploy to Production?', ok: 'Yes'
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    echo "Deploying to Production environment..."
                    bat """
                    docker-compose -f docker-compose.prod.yml down --remove-orphans
                    docker-compose -f docker-compose.prod.yml pull
                    docker-compose -f docker-compose.prod.yml up -d --remove-orphans
                    """
                }
            }
        }

        stage('Verify Monitoring (Production)') {
            steps {
                script {
                    echo 'Verifying Prometheus in Production...'
                    // Wait for services to start
                    sleep time: 10, unit: 'SECONDS'
                    // Check Prometheus in production
                    bat 'curl --fail http://localhost:9090/-/healthy'

                    echo 'Verifying Grafana in Production...'
                    sleep time: 10, unit: 'SECONDS'
                    // Check Grafana login page
                    bat 'curl --fail http://localhost:3000/login'
                }
            }
            post {
                success {
                    echo 'Prometheus and Grafana are reachable in Production!'
                }
                failure {
                    error('Failed to verify Monitoring in Production.')
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline ended.'
        }
        success {
            echo 'All steps succeeded! Code is built, tested, and deployed to production.'
        }
        failure {
            echo 'Something failed. Check the logs for details.'
        }
    }
}
