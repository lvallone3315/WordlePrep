// Initialize application version to empty, will populate in build stage
def globalAppVersion = "unknown"

// main pipeline
pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        APP_NAME = 'WordlePrep.jar'
        TEMP_APP_NAME = 'WordlePrep.jar.tmp'
        // Ensures the Maven Extension can see the branch/tag in Jenkins' "Detached HEAD" state
        GIT_BRANCH = "${env.BRANCH_NAME}"
        GIT_COMMIT_REV = "${env.GIT_COMMIT}"
    }

    stages {
        stage('Checkout') {
            steps {
                // prior version - checkout scm
                // 3. Ensure a "Full Clone" so the extension can see your Git Tags
                checkout([$class: 'GitSCM',
                    branches: scm.branches,
                    userRemoteConfigs: scm.userRemoteConfigs,
                    extensions: scm.extensions + [[$class: 'CloneOption', shallow: false, noTags: false, depth: 0]]
                ])
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                script {
                    def branchName = env.BRANCH_NAME ?: sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()

                    if (branchName == 'master' || branchName == 'main') {
                        // 1. Get the latest Git Tag
                        try {
                            globalAppVersion = sh(script: "git describe --tags --abbrev=0", returnStdout: true).trim()
                            // Strip the 'v' if your tags look like v1.0.0
                            globalAppVersion = globalAppVersion.replace('v', '')
                        } catch (Exception e) {
                            // Fallback if no tag exists yet
                            globalAppVersion = "1.0.0-untagged"
                        }
                    } else {
                        // 2. Format for branches (e.g., feature-logic-b42)
                        def safeBranchName = branchName.replaceAll("/", "-")
                        globalAppVersion = "${safeBranchName}-b${env.BUILD_NUMBER}"
                    }

                    echo "--- Building Wordle App Version: ${globalAppVersion} ---"

                    // 3. Run the build and INJECT the version
                    // This ensures your version.properties gets the correct string
                    sh "mvn clean package -DskipTests -Dproject.version=${globalAppVersion}"
                }
            }
        }


        stage('Configure Deployment') {
            when {
                not { changeRequest() }   // ❗ never deploy PRs
            }
            steps {
            // strategy - updates to master deploy to wordle-app
            //    updates on any other branch deploy to wordle-app-test
                script {
                    if (env.BRANCH_NAME == 'master') {
                        env.DEPLOY_DIR = '/opt/wordle-app'
                    } else {
                        env.DEPLOY_DIR = '/opt/wordle-app-test'
                    }
                    echo "Deploying branch ${env.BRANCH_NAME} to ${env.DEPLOY_DIR}"
                }
            }
        }

        stage('Deploy') {
            when {
                not { changeRequest() }   // don't deploy PRs, deploy on the commits-pushes
            }
            steps {
                script {
                    if (!env.DEPLOY_DIR) {
                        error "DEPLOY_DIR was never configured - aborting deployment"
                    }

                    sh """
                    set -e
                    set -x

                    # Note - using sh double quotes, means we need to escape references to Linux vars/commands
                    #   see JAR_SOURCE
                    JAR_SOURCE=\$(ls target/*.jar | head -n 1)
                    [ -f "\$JAR_SOURCE" ] || { echo "JAR not found: \$JAR_SOURCE"; exit 1; }

                    echo "Deploying new JAR, move to .tmp file first to ensure systemd sees complete JAR ..."
                    cp "\$JAR_SOURCE" "$DEPLOY_DIR/$TEMP_APP_NAME"
                    mv "$DEPLOY_DIR/$TEMP_APP_NAME" "$DEPLOY_DIR/$APP_NAME"

                    # Create a versioned backup for easy rollback, create the backup dir if doesn't already exist
                    mkdir -p $DEPLOY_DIR/backups
                    cp "$DEPLOY_DIR/$APP_NAME" "$DEPLOY_DIR/backups/WordlePrep-${globalAppVersion}-b${env.BUILD_NUMBER}.jar"

                    # --- CLEANUP STEP ---
                    echo "Pruning old backups, keeping latest 2..."
                    cd "$DEPLOY_DIR/backups" && ls -t WordlePrep-*.jar | tail -n +3 | xargs rm -f -- || true

                    touch "$DEPLOY_DIR/.deploy-trigger"

                    """
                }
            }
        }
    }
}