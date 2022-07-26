def call(mode = "") {
	if(!params.BRANCH) {
		error "Branch parameter is missing"
	}
	def ApprunTests = utils.loadPipelineProps("runTests")
	def Appartifact_name = utils.loadPipelineProps("appName")
	def AppVersion = utils.loadPipelineProps("version")
	def AppPublish = utils.loadPipelineProps("publish")
	if(AppPublish != false ) {
	sh "dotnetPublish"
	}
	steps.sh "mkdir ${Appartifact_name}-${AppVersion}"
	if(ApprunTests != false ) { 
	sh "dotnetTest"
	sh "dotnetBuild --outputDirectory ${Appartifact_name}-${AppVersion}"
	}
	else {
	sh"dotnetBuild --outputDirectory ${Appartifact_name}-${AppVersion}"
	}
	steps.sh "echo zipping artifact"
	steps.sh "zip -r '${artifact_name}-${app_version}'.zip '${Appartifact_name}-${AppVersion}'"
}
