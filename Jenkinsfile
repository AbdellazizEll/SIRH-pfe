pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Assurez-vous que ce credential est configuré dans Jenkins
        DOCKERHUB_REPO = 'abdellazizell' // Remplacez par votre nom d'utilisateur Docker Hub
    }

    tools {
        maven 'Maven' // Assurez-vous que 'Maven' est configuré dans Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('sirh-backend') { // Assurez-vous que le chemin est correct
                    bat 'mvn clean package' // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        stage('Test Backend') {
            steps {
                dir('sirh-backend') {
                    bat 'mvn test'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('sirh-frontend') { // Assurez-vous que le chemin est correct
                    bat '''
                    npm install --legacy-peer-deps
                    npm run build
                    ''' // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        // Étape "Test Frontend" désactivée

        stage('Build Docker Images') {
            steps {
                script {
                    // Définir les variables d'environnement pour les images Docker
                    env.backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
                    env.frontendImage = "${DOCKERHUB_REPO}/frontend:${env.BUILD_NUMBER}"

                    // Construire les images Docker
                    bat """
                    docker build -t ${env.backendImage} -f sirh-backend/Dockerfile sirh-backend
                    docker build -t ${env.frontendImage} -f sirh-frontend/Dockerfile sirh-frontend
                    """
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    // Se connecter à Docker Hub
                    bat 'docker login -u %DOCKERHUB_CREDENTIALS_USR% -p %DOCKERHUB_CREDENTIALS_PSW%'

                    // Pousser les images Docker
                    bat """
                    docker push ${env.backendImage}
                    docker push ${env.frontendImage}
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Option 1 : Utiliser les variables d'environnement directement
                    bat """
                    set DOCKERHUB_REPO=${env.DOCKERHUB_REPO}
                    set BUILD_NUMBER=${env.BUILD_NUMBER}
                    docker-compose pull
                    docker-compose up -d
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
        success {
            echo 'Build, test, et push réussis!'
        }
        failure {
            echo 'Build, test, ou push échoué.'
        }
    }
}
