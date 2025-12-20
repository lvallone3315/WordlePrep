pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        DEPLOY_DIR = 'UNSET'
        SERVER_PORT = 'UNSET'
        APP_NAME = 'WordlePrep.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Configure Deployment') {
            when {
                not { changeRequest() }   // ❗ never deploy PRs
            }
            steps {
            // strategy - updates to master deploy to wordle-app on port 8081
            //    updates on any other branch deploy to wordle-app-test on port 8082
                script {
                    if (env.BRANCH_NAME == 'master') {
                        env.DEPLOY_DIR = '/opt/wordle-app'
                        env.SERVER_PORT = '8081'
                    } else {
                        env.DEPLOY_DIR = '/opt/wordle-app-test'
                        env.SERVER_PORT = '8082'
                    }

                    echo "Deploying branch ${env.BRANCH_NAME} to ${env.DEPLOY_DIR} on port ${env.SERVER_PORT}"
                }
            }
        }

        stage('Deploy') {
            when {
                not { changeRequest() }   // don't deploy PRs, deploy on the commits-pushes
            }
            steps {
                script {
                    if (env.DEPLOY_DIR == 'UNSET') {
                        error "DEPLOY_DIR was never configured - abortng deployment"
                    }
                }
                sh '''
                set -e

                JAR_SOURCE=$(ls target/*.jar | head -n 1)

                echo "Stopping existing app (if running)..."
                pkill -f "$DEPLOY_DIR/$APP_NAME" || true

                echo "Deploying new JAR..."
                cp "$JAR_SOURCE" "$DEPLOY_DIR/$APP_NAME"

                echo "Starting app on port $SERVER_PORT..."
                nohup java -jar "$DEPLOY_DIR/$APP_NAME" \
                    --server.port=$SERVER_PORT \
                    > "$DEPLOY_DIR/wordle.log" 2>&1 &
                '''
            }
        }
    }
}
