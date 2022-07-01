def call() {
    sh "rm -f workflow.properties"
    if(!params.BRANCH) {
        error "Branch parameter is missing"
    }    
    if(params.BRANCH != "develop") {
        error "Bumping version will be executed only on develop branch"
    }
    def scmUrl = utils.getSCMURL()
    deleteDir()
    git url: "${scmUrl}", branch: "${BRANCH}", credentialsId: "Gitlab"
    //sh "git checkout ${BRANCH}"
    
    def skipCI = utils.validateCommitMsg()
    if(skipCI) {
        utils.appendFile('workflow.properties', "skipCiFlag=true");
        echo "*********************** Skip CI found in Commig msg *************************"
        return
    }
    //Variable used in workflows to decide wheather to continue or not
    utils.appendFile('workflow.properties', "skipCiFlag=false");
    
    def CZ = "/usr/local/bin/cz"
    def appName = utils.getAppName()
    //def chartFolder = "chart/${appName}"
    def chartFolder = utils.getHelmChartPath()
    println "Helm --- " + chartFolder
    def version = sh script: "${CZ} bump --dry-run --yes | awk 'FNR==2' | cut -f2 -d: | tr -d ' '", returnStdout: true
    println "Version -- " + version

    sh "yq w -i ${chartFolder}/values/base-values.yaml image.tag ${version}"
    sh "yq w -i ${chartFolder}/Chart.yaml appVersion ${version}"
    sh "${CZ} bump --yes"
    sh "git branch"
    scmUrl = scmUrl.replaceAll("https://","")    
    withCredentials([usernameColonPassword(credentialsId: 'DT_GITLAB_TOKEN', variable: 'SCM_USER')]) {
        scmUrl = "https://${SCM_USER}@" + scmUrl
        sh "git remote set-url origin ${scmUrl}"
        sh "git push origin ${BRANCH}"
    }

    utils.appendFile('workflow.properties', "version=" + version)
    stash name: "workflowProperties", includes: "workflow.properties"
    //writeFile file: 'workflow.properties', text: "version=" + version

    def dockerImage = sh script: "yq r ${chartFolder}/values/base-values.yaml image.repository | tr -d '\n'", returnStdout: true
    dockerImage = dockerImage.replaceAll("-env","-dev")
    dockerImageTag = sh script: "yq r ${chartFolder}/values/base-values.yaml image.tag | tr -d '\n'", returnStdout: true
    dockerImage = dockerImage + ":" + dockerImageTag
    writeFile file: "image.txt", text: "${dockerImage}"
    stash name: "imageTag", includes: "image.txt"

    echo "================== Image text ======================"
    sh "cat image.txt"
}
