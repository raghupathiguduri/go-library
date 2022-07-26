def call(mode = "") {
	if(!params.BRANCH) {
		error "Branch parameter is missing"
	}
	def ApprunTests = utils.loadPipelineProps("runTests")
	def Appartifact_name = utils.loadPipelineProps("appName")
	def AppVersion = utils.loadPipelineProps("version")
	def AppPublish = utils.loadpipelineProps("publish")
	if(AppPublish.equals(true)) {
	steps.sh "dotnetPublish"
	}
	steps.sh "mkdir ${Appartifact_name}-${AppVersion}"
	if(ApprunTests.equals(true)) { 
	steps.sh "dotnetTest"
	steps.sh "dotnetBuild --outputDirectory ${Appartifact_name}-${AppVersion}"
	}
	else {
	steps.sh"dotnetBuild --outputDirectory ${Appartifact_name}-${AppVersion}"
	}
	steps.sh "echo zipping artifact"
	steps.sh "zip -r '${artifact_name}-${app_version}'.zip '${Appartifact_name}-${AppVersion}'"
}
