def call(mode = "") {
    if(!params.BRANCH) {
        error "Branch parameter is missing"
    }
    if(fileExists("pom.xml")) {
        steps.sh "mvn clean install -DskipTests"
        artifact_name = sh(returnStdout: true, script: "echo $JOB_NAME").split('/')[-1].trim().toLowerCase()
        steps.sh "mkdir ${artifact_name}"
        steps.sh "cp target/*.jar ${artifact_name}"
        steps.sh "zip -j ${artifact_name}.zip ${artifact_name}/*.jar"
    }
    else
    {
        error "There is no pom.xml file in the project"
    }
}
