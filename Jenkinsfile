pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
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
                    // work with local variables to work around issues where directly setting environment vars in groovy get ignored
                    def deployDir
                    def serverPort
                    if (env.BRANCH_NAME == 'master') {
                        deployDir = '/opt/wordle-app'
                        serverPort = '8081'
                    } else {
                        deployDir = '/opt/wordle-app-test'
                        serverPort = '8082'
                    }

                    echo "Deploying branch ${env.BRANCH_NAME} to ${deployDir} on port ${serverPort}"

                    env.DEPLOY_DIR = deployDir
                    env.SERVER_PORT = serverPort
                    echo "Env Vars: Deploying branch ${env.BRANCH_NAME} to ${env.DEPLOY_DIR} on port ${env.SERVER_PORT}"
                }
            }
        }

        stage('Deploy') {
            when {
                not { changeRequest() }   // don't deploy PRs, deploy on the commits-pushes
            }
            steps {
                script {
                    if (env.DEPLOY_DIR == '') {
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
