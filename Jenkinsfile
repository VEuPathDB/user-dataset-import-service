#!groovy

node('centos8') {
  def tag = "latest"
  def gitUrl = 'https://github.com/VEuPathDB/service-user-dataset-import.git'
  def imageName = 'user-dataset-import-service'

  stage('checkout') {
    checkout([
      $class: 'GitSCM',
      branches: [[name: env.BRANCH_NAME ]],
      doGenerateSubmoduleConfigurations: false,
      extensions: [[
        $class: 'SubmoduleOption',
        disableSubModules: true
      ]],
      userRemoteConfigs:[[url: gitUrl]]
    ])
  }

  stage('build') {
    withCredentials([
      usernameColonPassword(
        credentialsId: '0f11d4d1-6557-423c-b5ae-693cc87f7b4b',
        variable: 'HUB_LOGIN'
      )
    ]) {

      // set tag to branch if it isn't master
      if (env.BRANCH_NAME != 'master') {
        tag = "${env.BRANCH_NAME}"
      }

      // build the image
      sh "podman build --format=docker -t ${imageName} ."

      // push to dockerhub (for now)
      sh "podman push --creds \"$HUB_LOGIN\" ${imageName} docker://docker.io/veupathdb/${imageName}:${tag}"

      // build postgres image
      sh "podman build --format=docker -f pgDockerfile -t user-dataset-import-datastore ."

      // push to dockerhub (for now)
      sh "podman push --creds \"$HUB_LOGIN\" user-dataset-import-datastore docker://docker.io/veupathdb/user-dataset-import-datastore:${tag}"
    }
  }
}
