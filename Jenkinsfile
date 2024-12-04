pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Assurez-vous d'avoir ce credential configuré
        DOCKERHUB_REPO = 'abdellazizell' // Remplacez par votre nom d'utilisateur Docker Hub
    }

    tools {
        maven 'Maven' // Utilisez le nom configuré dans Jenkins pour Maven
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

        stage('Build Docker Images') {
            steps {
                script {
                    // Définir les variables d'environnement pour qu'elles soient accessibles globalement
                    env.backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
                    env.frontendImage = "${DOCKERHUB_REPO}/frontend:${env.BUILD_NUMBER}"

                    bat """
                    docker build -t ${env.backendImage} -f sirh-backend/Dockerfile sirh-backend
                    docker build -t ${env.frontendImage} -f sirh-frontend/Dockerfile sirh-frontend
                    """ // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    bat 'docker login -u %DOCKERHUB_CREDENTIALS_USR% -p %DOCKERHUB_CREDENTIALS_PSW%'
                    bat """
                    docker push ${env.backendImage}
                    docker push ${env.frontendImage}
                    """ // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
        success {
            echo 'Build et push réussis!'
        }
        failure {
            echo 'Build ou push échoué.'
        }
    }
}
