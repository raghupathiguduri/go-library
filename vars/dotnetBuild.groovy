def call(String dotnetcmd) 
{
    if(!params.BRANCH) 
    {
        error "Branch parameter is missing"
    }
	try 
    {	
		sh "dotnet ${dotnetcmd}"
    }
    catch (Exception ex) 
    {
        throw ex
    }
}
