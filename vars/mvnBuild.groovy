def call(mode = "") {
    if(!params.BRANCH) {
        error "Branch parameter is missing"
    }
    if(!fileExists("pipeline-config.json")) {
        error "Pipeline config file is missing"
    }
    def runTests = false
    def AppParams = utils.loadPipelineProps()
    if(AppParams.runTests != null) {
        runTests = AppParams.runTests
    }
    artifact_name = sh(returnStdout: true, script: "echo $JOB_NAME").split('/')[-1].trim().toLowerCase()
    if(AppParams.appname != null) {
        artifact_name = AppParams.appname
    }
    if(fileExists("pom.xml")) {
        if (runTests != true) {
            steps.sh "mvn clean install -DskipTests"
        }
        else {
            steps.sh "mvn clean install"
        }
        steps.sh "mkdir ${artifact_name}"
        steps.sh "cp target/*.jar ${artifact_name}"
        steps.sh "zip -j ${artifact_name}.zip ${artifact_name}/*.jar"   
    }
    else
    {
        error "There is no pom.xml file in the project"
    }
}
